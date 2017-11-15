package sqlapp.controller.commands;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseModel;
import sqlapp.model.ResponseData;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.utils.StringUtils;

import java.util.Arrays;

public class CreateTableCommand extends Command {
	DataBaseManager dataBaseManager;

	public CreateTableCommand(DataBaseManager dataBaseManager, ResponseModel responseModel) {
		super("create_tb", responseModel);

		this.dataBaseManager = dataBaseManager;
	}

    @Override
    protected void executeInternal(String[] args)
		    throws DataBaseRequestException
    {

	    dataBaseManager.createTable(args[0], Arrays.copyOfRange(args, 1, args.length));
        responseModel().push(
                new ResponseData(
                        StringUtils.substitute(
		                        AppConfig.translationBase.getTranslation(Keys.CREATE_TABLE_SUCCESS.toString()),
                                args[0]
                        )
                )
        );
    }

	@Override
	protected String format() {
		return "create_tb | table_name | <column1> | <column2> | <...> | <columnN>";
	}

	@Override
	protected String descKey() {
		return Keys.CREATE_COMMAND_DESC.toString();
	}
}
