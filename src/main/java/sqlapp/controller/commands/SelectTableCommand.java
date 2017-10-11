package sqlapp.controller.commands;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseModel;
import sqlapp.model.ResponseData;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.utils.SplitedString;
import sqlapp.utils.StringUtils;

import java.util.ArrayList;

public class SelectTableCommand extends Command {
	DataBaseManager dataBaseManager;

	public SelectTableCommand(DataBaseManager dataBaseManager, ResponseModel responseModel) {
		super("select", responseModel);

		this.dataBaseManager = dataBaseManager;
	}

    @Override
    protected void executeInternal(String[] args)
			throws DataBaseRequestException
	{
        ArrayList<ArrayList<String>> tableContent = dataBaseManager.selectTable(args[0]);
        responseModel().push(new ResponseData(tableContent));
    }

	@Override
	protected String format() {
		return "select | table_name";
	}

	@Override
	protected String descKey() {
		return Keys.FIND_COMMAND_DESC.toString();
	}

//	@Override
//	public String description() {
//		SplitedString splited = SplitedString.split(format(), "\\|");
//
//		return StringUtils.substitute(
//				AppConfig.translationBase.getTranslation(Keys.FIND_COMMAND_DESC.toString()),
//				id(),
//				format(),
//				splited.get(1)
//		);
//	}
}
