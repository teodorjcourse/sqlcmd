package sqlcmd.int_test;
import org.mockito.Mockito;
import sqlapp.Keys;

import java.util.List;

public class IncorrectInputsTest extends IntBase {

	public void testWrongCommandINput() {
		StringBuilder expectedMessage = new StringBuilder();
		expectedMessage.append(Keys.WRONG_COMMAND_ERROR);
		expectedMessage.append(Keys.HELP_INFO_TEXT);

		controller.onUserInput("");
		controller.onUserInput("dawdadwa");
		controller.onUserInput(null);

		Mockito.verify(viewMock, Mockito.times(3)).render(argument.capture());

		List<String> capturedArgs = argument.getAllValues();

		for (String message : capturedArgs) {
			assertEquals(expectedMessage.toString(), message);
		}
	}

}
