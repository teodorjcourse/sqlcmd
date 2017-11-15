package sqlapp.controller.commands;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseData;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.model.ResponseModel;

import java.util.List;

public class GetTablesCommand extends Command {
	DataBaseManager dataBaseManager;

	public GetTablesCommand(DataBaseManager dataBaseManager, ResponseModel responseModel) {
        super("tables", responseModel);

		this.dataBaseManager = dataBaseManager;
    }

    @Override
    protected void executeInternal(String[] args)
		    throws DataBaseRequestException
    {
		List<String> tablesNames = dataBaseManager.getTables();

		Object responseData = tablesNames.size() == 0 ?
                AppConfig.translationBase.getTranslation(Keys.EMPTY_TABLE_TEXT.toString()) :
                tablesNames;

		responseModel().push(new ResponseData(responseData));
    }

	@Override
	protected String format() {
		return id();
	}

	@Override
	protected String descKey() {
		return Keys.TABLES_COMMAND_DESC.toString();
	}
}
