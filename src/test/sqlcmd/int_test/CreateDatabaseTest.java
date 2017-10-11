package sqlcmd.int_test;

import org.mockito.Mockito;
import sqlapp.Keys;
import sqlapp.model.exceptions.DataBaseRequestException;

import java.util.List;

public class CreateDatabaseTest extends IntBase {

	/**
	 * Если юзер вводит неправильный формат комнды, то он увидит сообщения:
	 * Keys.INPUT_ERROR
	 * Keys.COMMAND_ARGUMENT_ERROR
	 * Keys.HELP_INFO_TEXT
	 */
	public void testCreateDatabaseWrongInput() {
		final String[] inputs = {
				"create_db |",
				"create_db    |   ",
				"create_db|  |"
		};

		final String[] expectedMessages = {
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
	 * Команда введена верно, но соединение не установлено
	 * Юзер увидит сообщения
	 * Keys.CONNECTION_ERROR
	 * Keys.HELP_INFO_TEXT
	 */
	public void testCreateConnectionDoesntSet() {
		final String[] inputs = {
				"create_db|" + TEST_DATABASE_NAME
		};

		final String[] expectedMessages = {
				new StringBuilder().append(Keys.CONNECTION_ERROR).append(Keys.HELP_INFO_TEXT).toString()
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
	 * Команда введена верно, соединение установлено, но база данных существует
	 * Юзер увидит сообщения
	 * Keys.DATABASE_ALREADY_EXISTS_ERROR
	 */
	public void testCreateDatabaseAlreadyExists() {
		try {
			prepareDatabase();
			openConnection();
		} catch (DataBaseRequestException e) {}

		final String[] inputs = {
				"create_db|" + TEST_DATABASE_NAME
		};

		final String[] expectedMessages = {
				new StringBuilder().append(Keys.DATABASE_ALREADY_EXISTS_ERROR).toString()
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
	 * Команда введена верно, соединение установлено, база создается успешно
	 * Юзер видит сообщение
	 * Keys.DATABASE_CREATE_SUCCESS
	 */
	public void testCreateDatabase() {
		try {
			openConnection();
		} catch (DataBaseRequestException e) {}

		final String[] inputs = {
				"create_db|" + TEST_DATABASE_NAME
		};

		final String[] expectedMessages = {
				new StringBuilder().append(Keys.DATABASE_CREATE_SUCCESS).toString()
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
}
