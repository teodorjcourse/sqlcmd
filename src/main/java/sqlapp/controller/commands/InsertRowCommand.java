package sqlapp.controller.commands;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseData;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.utils.SplitedString;
import sqlapp.utils.StringUtils;
import java.util.Arrays;

public class InsertRowCommand extends Command {
	DataBaseManager dataBaseManager;

	public InsertRowCommand(DataBaseManager dataBaseManager, ResponseModel response) {
		super("insert", response);

		this.dataBaseManager = dataBaseManager;
	}

    @Override
    protected void executeInternal(String[] args)
            throws DataBaseRequestException
    {
		dataBaseManager.insertRow(args[0], Arrays.copyOfRange(args, 1, args.length));
        responseModel().push(
                new ResponseData(
                        StringUtils.substitute(
		                        AppConfig.translationBase.getTranslation(Keys.INSERT_TABLE_SUCCESS.toString()),
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
	protected String descKey() {
		return Keys.INSERT_COMMAND_DESC.toString();
	}

//	@Override
//	public String description() {
//		SplitedString splited = SplitedString.split(format(), "\\|");
//
//		String[] args = new String[splited.all().length + 1];
//		args[0] = id();
//		args[1] = format();
//		System.arraycopy(splited.all(), 1, args, 2, splited.all().length - 1);
//
//		return StringUtils.substitute(
//				AppConfig.translationBase.getTranslation(Keys.INSERT_COMMAND_DESC.toString()),
//				args
//		);
//	}
}
