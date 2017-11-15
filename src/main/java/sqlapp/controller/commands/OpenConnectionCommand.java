package sqlapp.controller.commands;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseData;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;

public class OpenConnectionCommand extends Command {
	DataBaseManager dataBaseManager;

	public OpenConnectionCommand(DataBaseManager dataBaseManager, ResponseModel response) {
		super("open", response);

		this.dataBaseManager = dataBaseManager;
	}

	@Override
	protected void executeInternal(String[] args)
			throws DataBaseRequestException
	{
		dataBaseManager.openConnection(args[0], args[1]);
		responseModel().push(
				new ResponseData(
						AppConfig.translationBase.getTranslation(Keys.CONNECTION_SET.toString())
				)
		);
	}

	@Override
	protected String format() {
		return "open | username | password";
	}

	@Override
	protected String descKey() {
		return Keys.OPEN_CONNECTION_COMMAND_DESC.toString();
	}
}