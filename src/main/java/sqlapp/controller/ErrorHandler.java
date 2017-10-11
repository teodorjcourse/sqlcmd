package sqlapp.controller;

import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.Log;
import sqlapp.model.ResponseData;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;
import sqlapp.utils.StringUtils;

import java.sql.SQLException;

public class ErrorHandler {
	private ResponseModel response;

	public ErrorHandler(ResponseModel response) {
		this.response = response;
	}

	public void handleRequestErrors(DataBaseRequestException requestException, String inputRequest) {
		switch (requestException.error()) {
			case RequestError.DRIVER_LOAD_ERROR:
				onDriverLoadError(requestException);
				break;
			case RequestError.CONNECTION_SET_ERROR:
				onConnectionSetError();
				break;
			case RequestError.CONNECTION_ALREADY_SET:
				sendMessage(AppConfig.translationBase.getTranslation(Keys.CONNECTION_ALREADY_SET.toString()));
				break;
			case RequestError.CONNECTION_DOESNT_SET:
				onConnectionError();
				break;
			case RequestError.REQUEST_ERROR:
				onRequestError(requestException, inputRequest);
				break;
			case RequestError.SYNTAX_ERROR:
				onSyntaxError(requestException, inputRequest);
				break;
			case RequestError.CREATE_STATEMENT_ERROR:
				onCreateStatementError(requestException);
				break;
			case RequestError.CMD_ARG_ERROR:
				onWrongArgumentValue(requestException, inputRequest);
				break;
			case RequestError.TABLE_DOESNT_EXISTS_ERROR:
				sendMessage(AppConfig.translationBase.getTranslation(Keys.TABLE_DOESNT_EXIST_ERROR.toString()));
				break;
			case RequestError.TABLE_ALREADY_EXISTS_ERROR:
				sendMessage(AppConfig.translationBase.getTranslation(Keys.TABLE_ALREADY_EXISTS_ERROR.toString()));
				break;
			case RequestError.DATABASE_DOESNT_EXISTS_ERROR:
				sendMessage(AppConfig.translationBase.getTranslation(Keys.DATABASE_DOESNT_EXISTS_ERROR.toString()));
				break;
			case RequestError.DATABASE_ALREADY_EXISTS_ERROR:
				sendMessage(AppConfig.translationBase.getTranslation(Keys.DATABASE_ALREADY_EXISTS_ERROR.toString()));
				break;
		}
	}

	public void sendMessage(String message) {
		response.push(new ResponseData(message));
	}

	public void sendErrorMessage(String errorMessage) {
		StringBuilder sb = new StringBuilder();

		if (errorMessage != null) {
			sb.append(errorMessage);
		}
		sb.append(AppConfig.translationBase.getTranslation(Keys.HELP_INFO_TEXT.toString()));

		response.push(new ResponseData(sb.toString()));
	}


	private void onSyntaxError(DataBaseRequestException requestException, String inputRequest) {
		StringBuilder message = new StringBuilder();

		message.append(AppConfig.translationBase.getTranslation(Keys.SYNTAX_ERROR.toString()));
		sendErrorMessage(message.toString());
	}

	private void onConnectionSetError() {
		StringBuilder message = new StringBuilder();

		message.append(AppConfig.translationBase.getTranslation(Keys.CONNECTION_SET_ERROR.toString()));
		sendErrorMessage(message.toString());;
	}

	private void onConnectionError() {
		StringBuilder message = new StringBuilder();

		message.append(AppConfig.translationBase.getTranslation(Keys.CONNECTION_ERROR.toString()));
		sendErrorMessage(message.toString());
	}

	private void onRequestError(DataBaseRequestException requestException, String inputRequest) {
		StringBuilder message = new StringBuilder();
		String detailedMessage = "";

		Throwable[] supressed = requestException.getSuppressed();
		if (supressed.length > 0) {
			Throwable last = supressed[supressed.length - 1];
			if (last instanceof SQLException) {
				Log.err(last.getMessage());
				detailedMessage = last.getMessage();
			}
		}

		message.append(
				StringUtils.substitute(
						AppConfig.translationBase.getTranslation(Keys.REQUEST_ERROR.toString()),
						inputRequest
				)
		);

		message.append(detailedMessage);

		sendErrorMessage(message.toString());
	}

	private void onCreateStatementError(DataBaseRequestException requestException) {
		Log.err(requestException.getMessage());
	}

	private void onDriverLoadError(DataBaseRequestException requestException) {
		response.push(
				new ResponseData(
						StringUtils.substitute(
								AppConfig.translationBase.getTranslation(Keys.DRIVER_LOAD_ERROR.toString()),
								requestException.getMessage()
						)
				)
		);
	}

	private void onWrongArgumentValue(DataBaseRequestException requestException, String inputRequest) {
		StringBuilder message = new StringBuilder();
		message.append(
				StringUtils.substitute(
						AppConfig.translationBase.getTranslation(Keys.INPUT_ERROR.toString()),
						inputRequest
				)
		);

		message.append(requestException.getMessage());
		sendErrorMessage(message.toString());
	}

}
