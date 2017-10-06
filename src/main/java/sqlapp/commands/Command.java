package sqlapp.commands;

import sqlapp.ApplicationConfig;
import sqlapp.Keys;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.RequestError;
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
		return ID;
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
		StringUtils.SplitedString formatArgs = StringUtils.SplitedString.split(format(), "\\|");
		int l = formatArgs.all().length - 1;

		for (int index = 0; index < l; index++) {
			if (!formatArgs.get(index + 1).matches("^<.*>$")
					&& (index == args.length|| StringUtils.nullOrEmpty(args[index])))
			{

				DataBaseRequestException requestException =
						new DataBaseRequestException(
								RequestError.CMD_ARG_ERROR,
								StringUtils.substitute(
										new ApplicationConfig
												.FormattedTranslation(Keys.COMMAND_ARGUMENT_ERROR.toString())
												.translation(),
										formatArgs.get(index + 1)
								)
						);

				throw requestException;
			}
		}
	}

	protected abstract void executeInternal(String[] args) throws DataBaseRequestException;

	protected abstract String format();

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
