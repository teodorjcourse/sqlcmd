package sqlcmd.model;

import sqlcmd.DataBaseManagerTestBase;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;

import java.util.ArrayList;

public class CreateTableTest extends DataBaseManagerTestBase {

	/**
	 * Содеинениене не установлено
	 * Попытка создать таблицу вызывает исключение с кодом ошибки {@link RequestError#CONNECTION_DOESNT_SET}
	 */
	public void testCreateConnectionDoesntSet() {
		final String TABLE_NAME = "foo";
		int errCodeExcpecting = RequestError.CONNECTION_DOESNT_SET;
		int errCodeResult = -1;

		try {
			dataBaseManager.createTable(
					TABLE_NAME,
					new String[]{}
			);

		} catch (DataBaseRequestException e) {
			errCodeResult = e.error();
		}

		assertEquals(errCodeExcpecting, errCodeResult);
	}

	/**
	 * Содеинениене установлено
	 * Успешно создаем пустую таблицу
	 */
	public void testCreateEmptyTable() {
		final String TABLE_NAME = "foo";
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
					new String[]{}
			);

			ArrayList<ArrayList<String>> result = dataBaseManager.selectTable(
					TABLE_NAME
			);

			assertEquals(0, result.size());

		} catch (DataBaseRequestException e) {
			any = e;
		}

		assertTrue(any == null);
	}

	/**
	 * Содеинениене установлено
	 * Создаем таблицу, вводим нулевое значение для колонки
	 * Менеджер выбросит исключение с кодом {@link RequestError#REQUEST_ERROR}
	 */
	public void testCreateTableWrongColumnName() {
		final String TABLE_NAME = "foo";
		int errCodeExcpecting = RequestError.SYNTAX_ERROR;
		int errCodeResult = -1;

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
					new String[]{null}
			);

			ArrayList<ArrayList<String>> result = dataBaseManager.selectTable(
					TABLE_NAME
			);

			assertEquals(0, result.size());

		} catch (DataBaseRequestException e) {
			errCodeResult = e.error();
		}

		assertEquals(errCodeExcpecting, errCodeResult);
	}

	/**
	 * Содеинениене установлено
	 * Создаем таблицу и пару колонок
	 * Проверяем, что все ок
	 */
	public void testCreateTableWithTwoColumns() {
		final String TABLE_NAME = "foo";
		final String[] expectedColumns = {"col_1", "col_2"};
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

			ArrayList<ArrayList<String>> result = dataBaseManager.selectTable(
					TABLE_NAME
			);

			int orderIndex = 0;
			for (ArrayList<String> rows : result) {
				for (String colValue : rows) {
					assertEquals(expectedColumns[orderIndex++], colValue);
				}
			}

		} catch (DataBaseRequestException e) {
			any = e;
		}

		assertTrue(any == null);
	}

}
