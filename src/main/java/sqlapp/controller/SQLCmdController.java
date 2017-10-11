package sqlapp.controller;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.Log;
import sqlapp.controller.commands.*;
import sqlapp.controller.formatter.TableDataFormatter;
import sqlapp.model.ResponseData;
import sqlapp.model.events.Callback;
import sqlapp.model.events.Data;
import sqlapp.model.events.Event;
import sqlapp.model.events.Events;
import sqlapp.controller.exceptions.CommandNotFoundException;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.model.ResponseModel;
import sqlapp.utils.StringUtils;
import sqlapp.view.View;
import java.util.*;

public class SQLCmdController {
	private Set<View> views;

	private ErrorHandler error;
	private ResponseModel response;
	private Commands commands;

	private Callback notifyViewersCallback;
	private Callback terminateApplicationCallback;

	public SQLCmdController(DataBaseManager dataBaseManager) {
        views = new HashSet<>();

		notifyViewersCallback = this::notifyViews;
		terminateApplicationCallback = this::terminateApplication;

		response = new ResponseModel();
        response.subscribe(Events.CHANGED.toString(), notifyViewersCallback);
        response.subscribe(Events.ON_EXIT.toString(), terminateApplicationCallback);

		error = new ErrorHandler(response);

		commands = new Commands();
		commands.register(new ChangeLanguage(response));
		commands.register(new OpenConnectionCommand(dataBaseManager, response));
		commands.register(new CloseConnection(dataBaseManager, response));
		commands.register(new ConnectCommand(dataBaseManager, response));
		commands.register(new CreateDataBaseCommand(dataBaseManager, response));
		commands.register(new DropDataBaseCommand(dataBaseManager, response));
		commands.register(new CreateTableCommand(dataBaseManager, response));
		commands.register(new DropTableCommand(dataBaseManager, response));
		commands.register(new ClearTableCommand(dataBaseManager, response));
		commands.register(new InsertRowCommand(dataBaseManager, response));
		commands.register(new UpdateTableCommand(dataBaseManager, response));
		commands.register(new DeleteRowCommand(dataBaseManager, response));
		commands.register(new GetTablesCommand(dataBaseManager, response));
		commands.register(new SelectTableCommand(dataBaseManager, response));
		commands.register(new ExitCommand(dataBaseManager, response));
		commands.register(new HelpCommand(commands, response));
	}

    private void terminateApplication(Event event) {
        response.push(
                new ResponseData(
		                AppConfig.translationBase.getTranslation(Keys.GOODBYE.toString())
                )
        );

	    response.unsubscribe(Events.CHANGED.toString(), notifyViewersCallback);
	    response.unsubscribe(Events.ON_EXIT.toString(), terminateApplicationCallback);

	    notifyViewersCallback = null;
	    terminateApplicationCallback = null;

        System.exit(0);
    }

    public void addView(View view) {
        views.add(view);
    }

	public void onUserInput(String inputRequest) {
		try {
			commands.tryToExecute(inputRequest);
		}
		catch (DataBaseRequestException requestException) {
			error.handleRequestErrors(requestException, inputRequest);
		}
		catch (CommandNotFoundException e) {
			StringBuilder message = new StringBuilder();

			message.append(StringUtils.substitute(
					AppConfig.translationBase.getTranslation(Keys.WRONG_COMMAND_ERROR.toString()),
					e.request()
					)
			);

			error.sendErrorMessage(message.toString());
		}
		catch (Exception e) {
			Log.err(e.getMessage());
			error.sendMessage(e.getMessage());
		}
	}

	public void notifyViews(Event event) {
		StringBuilder dataMessage = new StringBuilder();
		Data data = event.data();
		dataMessage.append("").append(new TableDataFormatter(data.data()).format());

		for (View view : views) {
			view.render(dataMessage.toString());
		}
	}
}


