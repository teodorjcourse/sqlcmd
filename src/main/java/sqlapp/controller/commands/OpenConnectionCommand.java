package sqlapp.controller.commands;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.model.ResponseData;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.utils.SplitedString;
import sqlapp.utils.StringUtils;

public class OpenConnectionCommand extends Command {
	DataBaseManager dataBaseManager;

	public OpenConnectionCommand(DataBaseManager dataBaseManager, ResponseModel response) {
		super("open", response);

		this.dataBaseManager = dataBaseManager;
	}

	@Override
	protected void executeInternal(String[] args)
			throws DataBaseRequestException
	{
		dataBaseManager.openConnection(args[0], args[1]);
		responseModel().push(
				new ResponseData(
						AppConfig.translationBase.getTranslation(Keys.CONNECTION_SET.toString())
				)
		);
	}

	@Override
	protected String format() {
		return "open | username | password";
	}

	@Override
	protected String descKey() {
		return Keys.OPEN_CONNECTION_COMMAND_DESC.toString();
	}

//	@Override
//	public String description() {
//		SplitedString splited = SplitedString.split(format(), "\\|");
//
//		return StringUtils.substitute(
//				AppConfig.translationBase.getTranslation(Keys.OPEN_CONNECTION_COMMAND_DESC.toString()),
//				id(),
//				format(),
//				splited.get(1),
//				splited.get(2),
//				splited.get(3)
//		);
//	}
}