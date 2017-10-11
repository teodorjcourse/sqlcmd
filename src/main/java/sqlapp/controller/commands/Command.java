package sqlapp.controller.commands;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.RequestError;
import sqlapp.utils.SplitedString;
import sqlapp.utils.StringUtils;

public abstract class Command {
	private final String ID;
	private final ResponseModel responseModel;

	Command(String id, ResponseModel responseModel) {
		ID = id;
		this.responseModel = responseModel;
	}

	Command(String id) {
		this(id, null);
	}

	public String description() {
		SplitedString splited = SplitedString.split(format(), "\\|");

		String[] args = new String[splited.all().length + 1];
		args[0] = id();
		args[1] = format();
		System.arraycopy(splited.all(), 1, args, 2, splited.all().length - 1);

		return StringUtils.substitute(
				AppConfig.translationBase.getTranslation(descKey()),
				args
		);
	}

	public final String id() {
		return ID;
	}

	public final void execute(String[] args)
			throws DataBaseRequestException
	{
		validateCommandArguments(args);
		executeInternal(args);
	}

	private void validateCommandArguments(String[] args)
			throws DataBaseRequestException
	{
		SplitedString formatArgs = SplitedString.split(format(), "\\|");
		int l = formatArgs.all().length - 1;

		for (int index = 0; index < l; index++) {
			if (!formatArgs.get(index + 1).matches("^<.*>$")
					&& (index == args.length|| StringUtils.nullOrEmpty(args[index])))
			{

				DataBaseRequestException requestException =
						new DataBaseRequestException(
								RequestError.CMD_ARG_ERROR,
								StringUtils.substitute(
										AppConfig.translationBase.getTranslation(Keys.COMMAND_ARGUMENT_ERROR.toString()),
										formatArgs.get(index + 1)
								)
						);

				throw requestException;
			}
		}
	}

	protected abstract void executeInternal(String[] args) throws DataBaseRequestException;

	protected abstract String format();

	protected abstract String descKey();

	protected ResponseModel responseModel() {
		return this.responseModel;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null ||
				!(obj instanceof String))
		{
			return false;
		}

		return ID.equals((String)obj);
	}
}
