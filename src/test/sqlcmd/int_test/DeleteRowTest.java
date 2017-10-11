package sqlcmd.int_test;

import org.mockito.Mockito;
import sqlapp.Keys;
import sqlapp.model.exceptions.DataBaseRequestException;

import java.util.List;

public class DeleteRowTest extends IntBase {
	// delete | tableName | column | value

	/**
	 * Если юзер вводит неправильный формат комнды, то он увидит сообщения:
	 * Keys.INPUT_ERROR
	 * Keys.COMMAND_ARGUMENT_ERROR
	 * Keys.HELP_INFO_TEXT
	 */
	public void testDeleteRowWrongInput() {
		final String[] inputs = {
				"delete",
				"delete |",
				"delete | tableName",
				"delete | tableName|",
				"delete | tableName | column",
				"delete | tableName | column |"
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
	 * Команда введена верно, но соединение не установлено
	 * Юзер увидит сообщения
	 * Keys.CONNECTION_ERROR
	 * Keys.HELP_INFO_TEXT
	 */
	public void testDeleteRowConnectionDoesntSet() {
		final String[] inputs = {
				"delete|any|any|any"
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
	public void testDeleteRowTableDoesntExists() {
		try {
			prepareDatabase();
			setConnection();
		} catch (DataBaseRequestException e) {
		}
		final String[] inputs = {
				"delete|wrong|col_1|val_1"
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
	 * Keys.DELETE_ROW_SUCCESS, если есть совпадения
	 * Keys.DELETE_ROW_SUCCESS_ZERO, если совпадений нет
	 */
	public void testDeleteRow() {
		try {
			prepairTable(new String[]{"col_1"}, new String[]{"col_1", "val_2"});
			setConnection();
		} catch (DataBaseRequestException e) {}

		final String[] inputs = {
				"delete|" + TEST_TABLE + "|col_1|val_1",
				"delete|" + TEST_TABLE + "|col_1|val_2"
		};

		final String[] expectedMessages = {
				new StringBuilder().append(Keys.DELETE_ROW_SUCCESS_ZERO).toString(),
				new StringBuilder().append(Keys.DELETE_ROW_SUCCESS).toString()
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
