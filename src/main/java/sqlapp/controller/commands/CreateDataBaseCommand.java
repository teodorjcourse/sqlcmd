package sqlapp.controller.commands;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseData;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.model.ResponseModel;

public class CreateDataBaseCommand extends Command {
	DataBaseManager dataBaseManager;

	public CreateDataBaseCommand(DataBaseManager dataBaseManager, ResponseModel response) {
		super("create_db", response);

		this.dataBaseManager = dataBaseManager;
	}

	@Override
	protected void executeInternal(String[] args)
			throws DataBaseRequestException
	{
		dataBaseManager.createDataBase(args[0]);
		responseModel().push(
				new ResponseData(
						AppConfig.translationBase.getTranslation(Keys.DATABASE_CREATE_SUCCESS.toString())
				)
		);
	}

	@Override
	protected String format() {
		return "create_db | database";
	}

	@Override
	protected String descKey() {
		return Keys.CREATE_DATABASE_COMMAND_DESC.toString();
	}
}
