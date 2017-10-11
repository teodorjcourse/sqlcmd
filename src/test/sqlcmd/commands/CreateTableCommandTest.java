package sqlcmd.commands;
import org.mockito.Mockito;
import sqlapp.controller.commands.CreateTableCommand;
import sqlapp.model.ResponseModel;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;
import sqlapp.model.managers.DataBaseManager;
import sqlcmd.DataBaseManagerTestBase;

public class CreateTableCommandTest extends DataBaseManagerTestBase {
	private ResponseModel mock;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mock = Mockito.mock(ResponseModel.class);
		dataBaseManager = Mockito.mock(DataBaseManager.class);
	}

	/**
	 * Выбрасывается исключения если юзер не ввел обязательные парамтеры команды
	 * Исключение невыбрасывается, если ввел все правильно
	 */
	public void test() {
		final String[][] inputs = {
				{},
				{""},
				{"   "}
		};

		final short errCodeExpected = RequestError.CMD_ARG_ERROR;

		CreateTableCommand cmd = new CreateTableCommand(dataBaseManager, mock);


		for (String[] inputCase : inputs) {
			short errCodeActual = -1;

			try {
				cmd.execute(inputCase);
			} catch (DataBaseRequestException e) {
				errCodeActual = e.error();
			}

			assertEquals(errCodeExpected, errCodeActual);
		}
	}
}
