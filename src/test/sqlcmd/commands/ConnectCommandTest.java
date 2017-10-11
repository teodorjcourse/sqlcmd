package sqlcmd.commands;

import org.mockito.Mockito;
import sqlapp.controller.commands.ConnectCommand;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;
import sqlcmd.DataBaseManagerTestBase;

public class ConnectCommandTest extends DataBaseManagerTestBase {
	private ResponseModel mock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mock = Mockito.mock(ResponseModel.class);

		dataBaseManager.openConnection(
				username(),
				password()
		);

		dataBaseManager.createDataBase(
				TEST_DATABASE_NAME
		);

		dataBaseManager.closeConnection();
	}

	/**
	 * Выбрасывается исключения если юзер не ввел обязательные парамтеры команды
	 * Исключение не выбрасывается, если ввел все правильно
	 */
	public void test() {
		final String[][] inputs = {
				{},
				{TEST_DATABASE_NAME},
				{TEST_DATABASE_NAME, username()}
		};

		final String[] correctInput = {TEST_DATABASE_NAME, username(), password()};

		ConnectCommand cmd = new ConnectCommand(dataBaseManager, mock);

		short errCodeExpected = RequestError.CMD_ARG_ERROR;
		short errCodeActual = -1;

		for (String[] inputCase : inputs) {
			try {
				cmd.execute(inputCase);
			} catch (DataBaseRequestException e) {
				errCodeActual = e.error();
			}

			assertEquals(errCodeExpected, errCodeActual);
		}

		Throwable any = null;
		try {
			cmd.execute(correctInput);
		} catch (DataBaseRequestException e) {
			any = e;
		}

		assertTrue(any == null);
	}
}
