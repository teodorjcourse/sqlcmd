package sqlcmd.commands;

import junit.framework.TestCase;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import sqlapp.controller.commands.Description;
import sqlapp.controller.commands.HelpCommand;
import sqlapp.model.ResponseData;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.DataBaseRequestException;

import java.util.List;

public class HelpCommandTest extends TestCase {

	/**
	 * Тестим ожидаемые поведения при введенных корректных данных
	 */
	public void test() {
		final String EXPECTED_KEY_DESC = "key_desk";
		final String EXPECTED_SHORT_DESC = "short_desk";
		final String EXPECTED_FULL_DESC = "full_desk";

		final String[][] inputRequests = {
				{},
				{null},
				{"list"},
				{"cmd_key"},
				{"detailed_wanted_but_full_will_be"}
		};

		final String[] expectedBehaviors = {
				EXPECTED_FULL_DESC,
				EXPECTED_FULL_DESC,
				EXPECTED_SHORT_DESC,
				EXPECTED_KEY_DESC,
				EXPECTED_FULL_DESC
		};

		Throwable actualException = null;

		Description commandDescription = Mockito.mock(Description.class);
		ResponseModel responseModel = Mockito.mock(ResponseModel.class);

		ArgumentCaptor<ResponseData> argument = ArgumentCaptor.forClass(ResponseData.class);

		Mockito
				.when(commandDescription.descriptionFor(Mockito.anyString()))
				.thenAnswer(invocationOnMock -> {
					String string = invocationOnMock.getArgumentAt(0, String.class);
					return string != null && string.equals("cmd_key") ? EXPECTED_KEY_DESC : EXPECTED_FULL_DESC ;
				});

		Mockito
				.when(commandDescription.shortDescription())
				.thenReturn(EXPECTED_SHORT_DESC);

		Mockito
				.when(commandDescription.fullDescription())
				.thenReturn(EXPECTED_FULL_DESC);


		HelpCommand test = new HelpCommand(commandDescription, responseModel);

		for (String[] requestArgs : inputRequests) {
			try {
				test.execute(requestArgs);
			} catch (DataBaseRequestException e) {
				actualException = e;
			}
		}

		Mockito.verify(responseModel, Mockito.times(inputRequests.length)).push(argument.capture());

		List<ResponseData> capturedArgs = argument.getAllValues();
		for (int index = 0; index < capturedArgs.size(); index++) {
			assertEquals(expectedBehaviors[index], capturedArgs.get(index).data());
		}

		assertNull(actualException);
	}
}
