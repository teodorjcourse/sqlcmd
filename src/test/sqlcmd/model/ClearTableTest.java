package sqlcmd.model;

import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;
import sqlcmd.DataBaseManagerTestBase;

import java.util.ArrayList;

public class ClearTableTest extends DataBaseManagerTestBase {

	/**
	 * Попытка отправить заспрос очистки таблицы, при неустановленном соединении
	 * вызовет ошибку с кодом {@link RequestError#CONNECTION_DOESNT_SET}
	 */
	public void testConnectionDoesntSet() {
		short errCodeExpectin = RequestError.CONNECTION_DOESNT_SET;
		short errCode = -1;

		try {
			dataBaseManager.clearTable(
					TEST_DATABASE_NAME
			);
		} catch (DataBaseRequestException e) {
			errCode = e.error();
		}

		assertEquals(errCodeExpectin, errCode);
	}

	/**
	 * Попытка отправить заспрос очистки несуществующей таблицы
	 * вызовет ошибку с кодом {@link RequestError#TABLE_DOESNT_EXISTS_ERROR}
	 */
	public void testClearTableDoesntExists() {
		short errCodeExpectin = RequestError.TABLE_DOESNT_EXISTS_ERROR;
		short errCode = -1;

		try {
			dataBaseManager.openConnection(
					username(),
					password()
			);

			dataBaseManager.clearTable(
					TEST_DATABASE_NAME
			);
		} catch (DataBaseRequestException e) {
			errCode = e.error();
		}

		assertEquals(errCodeExpectin, errCode);
	}

	/**
	 * Очистка таблицы проходит спещно
	 */
	public void testClearTable() {
		final String TABLE_NAME = "foo";
		final String[] rowValuesOrder = {"col_1", "col_2", "col_3", "val_1", "val_2", null};
		final String[] rowValuesExpected = {"col_1", "col_2", "col_3"};
		Throwable any = null;
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
					new String[]{"col_1", "col_2", "col_3"}
			);

			dataBaseManager.insertRow(
					TABLE_NAME,
					new String[]{"col_1", "val_1", "col_2", "val_2"}
			);

			ArrayList<ArrayList<String>> result = dataBaseManager.selectTable(
					TABLE_NAME
			);

			int orderIndex = 0;
			for (ArrayList<String> rows : result) {
				for (String colValue : rows) {
					assertEquals(rowValuesOrder[orderIndex++], colValue);
				}
			}

			dataBaseManager.clearTable(TABLE_NAME);

			result = dataBaseManager.selectTable(
					TABLE_NAME
			);

			orderIndex = 0;
			for (ArrayList<String> rows : result) {
				for (String colValue : rows) {
					assertEquals(rowValuesExpected[orderIndex++], colValue);
				}
			}

		} catch (DataBaseRequestException e) {
			any = e;
		}

		assertTrue(any == null);
	}
}
