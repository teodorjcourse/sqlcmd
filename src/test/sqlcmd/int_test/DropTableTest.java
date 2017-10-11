package sqlcmd.int_test;

import org.mockito.Mockito;
import sqlapp.Keys;
import sqlapp.model.exceptions.DataBaseRequestException;

import java.util.List;

public class DropTableTest extends IntBase{
	// drop_tb | table_name

	/**
	 * Если юзер вводит неправильный формат комнды, то он увидит сообщения:
	 * Keys.INPUT_ERROR
	 * Keys.COMMAND_ARGUMENT_ERROR
	 * Keys.HELP_INFO_TEXT
	 */
	public void testDropTableWrongInput() {
		final String[] inputs = {
				"drop_tb|"
		};

		final String[] expectedMessages = {
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
	public void testDropConnectionDoesntSet() {
		final String[] inputs = {
				"drop_tb|any"
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
	 * Команда введена верно, таблица не существует
	 * Юзер увидит сообщения
	 * Keys.TABLE_DOESNT_EXIST_ERROR
	 */
	public void testDropTableDoesntSet() {
		try {
			prepareDatabase();
			setConnection();
		} catch (DataBaseRequestException e) {
		}
		final String[] inputs = {
				"drop_tb|" + TEST_TABLE
		};

		final String[] expectedMessages = {
				new StringBuilder().append(Keys.TABLE_DOESNT_EXIST_ERROR).toString()
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
	 * Команда введена верно, таблица существует
	 * Юзер увидит сообщение
	 * Keys.DROP_TABLE_SUCCESS
	 */
	public void testDropTable() {
		try {
			prepairEmptyTable();
			setConnection();
		} catch (DataBaseRequestException e) {}

		final String[] inputs = {
				"drop_tb|" + TEST_TABLE
		};

		final String[] expectedMessages = {
				new StringBuilder().append(Keys.DROP_TABLE_SUCCESS).toString()
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
