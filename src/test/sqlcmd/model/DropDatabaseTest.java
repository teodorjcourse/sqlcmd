package sqlcmd.model;

import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;
import sqlcmd.DataBaseManagerTestBase;

public class DropDatabaseTest extends DataBaseManagerTestBase {

	/**
	 * Попытка дронуть базу данных вызовет иксепшин с кодом {@link RequestError#CONNECTION_DOESNT_SET}
	 * если соединение не установлено
	 */
	public void testDropDatabaseDoesntSet() {
		short expectedErrCode = RequestError.CONNECTION_DOESNT_SET;
		short errCode = -1;

		try {
			dataBaseManager.dropDataBase(
					TEST_DATABASE_NAME
			);
		} catch (DataBaseRequestException e) {
			errCode = e.error();
		}

		assertEquals(expectedErrCode, errCode);
	}

	/**
	 * Юзер пытается дропнуть базу, которой не существует
	 * менеджер выбросит исключение с ошибкой {@link RequestError#DATABASE_DOESNT_EXISTS_ERROR}
	 */
	public void testDropDatabaseWrongUsername() {
		short expectedErrCode = RequestError.DATABASE_DOESNT_EXISTS_ERROR;
		short errCode = -1;

		try {
			dataBaseManager.openConnection(
					username(),
					password()
			);

			dataBaseManager.dropDataBase(
					"wrong"
			);

		} catch (DataBaseRequestException e) {
			errCode = e.error();
		}

		assertEquals(expectedErrCode, errCode);
	}

	/**
	 * Юзер пытается дропнуть базу, к которой в данный момент подключен
	 * менеджер выбросит исключение с ошибкой {@link RequestError#DATABASE_IN_USE_DROP_ERROR}
	 */
	public void testDropActiveDatabase() {
		short expectedErrCode = RequestError.DATABASE_IN_USE_DROP_ERROR;
		short errCode = -1;

		try {
			dataBaseManager.openConnection(
					username(),
					password()
			);

			dataBaseManager.createDataBase(
					TEST_DATABASE_NAME
			);

			dataBaseManager.closeConnection();

			dataBaseManager.connect(
					TEST_DATABASE_NAME,
					username(),
					password()
			);

			dataBaseManager.dropDataBase(
					TEST_DATABASE_NAME
			);

		} catch (DataBaseRequestException e) {
			errCode = e.error();
		}

		assertEquals(expectedErrCode, errCode);
	}

	/**
	 * База дропается без ошибок, после удаления к ней нельзя подключиться
	 */
	public void testDropDatabase() {
		short expectedErrCode = RequestError.DATABASE_DOESNT_EXISTS_ERROR;
		short errCode = -1;

		try {
			dataBaseManager.openConnection(
					username(),
					password()
			);

			dataBaseManager.createDataBase(
					TEST_DATABASE_NAME
			);

			dataBaseManager.dropDataBase(
					TEST_DATABASE_NAME
			);

			dataBaseManager.closeConnection();

			dataBaseManager.connect(
					TEST_DATABASE_NAME,
					username(),
					password()
			);

		} catch (DataBaseRequestException e) {
			errCode = e.error();
		}

		assertEquals(expectedErrCode, errCode);
	}

}
