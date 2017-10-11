package sqlapp.controller.commands;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseModel;
import sqlapp.model.ResponseData;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.utils.SplitedString;
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
		                        AppConfig.translationBase.getTranslation(Keys.CLEAR_TABLE_SUCCESS.toString()),
				                args[0])
                )
        );
    }

	@Override
	protected String format() {
		return "clear | table_name";
	}

	@Override
	protected String descKey() {
		return Keys.CLEAR_COMMAND_DESC.toString();
	}

//	@Override
//	public String description() {
//		SplitedString splited = SplitedString.split(format(), "\\|");
//
//		return StringUtils.substitute(
//				AppConfig.translationBase.getTranslation(Keys.CLEAR_COMMAND_DESC.toString()),
//				id(),
//				format(),
//				splited.get(1)
//				);
//	}
}
