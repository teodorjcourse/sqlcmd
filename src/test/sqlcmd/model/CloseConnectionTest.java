package sqlcmd.model;

import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;
import sqlcmd.DataBaseManagerTestBase;

public class CloseConnectionTest extends DataBaseManagerTestBase {
	/**
	 * Закрытие соединения отрабатывает успешно даже в том случае, если оно не открывалось
	 */
	public void testCloseConnection() {
		Throwable any = null;
		try {

			dataBaseManager.closeConnection();

			dataBaseManager.openConnection(
					username(),
					password()
			);

			dataBaseManager.closeConnection();

			try {
				dataBaseManager.createDataBase(
						TEST_DATABASE_NAME
				);
			} catch (DataBaseRequestException e) {
				assertEquals(RequestError.CONNECTION_DOESNT_SET, e.error());
			}

		} catch (DataBaseRequestException e) {
			any = e;
		}

		assertTrue(any == null);
	}
}
