package sqlcmd.int_test;

import org.mockito.Mockito;
import sqlapp.Keys;
import sqlapp.model.exceptions.DataBaseRequestException;

import java.util.List;

public class SelectTableTest extends IntBase {
    // select | table_name

    /**
     * Если юзер вводит неправильный формат комнды, то он увидит сообщения:
     * Keys.INPUT_ERROR
     * Keys.COMMAND_ARGUMENT_ERROR
     * Keys.HELP_INFO_TEXT
     */
    public void testSelectWrongInput() {
        final String expectedMessage = new StringBuilder()
                .append(Keys.INPUT_ERROR)
                .append(Keys.COMMAND_ARGUMENT_ERROR)
                .append(Keys.HELP_INFO_TEXT)
                .toString();

        final String[] inputs = {
                "select",
                "select|"
        };

        final String[] expectedMessages = {
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
    public void testSelectConnectionDoesntSet() {
        final String[] inputs = {
                "select|any"
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
    public void testSelectTableDoesntExists() {
        try {
            prepareDatabase();
            setConnection();
        } catch (DataBaseRequestException e) {
        }
        final String[] inputs = {
                "select|" + TEST_TABLE
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
    public void testSelectTable() {
        try {
            prepairTable(new String[]{"col_1", "col_2"}, new  String[]{"col_1", "val_1", "col_2", "val_2hgfhfghgfhfghfghf"});
            setConnection();
        } catch (DataBaseRequestException e) {}

        final String[] inputs = {
                "select|" + TEST_TABLE
        };

        final String[] expectedMessages = {
                "\t+----------+----------+%n" +
                "\t|  col_1   |  col_2   |%n" +
                "\t+----------+----------+%n" +
                "\t|  val_1   |val_2hg...|%n" +
                "\t+----------+----------+%n"
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
