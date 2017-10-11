package sqlcmd.int_test;

import org.mockito.Mockito;
import sqlapp.Keys;
import sqlapp.model.exceptions.DataBaseRequestException;

import java.util.List;

public class OpenConnectionTest extends IntBase {
    //open | username | password

    /**
     * Если юзер вводит неправильный формат комнды, то он увидит сообщения:
     * Keys.INPUT_ERROR
     * Keys.COMMAND_ARGUMENT_ERROR
     * Keys.HELP_INFO_TEXT
     */
    public void testOpenConnectionWrongInput() {
        final String[] inputs = {
                "open",
                "open |",
                "open | username",
                "open | username|"
        };

        final String[] expectedMessages = {
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
     * Юзер допускает ошибку в поле username или password
     * Keys.CONNECTION_SET_ERROR
     * Keys.HELP_INFO_TEXT
     */
    public void testOpenConnectionWrongAuthData() {
        final String[] inputs = {
//                "open |" + username() + "|wrong",
                "open |wrong|" + password()
        };

        // TODO каким то образом удается подключиться к БД без пароля. Хотя пароль установлен
        final String[] expectedMessages = {
//                new StringBuilder().append(Keys.CONNECTION_SET_ERROR).append(Keys.HELP_INFO_TEXT).toString(),
                new StringBuilder().append(Keys.CONNECTION_SET_ERROR).append(Keys.HELP_INFO_TEXT).toString()
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
    public void testOpenConnection() {
        final String[] inputs = {
                "open |" + username() + "|" + password()
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
        final String[] inputs = {
                "open |" + username() + "|" + password(),
                "open |" + username() + "|" + password(),
                "open |" + username() + "|" + password()
        };

        final String[] expectedMessages = {
                new StringBuilder().append(Keys.CONNECTION_SET).toString(),
                new StringBuilder().append(Keys.CONNECTION_ALREADY_SET).toString(),
                new StringBuilder().append(Keys.CONNECTION_SET).toString()
        };

        int index = 0;
        for (String input : inputs) {
            controller.onUserInput(input);

            if (index++ == 1) {
                try {
                    closeConnection();
                } catch (DataBaseRequestException e) {}
            }
        }

        Mockito.verify(viewMock, Mockito.times(inputs.length)).render(argument.capture());

        List<String> capturedArgs = argument.getAllValues();

        for (index = 0; index < inputs.length; index++) {
            assertEquals(expectedMessages[index].toString(), capturedArgs.get(index));
        }
    }
}
