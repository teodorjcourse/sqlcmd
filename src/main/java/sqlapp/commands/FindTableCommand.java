package sqlapp.commands;

import sqlapp.ApplicationConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseModel;
import sqlapp.model.ResponseData;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.utils.StringUtils;

import java.util.ArrayList;

public class FindTableCommand extends Command {
	DataBaseManager dataBaseManager;

	public FindTableCommand(DataBaseManager dataBaseManager, ResponseModel responseModel) {
		super("find", responseModel);

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
		return "find | table_name";
	}

	@Override
	public String description() {
		StringUtils.SplitedString splited = StringUtils.SplitedString.split(format(), "\\|");

		return StringUtils.substitute(
				new ApplicationConfig
						.FormattedTranslation(Keys.FIND_COMMAND_DESC.toString())
						.translation(),
				id(),
				format(),
				splited.get(1)
		);
	}
}
