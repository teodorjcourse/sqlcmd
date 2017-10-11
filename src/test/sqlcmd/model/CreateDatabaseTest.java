package sqlcmd.model;

import sqlcmd.DataBaseManagerTestBase;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;

public class CreateDatabaseTest extends DataBaseManagerTestBase {

	/**
	 * Содеинениене не установлено
	 * Попытка создать базу данных вызывает исключение с кодом ошибки {@link RequestError#CONNECTION_DOESNT_SET}
	 */
	public void testFailConnectionDoesntSet() {
		int errCodeExcpecting = RequestError.CONNECTION_DOESNT_SET;
		int errCodeResult = -1;
		try {
			dataBaseManager.createDataBase(
					TEST_DATABASE_NAME
			);
		} catch (DataBaseRequestException e) {
			errCodeResult = e.error();
		}

		assertEquals(errCodeExcpecting, errCodeResult);
	}

	/**
	 * Соединение установлено
	 * Попытка создать уже существующую базу данных
	 * вызывает исключение с кодом ошибки {@link RequestError#DATABASE_ALREADY_EXISTS_ERROR}
	 */
	public void testDatabaseAlreadyExists() {
		int errCodeExcpecting = RequestError.DATABASE_ALREADY_EXISTS_ERROR;
		int errCodeResult = -1;

		try {
			dataBaseManager.openConnection(
					username(),
					password()
			);

			try {
				dataBaseManager.dropDataBase(
						TEST_DATABASE_NAME
				);
			} catch (DataBaseRequestException e) { }

			dataBaseManager.createDataBase(
					TEST_DATABASE_NAME
			);

			dataBaseManager.createDataBase(
					TEST_DATABASE_NAME
			);

		} catch (DataBaseRequestException e) {
			errCodeResult = e.error();
		}

		assertEquals(errCodeExcpecting, errCodeResult);
	}

}
