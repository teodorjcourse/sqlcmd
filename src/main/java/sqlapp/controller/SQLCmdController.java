package sqlapp.controller;

import sqlapp.ApplicationConfig;
import sqlapp.Keys;
import sqlapp.Log;
import sqlapp.commands.*;
import sqlapp.controller.formatter.TableDataFormatter;
import sqlapp.model.ResponseData;
import sqlapp.model.events.Data;
import sqlapp.model.events.Event;
import sqlapp.model.events.Events;
import sqlapp.controller.exceptions.CommandNotFoundException;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.RequestError;
import sqlapp.utils.StringUtils;
import sqlapp.view.View;

import java.sql.SQLException;
import java.util.*;

public class SQLCmdController {
	private Set<View> views;
	private ResponseModel response;
	private Commands commands;

	public SQLCmdController(DataBaseManager dataBaseManager) {
        views = new HashSet<>();

		response = new ResponseModel();
        response.subscribe(Events.CHANGED.toString(), this::notifyViews);
        response.subscribe(Events.ON_EXIT.toString(), this::TerminateApplication);

		commands = new Commands();
		commands.register(new ConnectCommand(dataBaseManager, response));
		commands.register(new CreateTableCommand(dataBaseManager, response));
		commands.register(new ClearTableCommand(dataBaseManager, response));
		commands.register(new DropTableCommand(dataBaseManager, response));
		commands.register(new InsertCommand(dataBaseManager, response));
		commands.register(new FindTableCommand(dataBaseManager, response));
		commands.register(new GetTablesCommand(dataBaseManager, response));
		commands.register(new ExitCommand(dataBaseManager, response));
		commands.register(new HelpCommand(commands, response));
	}

    private void TerminateApplication(Event event) {
        response.push(
                new ResponseData(
                        new ApplicationConfig.FormattedTranslation(Keys.GOODBYE.toString()).translation()
                )
        );

        System.exit(0);
    }

    public void addView(View view) {
        views.add(view);
    }

	public void monitorConsoleInput() {
        response.push(
                new ResponseData(
                        new ApplicationConfig.FormattedTranslation(Keys.GREETING.toString()).translation()
                ));


		Scanner in = new Scanner(System.in);

		while (true) {
			response.push(
			        new ResponseData(
			                new ApplicationConfig.FormattedTranslation(Keys.INPUT.toString()).translation()
                    )
            );

			onUserInput(in.nextLine());
		}
	}

	public void onUserInput(String inputRequest) {
		try {
			commands.tryToExecute(inputRequest);
		}
		catch (DataBaseRequestException requestException) {
			handleRequestErrors(requestException, inputRequest);
		}
		catch (CommandNotFoundException e) {
			StringBuilder message = new StringBuilder();

			message.append(StringUtils.substitute(new ApplicationConfig.
					FormattedTranslation(Keys.WRONG_COMMAND_ERROR.toString()).translation(), e.request()));

            sendErrorMessage(message.toString());
		}
		catch (Exception e) {
			Log.err(e.getMessage());
			response.push(new ResponseData(e.getMessage()));
		}
	}

	private void handleRequestErrors(DataBaseRequestException requestException, String inputRequest) {
		switch (requestException.error()) {
			case RequestError.DRIVER_LOAD_ERROR:
                onDriverLoadError(requestException);
				break;
			case RequestError.CONNECTION_SET_ERROR:
				onConnectionSetError();
				break;
			case RequestError.CONNECTION_ERROR:
				onConnectionError();
				break;
			case RequestError.REQUEST_ERROR:
                onRequestError(requestException, inputRequest);
				break;
			case RequestError.CREATE_STATEMENT_ERROR:
				onCreateStatementError(requestException);
				break;
			case RequestError.CMD_ARG_ERROR:
				onWrongArgumentValue(requestException, inputRequest);
				break;
		}
	}

    private void notifyViews(Event event) {
		StringBuilder dataMessage = new StringBuilder();
		Data data = event.data();
		dataMessage.append("").append(new TableDataFormatter(data.data()).format());

		for (View view : views) {
            view.render(dataMessage.toString());
        }
    }

	private void onDriverLoadError(DataBaseRequestException requestException) {
        response.push(
                new ResponseData(
                    StringUtils.substitute(
                            new ApplicationConfig.FormattedTranslation(Keys.DRIVER_LOAD_ERROR.toString()).translation(),
                            requestException.getMessage()
                            )
                    )
                );
    }

	private void onConnectionSetError() {
		StringBuilder message = new StringBuilder();

		message.append(new ApplicationConfig.FormattedTranslation(Keys.CONNECTION_SET_ERROR.toString()).translation());
        sendErrorMessage(message.toString());;
	}

	private void onConnectionError() {
		StringBuilder message = new StringBuilder();

		message.append(new ApplicationConfig.FormattedTranslation(Keys.CONNECTION_ERROR.toString()).translation());
        sendErrorMessage(message.toString());
	}

	private void onRequestError(DataBaseRequestException requestException, String inputRequest) {
        StringBuilder message = new StringBuilder();
		String detailedMessage = "";

		Throwable[] supressed = requestException.getSuppressed();
		if (supressed.length > 0) {
			Throwable last = supressed[supressed.length - 1];
			// FIXME избавиться от SQLState в обработке ошибок и выводе сообщений
			// FIXME возможно стоит обрабатывать на уровне модели
			if (last instanceof SQLException) {
				switch (((SQLException) last).getSQLState()) {
					case "42P01":
						detailedMessage = StringUtils.substitute(
								new ApplicationConfig.FormattedTranslation(Keys.DATABASE_NOT_EXIST_ERROR.toString()).translation(),
								inputRequest
						);
						break;
					case "42P07":
						detailedMessage = StringUtils.substitute(
								new ApplicationConfig.FormattedTranslation(Keys.DATABASE_EXISTS_ERROR.toString()).translation(),
								inputRequest
						);
						break;
					default:
						Log.err(last.getMessage());
				}
			}
		}

		message.append(
		        StringUtils.substitute(
		                new ApplicationConfig.FormattedTranslation(Keys.REQUEST_ERROR.toString()).translation(),
                        inputRequest
                )
        );

		message.append(detailedMessage);

        sendErrorMessage(message.toString());
	}

	private void onCreateStatementError(DataBaseRequestException requestException) {
		//TODO
		Log.err(requestException.getMessage());
	}

	private void onWrongArgumentValue(DataBaseRequestException requestException, String inputRequest) {
		StringBuilder message = new StringBuilder();
		message.append(
				StringUtils.substitute(
						new ApplicationConfig
								.FormattedTranslation(Keys.INPUT_ERROR.toString())
								.translation(),
						inputRequest
				)
		);

		message.append(requestException.getMessage());
        sendErrorMessage(message.toString());
	}

	private void sendErrorMessage(String errorMessage) {
        StringBuilder sb = new StringBuilder();

        if (errorMessage != null) {
            sb.append(errorMessage);
        }
        sb.append(new ApplicationConfig.FormattedTranslation(Keys.HELP_INFO_TEXT.toString()).translation());

        response.push(new ResponseData(sb.toString()));
    }
}


