package sqlcmd.model;

import sqlcmd.DataBaseManagerTestBase;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;

import java.util.ArrayList;

public class InsertRowTest extends DataBaseManagerTestBase {

	/**
	 * Попытка отправить заспрос вставки строки в  таблицу, при неустановленном соединении
	 * вызовет ошибку с кодом {@link RequestError#CONNECTION_DOESNT_SET}
	 */
	public void testConnectionDoesntSet() {
		short errCodeExpectin = RequestError.CONNECTION_DOESNT_SET;
		short errCode = -1;

		try {
			dataBaseManager.insertRow(
					TEST_DATABASE_NAME,
					new String[]{}
			);
		} catch (DataBaseRequestException e) {
			errCode = e.error();
		}

		assertEquals(errCodeExpectin, errCode);
	}

	/**
	 * Пытаемся вставить строку в несуществующую таблицу
	 * Менеджер выбрасывает исключение {@link RequestError#TABLE_DOESNT_EXISTS_ERROR}
	 */
	public void testInsertTableDoesntExist() {
		final String TABLE_NAME = "foo";
		short errCodeExpectin = RequestError.TABLE_DOESNT_EXISTS_ERROR;
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

			dataBaseManager.insertRow(
					TABLE_NAME,
					new String[]{"col_1"}
			);
		} catch (DataBaseRequestException e) {
			errCode = e.error();
		}

		assertEquals(errCodeExpectin, errCode);
	}

	/**
	 * Таблица существует, колонка сущестует, вставка просиходит успешно
	 */
	public void testInsert() {
		final String TABLE_NAME = "foo";
		final String[] expectedOrder = {"col_1", null};
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

			dataBaseManager.createTable(
					TABLE_NAME,
					new String[]{"col_1"}
			);

			dataBaseManager.insertRow(
					TABLE_NAME,
					new String[]{"col_1"}
			);

			ArrayList<ArrayList<String>> result = dataBaseManager.selectTable(
					TABLE_NAME
			);

			int orderIndex = 0;
			for (ArrayList<String> rows : result) {
				for (String colValue : rows) {
					assertEquals(expectedOrder[orderIndex++], colValue);
				}
			}


		} catch (Throwable e) {
			any = null;
		}

		assertTrue(any == null);
	}

	/**
	 * Таблица существует, колонка отсутствет, вставка просиходит с ошибкой {@link RequestError#REQUEST_ERROR}
	 */
	public void testInsertWrongColumnName() {
		final String TABLE_NAME = "foo";
		short errCodeExpectin = RequestError.REQUEST_ERROR;
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

			dataBaseManager.createTable(
					TABLE_NAME,
					new String[]{"col_1"}
			);

			dataBaseManager.insertRow(
					TABLE_NAME,
					new String[]{"col_2"}
			);

		} catch (DataBaseRequestException e) {
			errCode = e.error();
		}

		assertEquals(errCodeExpectin, errCode);
	}
}
