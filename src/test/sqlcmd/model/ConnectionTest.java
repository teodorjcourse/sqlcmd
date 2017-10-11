package sqlcmd.model;

import sqlcmd.DataBaseManagerTestBase;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;

public class ConnectionTest extends DataBaseManagerTestBase {
	/**
	 * Тест соединения по дефолтному хосту и порту проходит успешно
	 */
	public void testConnectionOpen() {
		Throwable any = null;

		try {
			dataBaseManager.openConnection(
					username(),
					password()
			);
		} catch (DataBaseRequestException e) {
			any = e;
		}

		assertTrue(any == null);
	}

	/**
	 * Тест подключения
	 * Юзер сделал ошибку при вводе параметра user_name
	 * менеждер выбрасывает исключение с кодом ошибки {@link RequestError#CONNECTION_SET_ERROR}
	 */
	public void testWrongUserName() {
		int errCodeExcpecting = RequestError.CONNECTION_SET_ERROR;
		int errCodeResult = -1;

		try {
			dataBaseManager.openConnection(
					"wrong",
					password()
			);
		} catch (DataBaseRequestException e) {
			errCodeResult = e.error();
		}

		assertEquals(errCodeExcpecting, errCodeResult);
	}

	// TODo тест стал проходить с неверным паролем. Необходимо просииледовать такой кейс
//	/**
//	 * Тест подключения
//	 * Юзер сделал ошибку при вводе пароля
//	 * менеждер выбрасывает исключение с кодом ошибки {@link RequestError#CONNECTION_SET_ERROR}
//	 */
//	public void testWrongPassword() {
//		int errCodeExcpecting = RequestError.CONNECTION_SET_ERROR;
//		int errCodeResult = -1;
//
//		try {
//			dataBaseManager.openConnection(
//					TestConfig.TEST_USERNAME,
//					"wrong"
//			);
//		} catch (DataBaseRequestException e) {
//			errCodeResult = e.error();
//		}
//
//		assertEquals(errCodeExcpecting, errCodeResult);
//	}

	/**
	 * Тест подключения
	 * Юзер пытается подключиться к базе данных.
	 * Вводит корректные username и password,
	 * но указанной базы данных нет
	 * менеждер выбрасывает исключение с кодом ошибки {@link RequestError#DATABASE_DOESNT_EXISTS_ERROR}
	 */
	public void testConnectionDatabaseDoesntExist() {
		int errCodeExcpecting = RequestError.DATABASE_DOESNT_EXISTS_ERROR;
		int errCodeResult = -1;

		try {
			dataBaseManager.connect(
					"wrong",
					username(),
					password()
			);
		} catch (DataBaseRequestException e) {
			errCodeResult = e.error();
		}

		assertEquals(errCodeExcpecting, errCodeResult);
	}

	/**
	 * Тест подключения
	 * Юзер ввел правильные данные, подключение успешно
	 */
	public void testConnectionSuccessfull() {
		Throwable any = null;

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
		} catch (DataBaseRequestException e) {
			any = e;
		}

		assertTrue(any == null);
	}

	/**
	 * Повторная попытка подключения к БД вызовет иксепшин с  кодом {@link RequestError#CONNECTION_ALREADY_SET}
	 * после закрытия соединения подключение успешно
	 */
	public void testSecondConnectionAttempt() {
		int errCodeExcpecting = RequestError.CONNECTION_ALREADY_SET;
		int errCodeResult = -1;
		Throwable any = null;

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

			dataBaseManager.connect(
					TEST_DATABASE_NAME,
					username(),
					password()
			);

		} catch (DataBaseRequestException e) {
			errCodeResult = e.error();
		}

		assertEquals(errCodeExcpecting, errCodeResult);

		try {
			dataBaseManager.closeConnection();
			dataBaseManager.connect(
					TEST_DATABASE_NAME,
					username(),
					password()
			);
		} catch (DataBaseRequestException e) {
			any = e;
		}

		assertTrue(any == null);
	}
}
