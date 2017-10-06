package sqlapp.commands;

import sqlapp.ApplicationConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseData;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.model.ResponseModel;
import sqlapp.utils.StringUtils;

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
		responseModel().push(new ResponseData(tablesNames));
    }

	@Override
	protected String format() {
		return id();
	}

	@Override
	public String description() {
		return StringUtils.substitute(new ApplicationConfig.
				FormattedTranslation(Keys.TABLES_COMMAND_DESC.toString()).translation(), id(), id());
	}
}
