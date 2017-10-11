package sqlcmd.int_test;

import org.mockito.Mockito;
import sqlapp.Keys;
import sqlapp.model.exceptions.DataBaseRequestException;

import java.util.List;

public class UpdateTableTest extends IntBase {
    // update | table_name | column | value | column1 | value1 | <...> | <columnN> | <valueN>

    /**
     * Если юзер вводит неправильный формат комнды, то он увидит сообщения:
     * Keys.INPUT_ERROR
     * Keys.COMMAND_ARGUMENT_ERROR
     * Keys.HELP_INFO_TEXT
     */
    public void testUpdateTableWrongInput() {
        final String[] inputs = {
                "update|",
                "update|table_name",
                "update|table_name|column",
                "update|table_name|column|val",
                "update|table_name|column|val|col_1"
        };

        final String[] expectedMessages = {
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
    public void testUpdateConnectionDoesntSet() {
        final String[] inputs = {
                "update|table_name|target_column|target_val|col_n|update"
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
    public void testUpdateTableDoesntExists() {
        try {
            prepareDatabase();
            setConnection();
        } catch (DataBaseRequestException e) {
        }
        final String[] inputs = {
                "update|" + TEST_TABLE + "|target_column|target_val|col_n|update"
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
     * Данные таблицы успешно обновляются
     * Keys.CLEAR_TABLE_SUCCESS
     */
    public void testUpdateTable() {
        try {
            prepairTable(new String[]{"col_1", "col_2"}, new  String[]{"col_1", "val_1", "col_2", "val_2hgfhfghgfhfghfghf"});
            setConnection();
        } catch (DataBaseRequestException e) {}

        final String[] inputs = {
                "select|" + TEST_TABLE,
                "update|" + TEST_TABLE + "|col_1|val_1|col_2|new",
                "update|" + TEST_TABLE + "|col_1|val_2|col_2|val_2hgfhfghgfhfghfghf",
                "select|" + TEST_TABLE
        };

        final String[] expectedMessages = {
                "\t+----------+----------+%n" +
                        "\t|  col_1   |  col_2   |%n" +
                        "\t+----------+----------+%n" +
                        "\t|  val_1   |val_2hg...|%n" +
                        "\t+----------+----------+%n",
                new StringBuilder().append(Keys.UPDATE_ROW_SUCCESS).toString(),
                new StringBuilder().append(Keys.UPDATE_ROW_SUCCESS_ZERO).toString(),
                "\t+----------+----------+%n" +
                        "\t|  col_1   |  col_2   |%n" +
                        "\t+----------+----------+%n" +
                        "\t|  val_1   |   new    |%n" +
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
