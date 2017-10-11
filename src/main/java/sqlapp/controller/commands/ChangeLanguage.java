package sqlapp.controller.commands;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.Locale;
import sqlapp.model.ResponseData;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.utils.SplitedString;
import sqlapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ChangeLanguage extends Command {

	public ChangeLanguage(ResponseModel responseModel) {
		super("language", responseModel);
	}

	@Override
	protected void executeInternal(String[] args)
			throws DataBaseRequestException {

		for (Locale c : Locale.values()) {
			if (c.toString().equals(args[0])) {
				AppConfig.translationBase.setLocale(c);

				responseModel().push(
						new ResponseData(
								StringUtils.substitute(
										AppConfig.translationBase.getTranslation(Keys.LANGUAGE_CHANGED_SUCCESS.toString()),
										args[0])
						)
				);
				return;
			}
		}

		responseModel().push(
				new ResponseData(
						StringUtils.substitute(
								AppConfig.translationBase.getTranslation(Keys.LANGUAGE_CHANGED_ERROR.toString()),
								args[0])
				)
		);
	}

	@Override
	protected String format() {
		return "language | locale";
	}

	@Override
	protected String descKey() {
		return Keys.LANGUAGE_COMMAND_DESC.toString();
	}

	@Override
	public String description() {
		SplitedString splited = SplitedString.split(format(), "\\|");
		StringBuilder sb = new StringBuilder();

		for (Locale c : Locale.values()) {
			if (sb.length() > 0) {
				sb.append(", ");
			}

			sb.append(c.toString());
		}

		sb.append(";");

		return StringUtils.substitute(
				AppConfig.translationBase.getTranslation(Keys.LANGUAGE_COMMAND_DESC.toString()),
				id(),
				format(),
				splited.get(1),
				sb.toString()
		);
	}
}
