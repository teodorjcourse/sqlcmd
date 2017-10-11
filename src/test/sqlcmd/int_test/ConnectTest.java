package sqlcmd.int_test;

import org.mockito.Mockito;
import sqlapp.Keys;
import sqlapp.model.exceptions.DataBaseRequestException;

import java.util.List;

public class ConnectTest extends IntBase {
	//connect | database | username | password

	/**
	 * Если юзер вводит неправильный формат комнды, то он увидит сообщения:
	 * Keys.INPUT_ERROR
	 * Keys.COMMAND_ARGUMENT_ERROR
	 * Keys.HELP_INFO_TEXT
	 */
	public void testConnectWrongInput() {
		final String[] inputs = {
				"connect",
				"connect |",
				"connect | database",
				"connect | database|",
				"connect | database | username",
				"connect | database | username |"
		};

		final String[] expectedMessages = {
				new StringBuilder().append(Keys.INPUT_ERROR).append(Keys.COMMAND_ARGUMENT_ERROR).append(Keys.HELP_INFO_TEXT).toString(),
				new StringBuilder().append(Keys.INPUT_ERROR).append(Keys.COMMAND_ARGUMENT_ERROR).append(Keys.HELP_INFO_TEXT).toString(),
				new StringBuilder().append(Keys.INPUT_ERROR).append(Keys.COMMAND_ARGUMENT_ERROR).append(Keys.HELP_INFO_TEXT).toString(),
				new StringBuilder().append(Keys.INPUT_ERROR).append(Keys.COMMAND_ARGUMENT_ERROR).append(Keys.HELP_INFO_TEXT).toString(),
				new StringBuilder().append(Keys.INPUT_ERROR).append(Keys.COMMAND_ARGUMENT_ERROR).append(Keys.HELP_INFO_TEXT).toString(),
				new StringBuilder().append(Keys.INPUT_ERROR).append(Keys.COMMAND_ARGUMENT_ERROR).append(Keys.HELP_INFO_TEXT).toString()
		};

		for (String input : inputs) {
			controller.onUserInput(input);
		}

		Mockito.verify(viewMock, Mockito.times(inputs.length)).render(argument.capture());

		List<String> capturedArgs = argument.getAllValues();

		for (int index = 0; index < inputs.length; index++) {
			assertEquals(expectedMessages[index].toString(), capturedArgs.get(index));
		}
	}

	/**
	 * Если вводит неверный пароль и юзер нейм:
	 * Keys.CONNECTION_SET_ERROR
	 * Keys.HELP_INFO_TEXT
	 */
	public void testConnectWrongAuthData() {
		try {
			prepareDatabase();
		} catch (DataBaseRequestException e) {
			e.printStackTrace();
		}

		final String[] inputs = {
				"connect | " + TEST_DATABASE_NAME + "|" + username() + "|wrong",
				"connect | " + TEST_DATABASE_NAME + "|wrong|" + password()
		};

		final String[] expectedMessages = {
				new StringBuilder().append(Keys.CONNECTION_SET_ERROR).append(Keys.HELP_INFO_TEXT).toString(),
				new StringBuilder().append(Keys.CONNECTION_SET_ERROR).append(Keys.HELP_INFO_TEXT).toString()
		};

		for (String input : inputs) {
			controller.onUserInput(input);
		}

		Mockito.verify(viewMock, Mockito.times(inputs.length)).render(argument.capture());

		List<String> capturedArgs = argument.getAllValues();

		// FIXME подключение может проходить даже если пароль установлен
		for (int index = 0; index < inputs.length; index++) {
			assertEquals(expectedMessages[index].toString(), capturedArgs.get(index));
		}
	}

	/**
	 * Юзер ввел правильные данные но база данных не существует
	 * Он увидит сообщения
	 * Keys.DATABASE_DOESNT_EXISTS_ERROR
	 */
	public void testConnectWrongDatabase() {
		final String[] inputs = {
				"connect | " + TEST_DATABASE_NAME + "|" + username() + "|" +  password()
		};

		final String[] expectedMessages = {
				new StringBuilder().append(Keys.DATABASE_DOESNT_EXISTS_ERROR).toString()
		};

		for (String input : inputs) {
			controller.onUserInput(input);
		}

		Mockito.verify(viewMock, Mockito.times(inputs.length)).render(argument.capture());

		List<String> capturedArgs = argument.getAllValues();

		for (int index = 0; index < inputs.length; index++) {
			assertEquals(expectedMessages[index].toString(), capturedArgs.get(index));
		}
	}

	/**
	 * Юзер вводет все правильно
	 * На экран выводится сообщение
	 * Keys.CONNECTION_SET
	 */
	public void testConnectDatabase() {
		try {
			prepareDatabase();
		} catch (DataBaseRequestException e) {
			e.printStackTrace();
		}
		final String[] inputs = {
				"connect | " + TEST_DATABASE_NAME + "|" + username() + "|" +  password()
		};

		final String[] expectedMessages = {
				new StringBuilder().append(Keys.CONNECTION_SET).toString()
		};

		for (String input : inputs) {
			controller.onUserInput(input);
		}

		Mockito.verify(viewMock, Mockito.times(inputs.length)).render(argument.capture());

		List<String> capturedArgs = argument.getAllValues();

		for (int index = 0; index < inputs.length; index++) {
			assertEquals(expectedMessages[index].toString(), capturedArgs.get(index));
		}
	}

	/**
	 * Повторная попытка подклюения выведет сообщение
	 * Keys.CONNECTION_ALREADY_SET
	 * После закрытия содинения подключение происходит успешно
	 * Юзер увидит
	 * Keys.CONNECTION_SET
	 */
	public void testMulripleConnectAttempt() {
		try {
			prepareDatabase();
			setConnection();
		} catch (DataBaseRequestException e) {
			e.printStackTrace();
		}
		final String[] inputs = {
				"connect | " + TEST_DATABASE_NAME + "|" + username() + "|" +  password()
		};

		final String[] expectedMessages = {
				new StringBuilder().append(Keys.CONNECTION_ALREADY_SET).toString(),
				new StringBuilder().append(Keys.CONNECTION_SET).toString()
		};

		for (String input : inputs) {
			controller.onUserInput(input);
		}

		try {
			closeConnection();
		} catch (DataBaseRequestException e) {}

		for (String input : inputs) {
			controller.onUserInput(input);
		}

		Mockito.verify(viewMock, Mockito.times(2)).render(argument.capture());

		List<String> capturedArgs = argument.getAllValues();

		for (int index = 0; index < inputs.length; index++) {
			assertEquals(expectedMessages[index].toString(), capturedArgs.get(index));
		}
	}
}
