package sqlcmd.int_test;

import org.mockito.Mockito;
import sqlapp.AppConfig;
import sqlapp.Keys;
import sqlapp.Locale;

import java.util.List;

public class ChangeLanguageIntTest extends IntBase {
    //language | locale

    /**
     * Если юзер вводит неправильный формат комнды, то он увидит сообщения:
     * Keys.INPUT_ERROR
     * Keys.COMMAND_ARGUMENT_ERROR
     * Keys.HELP_INFO_TEXT
     */
    public void testLanguageChangeWrongInput() {
        final String[] inputs = {
                "language"
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
    * Команда введена верно, но язык не поддерживается
    */
    public void testLanguageChangeError() {
        final String[] inputs = {
                "language|sp"
        };

        final String[] expectedMessages = {
                new StringBuilder().append(Keys.LANGUAGE_CHANGED_ERROR).toString()
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
    * Команда введена верно, но язык не поддерживается
    */
    public void testLanguage() {
        final String[] inputs = {
                "language|en"
        };

        final String[] expectedMessages = {
                new StringBuilder().append(Keys.LANGUAGE_CHANGED_SUCCESS).toString()
        };

        AppConfig.translationBase.setLocale(Locale.RU);
        assertEquals(Locale.RU, AppConfig.translationBase.locale());

        for (String input : inputs) {
            controller.onUserInput(input);
        }

        Mockito.verify(viewMock, Mockito.times(inputs.length)).render(argument.capture());

        List<String> capturedArgs = argument.getAllValues();

        for (int index = 0; index < inputs.length; index++) {
            assertEquals(expectedMessages[index].toString(), capturedArgs.get(index));
        }

        assertEquals(Locale.EN, AppConfig.translationBase.locale());

    }

}
