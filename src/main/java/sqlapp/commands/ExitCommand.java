package sqlapp.commands;

import sqlapp.ApplicationConfig;
import sqlapp.Keys;
import sqlapp.model.events.Event;
import sqlapp.model.events.Events;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.utils.StringUtils;

public class ExitCommand extends Command {
	DataBaseManager dataBaseManager;

	public ExitCommand(DataBaseManager dataBaseManager, ResponseModel responseModel) {
		super("exit", responseModel);

		this.dataBaseManager = dataBaseManager;
	}

	@Override
    protected void executeInternal(String[] args)
			throws DataBaseRequestException
	{
		dataBaseManager.closeConnection();
	    responseModel().dispatch(new Event(Events.ON_EXIT.toString()));
    }

	@Override
	protected String format() {
		return "exit";
	}

	@Override
	public String description() {
    	return StringUtils.
			    substitute(new ApplicationConfig.
					    FormattedTranslation(Keys.EXIT_COMMAND_DESC.toString()).translation(), id(), id());
	}
}
