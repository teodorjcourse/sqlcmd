package sqlcmd.int_test;

import org.mockito.Mockito;
import sqlapp.Keys;
import sqlapp.model.exceptions.DataBaseRequestException;

import java.util.List;

public class CreateTableTest extends IntBase {
	// create_tb | table_name | <column1> | <column2> | <...> | <columnN>

	/**
	 * Если юзер вводит неправильный формат комнды, то он увидит сообщения:
	 * Keys.INPUT_ERROR
	 * Keys.COMMAND_ARGUMENT_ERROR
	 * Keys.HELP_INFO_TEXT
	 */
	public void testCreateTableWrongInput() {
		final String[] inputs = {
				"create_tb|"
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
	public void testCreateConnectionDoesntSet() {
		final String[] inputs = {
				"create_tb|any"
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
	 * Команда введена верно, таблица уже существует
	 * Юзер увидит сообщения
	 * Keys.TABLE_ALREADY_EXISTS_ERROR
	 */
	public void testCreateTableAlreadyExists() {
		try {
			prepairEmptyTable();
			setConnection();
		} catch (DataBaseRequestException e) {
		}
		final String[] inputs = {
				"create_tb|" + TEST_TABLE
		};

		final String[] expectedMessages = {
				new StringBuilder().append(Keys.TABLE_ALREADY_EXISTS_ERROR).toString()
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
	 * Юзер не ввел колонки для таблицы, создается пустая
	 * Keys.TABLE_ALREADY_EXISTS_ERROR
	 */
	public void testCreateEmptyTable() {
		try {
			prepareDatabase();
			setConnection();
		} catch (DataBaseRequestException e) {}

		final String[] inputs = {
				"create_tb|" + TEST_TABLE
		};

		final String[] expectedMessages = {
				new StringBuilder().append(Keys.CREATE_TABLE_SUCCESS).toString()
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
	 * Юзер захотел создать колонки, но допукает ошибки при вводе
	 * Keys.SYNTAX_ERROR
	 * Keys.HELP_INFO_TEXT
	 */
	public void testCreateTableFail() {
		try {
			prepareDatabase();
			setConnection();
		} catch (DataBaseRequestException e) {}

		final String[] inputs = {
//				"create_table|" + TEST_TABLE + "||",
				"create_tb|" + TEST_TABLE + "||col_2",
				"create_tb|" + TEST_TABLE + "|    |col_2"
		};

		final String[] expectedMessages = {
				new StringBuilder().append(Keys.SYNTAX_ERROR).append(Keys.HELP_INFO_TEXT).toString(),
				new StringBuilder().append(Keys.SYNTAX_ERROR).append(Keys.HELP_INFO_TEXT).toString()
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
