package sqlcmd.int_test;

import org.mockito.Mockito;
import sqlapp.Keys;
import sqlapp.model.exceptions.DataBaseRequestException;

import java.util.List;

public class InsertRowTest extends IntBase {
    // insert | table_name | column1 | <value1> | <column2> | <value2> | <...> | <columnN> | <valueN>

    /**
     * Если юзер вводит неправильный формат комнды, то он увидит сообщения:
     * Keys.INPUT_ERROR
     * Keys.COMMAND_ARGUMENT_ERROR
     * Keys.HELP_INFO_TEXT
     */
    public void testInsertRowWrongInput() {
        final String expectedMessage = new StringBuilder()
                .append(Keys.INPUT_ERROR)
                .append(Keys.COMMAND_ARGUMENT_ERROR)
                .append(Keys.HELP_INFO_TEXT)
                .toString();

        final String[] inputs = {
                "insert",
                "insert|",
                "insert|table_name",
                "insert|table_name|"
        };

        final String[] expectedMessages = {
                expectedMessage,
                expectedMessage,
                expectedMessage,
                expectedMessage
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
    public void testInsertRowonnectionDoesntSet() {
        final String[] inputs = {
                "insert|table_name|any"
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
    public void testInsertRowTableDoesntSet() {
        try {
            prepareDatabase();
            setConnection();
        } catch (DataBaseRequestException e) {
        }
        final String[] inputs = {
                "insert |" + TEST_TABLE + "| column1 | some"
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
     * Keys.CLEAR_TABLE_SUCCESS
     */
    public void testInsertRowTable() {
        try {
            prepairTable(new String[]{"col_1"}, new String[]{});
            setConnection();
        } catch (DataBaseRequestException e) {}

        final String[] inputs = {
                "insert |" + TEST_TABLE + "| col_1 | some"
        };

        final String[] expectedMessages = {
                new StringBuilder().append(Keys.INSERT_TABLE_SUCCESS).toString()
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
