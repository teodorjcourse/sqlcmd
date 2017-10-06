package sqlapp.commands;

import sqlapp.ApplicationConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseData;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.model.ResponseModel;
import sqlapp.utils.StringUtils;

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
	                    new ApplicationConfig.FormattedTranslation(Keys.CONNECTION_SET.toString()).translation()
                )
        );
    }

	@Override
	protected String format() {
		return "connect | database | username | password";
	}

	@Override
	public String description() {
		StringUtils.SplitedString splited = StringUtils.SplitedString.split(format(), "\\|");

		return StringUtils.substitute(new ApplicationConfig.
						Translation(Keys.CONNECT_COMMAND_DESC.toString()).translation(),
				id(), format(), splited.get(1), splited.get(2), splited.get(3));
	}
}
