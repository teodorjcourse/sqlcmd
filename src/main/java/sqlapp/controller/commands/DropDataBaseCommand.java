package sqlapp.controller.commands;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseData;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;

public class DropDataBaseCommand extends Command {
	DataBaseManager dataBaseManager;

	public DropDataBaseCommand(DataBaseManager dataBaseManager, ResponseModel response) {
		super("drop_db", response);

		this.dataBaseManager = dataBaseManager;
	}

	@Override
	protected void executeInternal(String[] args)
			throws DataBaseRequestException
	{
		dataBaseManager.dropDataBase(args[0]);
		responseModel().push(
				new ResponseData(
						AppConfig.translationBase.getTranslation(Keys.DATABASE_DROP_SUCCESS.toString())
				)
		);
	}

	@Override
	protected String format() {
		return "drop_db | database";
	}

	@Override
	protected String descKey() {
		return Keys.DROP_DATABASE_COMMAND_DESC.toString();
	}
}
