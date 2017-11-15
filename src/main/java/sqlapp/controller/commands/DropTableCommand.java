package sqlapp.controller.commands;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseModel;
import sqlapp.model.ResponseData;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.utils.StringUtils;

public class DropTableCommand extends Command {
	DataBaseManager dataBaseManager;

	public DropTableCommand(DataBaseManager dataBaseManager, ResponseModel responseModel) {
		super("drop_tb", responseModel);

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
						        AppConfig.translationBase.getTranslation(Keys.DROP_TABLE_SUCCESS.toString()),
                                args[0]
                        )
                )
        );
    }

	@Override
	protected String format() {
		return "drop_tb | table_name";
	}

	@Override
	protected String descKey() {
		return Keys.DROP_COMMAND_DESC.toString();
	}
}
