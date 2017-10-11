package sqlapp.controller.commands;


import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseData;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;

public class CloseConnection extends Command {
	DataBaseManager dataBaseManager;

	public CloseConnection(DataBaseManager dataBaseManager, ResponseModel responseModel) {
		super("close", responseModel);

		this.dataBaseManager = dataBaseManager;
	}

	@Override
	protected void executeInternal(String[] args) throws DataBaseRequestException {
		dataBaseManager.closeConnection();

		responseModel().push(new ResponseData(
				AppConfig.translationBase.getTranslation(Keys.CONNECTION_CLOSED.toString())
		));
	}

	@Override
	protected String format() {
		return "close";
	}

	@Override
	protected String descKey() {
		return Keys.CLOSE_CONNECTION_COMMAND_DESC.toString();
	}
}
