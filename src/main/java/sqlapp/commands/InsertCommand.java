package sqlapp.commands;

import sqlapp.ApplicationConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseData;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class InsertCommand extends Command {
	DataBaseManager dataBaseManager;

	public InsertCommand(DataBaseManager dataBaseManager, ResponseModel response) {
		super("insert", response);

		this.dataBaseManager = dataBaseManager;
	}

    @Override
    protected void executeInternal(String[] args)
            throws DataBaseRequestException
    {
		String tableName = args[0];

        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList();

		for (int index = 1; index < args.length; index++) {
		    if (index % 2 != 0) {
		        if (StringUtils.nullOrEmpty(args[index])) {
		            throw new DataBaseRequestException(RequestError.CMD_ARG_ERROR);
                }

                keys.add(args[index]);
            } else {
		        if (StringUtils.nullOrEmpty(args[index])) {
                    values.add("DEFAULT");
                }
                values.add("'" + args[index] + "'");
            }
        }

        if (keys.size() > values.size()) {
            values.add("DEFAULT");
        }

		dataBaseManager.insert(tableName, String.join(",", keys), String.join(",", values));
        responseModel().push(
                new ResponseData(
                        StringUtils.substitute(
                                new ApplicationConfig.FormattedTranslation(Keys.INSERT_TABLE_SUCCESS.toString()).translation(),
                                args[0]
                        )
                )
        );
    }

	@Override
	protected String format() {
		return "insert | table_name | column1 | <value1> | <column2> | <value2> | <...> | <columnN> | <valueN>";
	}

	@Override
	public String description() {
		StringUtils.SplitedString splited = StringUtils.SplitedString.split(format(), "\\|");

		String[] args = new String[splited.all().length + 1];
		args[0] = id();
		args[1] = format();
		System.arraycopy(splited.all(), 1, args, 2, splited.all().length - 1);

		return StringUtils.substitute(new ApplicationConfig.
						Translation(Keys.INSERT_COMMAND_DESC.toString()).translation(),
				args
		);
	}
}
