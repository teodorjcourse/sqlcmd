package sqlapp.commands;

import sqlapp.ApplicationConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseModel;
import sqlapp.model.ResponseData;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.utils.StringUtils;

public class DropTableCommand extends Command {
	DataBaseManager dataBaseManager;

	public DropTableCommand(DataBaseManager dataBaseManager, ResponseModel responseModel) {
		super("drop", responseModel);

		this.dataBaseManager = dataBaseManager;
	}

    @Override
    protected void executeInternal(String[] args)
		    throws DataBaseRequestException
    {
	    dataBaseManager.dropTable(args[0]);
		responseModel().push(
				new ResponseData(
				        StringUtils.substitute(
				                new ApplicationConfig.FormattedTranslation(Keys.DROP_TABLE_SUCCESS.toString()).translation(),
                                args[0]
                        )
                )
        );
    }

	@Override
	protected String format() {
		return "drop | table_name";
	}

	@Override
	public String description() {
		StringUtils.SplitedString splited = StringUtils.SplitedString.split(format(), "\\|");

		return StringUtils.substitute(
				new ApplicationConfig
						.FormattedTranslation(Keys.DROP_COMMAND_DESC.toString())
						.translation(),
				id(),
				format(),
				splited.get(1)
		);
	}
}
