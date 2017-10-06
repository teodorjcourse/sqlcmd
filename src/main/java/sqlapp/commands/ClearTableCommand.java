package sqlapp.commands;

import sqlapp.ApplicationConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseModel;
import sqlapp.model.ResponseData;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.utils.StringUtils;

public class ClearTableCommand extends Command {
	DataBaseManager dataBaseManager;

	public ClearTableCommand(DataBaseManager dataBaseManager, ResponseModel response) {
    	super("clear", response);

		this.dataBaseManager = dataBaseManager;
	}

	@Override
    protected void executeInternal(String[] args)
			throws DataBaseRequestException
	{
		dataBaseManager.clearTable(args[0]);
		responseModel().push(
		        new ResponseData(
		                StringUtils.substitute(
		                        new ApplicationConfig
                                        .FormattedTranslation(Keys.CLEAR_TABLE_SUCCESS.toString())
                                        .translation(),
                        args[0])
                )
        );
    }

	@Override
	protected String format() {
		return "clear | table_name";
	}

	@Override
	public String description() {
		StringUtils.SplitedString splited = StringUtils.SplitedString.split(format(), "\\|");

		return StringUtils.substitute(
				new ApplicationConfig
						.FormattedTranslation(Keys.CLEAR_COMMAND_DESC.toString())
						.translation(),
				id(),
				format(),
				splited.get(1)
				);
	}
}
