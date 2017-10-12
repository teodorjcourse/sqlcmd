package sqlcmd.int_test;

import org.mockito.Mockito;
import sqlapp.Keys;
import sqlapp.model.exceptions.DataBaseRequestException;
import java.util.List;

public class GetTablesTest extends IntBase {

	/**
	 * Команда введена верно, но соединение не установлено
	 * Юзер увидит сообщения
	 * Keys.CONNECTION_ERROR
	 * Keys.HELP_INFO_TEXT
	 */
	public void testGetTablesConnectionDoesntSet() {
		final String[] inputs = {
				"tables"
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
	 * Соединение установлено
	 * Получаем таблицы
	 */
	public void testGetTables() {
		try {
			prepairEmptyTable();
			setConnection();
		} catch (DataBaseRequestException e) {

		}

		final String[] inputs = {
				"tables"
		};

		final String[] expectedMessages = {
				"\t+----------+%n\t|   foo    |%n\t+----------+%n"
		};

		for (String input : inputs) {
			controller.onUserInput(input);
		}

		Mockito.verify(viewMock, Mockito.times(inputs.length)).render(argument.capture());

		List<String> capturedArgs = argument.getAllValues();

		for (int index = 0; index < inputs.length; index++) {
			assertEquals(String.format(expectedMessages[index]), capturedArgs.get(index));
		}
	}

	/**
	 * Соединение установлено
	 * Таблиц нет. Юзеру выводим сообщение, что таблиц нет
	 */
	public void testGetTablesDoesntExists() {
		try {
			prepareDatabase();
			setConnection();
		} catch (DataBaseRequestException e) {

		}

		final String[] inputs = {
				"tables"
		};

		final String[] expectedMessages = {
                Keys.EMPTY_TABLE_TEXT.toString()
		};

		for (String input : inputs) {
			controller.onUserInput(input);
		}

		Mockito.verify(viewMock, Mockito.times(inputs.length)).render(argument.capture());

		List<String> capturedArgs = argument.getAllValues();

		for (int index = 0; index < inputs.length; index++) {
			assertEquals(String.format(expectedMessages[index]), capturedArgs.get(index));
		}
	}
}
