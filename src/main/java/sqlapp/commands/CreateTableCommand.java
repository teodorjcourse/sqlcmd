package sqlapp.commands;

import sqlapp.ApplicationConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseModel;
import sqlapp.model.ResponseData;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.utils.StringUtils;

public class CreateTableCommand extends Command {
	DataBaseManager dataBaseManager;

	public CreateTableCommand(DataBaseManager dataBaseManager, ResponseModel responseModel) {
		super("create", responseModel);

		this.dataBaseManager = dataBaseManager;
	}

    @Override
    protected void executeInternal(String[] args)
		    throws DataBaseRequestException
    {
    	StringBuilder columns = new StringBuilder();

	    for (int index = 1; index < args.length; index++) {
	    	if (index > 1) {
			    columns.append(", ");
		    }
		    columns.append(args[index]).append(" TEXT");
	    }

	    dataBaseManager.createTable(args[0], columns.toString());
        responseModel().push(
                new ResponseData(
                        StringUtils.substitute(
                                new ApplicationConfig.FormattedTranslation(Keys.CREATE_TABLE_SUCCESS.toString()).translation(),
                                args[0]
                        )
                )
        );
    }

	@Override
	protected String format() {
		return "create | table_name | <column1> | <column2> | <...> | <columnN>";
	}

	@Override
	public String description() {
		StringUtils.SplitedString splited = StringUtils.SplitedString.split(format(), "\\|");

		String[] args = new String[splited.all().length + 1];
		args[0] = id();
		args[1] = format();
		System.arraycopy(splited.all(), 1, args, 2, splited.all().length - 1);

		return StringUtils.substitute(new ApplicationConfig.
						Translation(Keys.CREATE_COMMAND_DESC.toString()).translation(),
						args
		);
	}
}
