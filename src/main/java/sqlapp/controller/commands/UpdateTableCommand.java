package sqlapp.controller.commands;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseData;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.utils.StringUtils;

import java.util.Arrays;

public class UpdateTableCommand extends Command {
	DataBaseManager dataBaseManager;

	public UpdateTableCommand(DataBaseManager dataBaseManager, ResponseModel responseModel) {
		super("update", responseModel);

		this.dataBaseManager = dataBaseManager;
	}

    @Override
    protected void executeInternal(String[] args)
		    throws DataBaseRequestException
    {
	    int result = dataBaseManager.updateRows(args[0], args[1], args[2], Arrays.copyOfRange(args, 3, args.length));

	    String key = result > 0 ? Keys.UPDATE_ROW_SUCCESS.toString() : Keys.UPDATE_ROW_SUCCESS_ZERO.toString();
	    responseModel().push(
			    new ResponseData(
					    StringUtils.substitute(
							    AppConfig.translationBase.getTranslation(key),
							    new String[]{args[0], Integer.toString(result)}
					    )
			    )
	    );

    }

	@Override
	protected String format() {
		return "update | table_name | column | value | column1 | value1 | <...> | <columnN> | <valueN>";
	}

	@Override
	protected String descKey() {
		return Keys.UPDATE_COMMAND_DESC.toString();
	}
}
