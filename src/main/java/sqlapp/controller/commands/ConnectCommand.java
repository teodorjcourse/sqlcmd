package sqlapp.controller.commands;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseData;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.model.ResponseModel;

public class ConnectCommand extends Command {
	DataBaseManager dataBaseManager;

    public ConnectCommand(DataBaseManager dataBaseManager, ResponseModel response) {
        super("connect", response);

	    this.dataBaseManager = dataBaseManager;
    }

    @Override
    protected void executeInternal(String[] args)
		    throws DataBaseRequestException
    {
	    dataBaseManager.connect(args[0], args[1], args[2]);
	    responseModel().push(
	            new ResponseData(
			            AppConfig.translationBase.getTranslation(Keys.CONNECTION_SET.toString())
                )
        );
    }

	@Override
	protected String format() {
		return "connect | database | username | password";
	}

	@Override
	protected String descKey() {
		return Keys.CONNECT_COMMAND_DESC.toString();
	}
}
