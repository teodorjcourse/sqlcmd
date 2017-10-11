package sqlcmd.model;

import sqlcmd.DataBaseManagerTestBase;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;

import java.util.ArrayList;

public class DeleteRowsTest extends DataBaseManagerTestBase {

	/**
	 * Попытка отправить запрос на удаление строк из таблицы при отсутствующем соединении
	 * вызовет ошибку с кодом {@link RequestError#CONNECTION_DOESNT_SET}
	 */
	public void testConnectionDoesntSet() {
		final String TABLE_NAME = "foo";
		short errCodeExpectin = RequestError.CONNECTION_DOESNT_SET;
		short errCode = -1;

		try {
			dataBaseManager.deleteRows(
					TABLE_NAME,
					new String[]{}
			);
		} catch (DataBaseRequestException e) {
			errCode = e.error();
		}

		assertEquals(errCodeExpectin, errCode);
	}

	/**
	 * Попытка отправить запрос на удаление строк из несуществующей таблицы
	 * вызовет ошибку с кодом {@link RequestError#TABLE_DOESNT_EXISTS_ERROR}
	 */
	public void testDeleteTablesDoesntExists() {
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

			dataBaseManager.createTable(
					TABLE_NAME,
					new String[]{}
			);

			dataBaseManager.deleteRows(
					"wrong",
					new String[]{"col_1", "val_1"}
			);
		} catch (DataBaseRequestException e) {
			errCode = e.error();
		}

		assertEquals(errCodeExpectin, errCode);
	}

	/**
	 * Удаление происходит успешно
	 */
	public void testDeleteRows() {
		final String TABLE_NAME = "foo";
		final String[] startRowValues = {"col_1", "col_2", "a", "b", "c", "d", "e", "f", null, "g"};
		final String[] eowValuesExpected = {"col_1", "col_2", "e", "f"};
		final int expectDeleteCount = 3;
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
					new String[]{"col_1", "col_2"}
			);

			dataBaseManager.insertRow(
					TABLE_NAME,
					new String[]{"col_1", "a", "col_2", "b"}
			);

			dataBaseManager.insertRow(
					TABLE_NAME,
					new String[]{"col_1", "c", "col_2", "d"}
			);

			dataBaseManager.insertRow(
					TABLE_NAME,
					new String[]{"col_1", "e", "col_2", "f"}
			);

			dataBaseManager.insertRow(
					TABLE_NAME,
					new String[]{"col_1", null, "col_2", "g"}
			);

			ArrayList<ArrayList<String>> result = dataBaseManager.selectTable(
					TABLE_NAME
			);

			int orderIndex = 0;

			for (ArrayList<String> rows : result) {
				for (String colValue : rows) {
					assertEquals(startRowValues[orderIndex++], colValue);
				}
			}

			int deleted = dataBaseManager.deleteRows(
					TABLE_NAME,
					new String[]{"col_1", "a", "col_2", "x", "col_2", "d", "col_1", null}
			);

			assertEquals(deleted, expectDeleteCount);

			result = dataBaseManager.selectTable(
					TABLE_NAME
			);

			orderIndex = 0;

			for (ArrayList<String> rows : result) {
				for (String colValue : rows) {
					assertEquals(eowValuesExpected[orderIndex++], colValue);
				}
			}
		} catch (Throwable e) {
			any = e;
		}

		assertTrue(any == null);
	}
}
