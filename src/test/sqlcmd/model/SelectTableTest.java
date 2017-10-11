package sqlcmd.model;

import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;
import sqlcmd.DataBaseManagerTestBase;

import java.util.ArrayList;

public class SelectTableTest extends DataBaseManagerTestBase {
    private final String TABLE = "foo";

    /**
     * Попытка отправить заспрос на получение содержимого таблицы при неустановленном соединении
     * вызовет ошибку с кодом {@link RequestError#CONNECTION_DOESNT_SET}
     */
    public void testSelectTableFail() {
        short errCodeExpectin = RequestError.CONNECTION_DOESNT_SET;
        short errCode = -1;

        try {
            dataBaseManager.selectTable(
                    TABLE
            );
        } catch (DataBaseRequestException e) {
            errCode = e.error();
        }

        assertEquals(errCodeExpectin, errCode);
    }

    /**
     * Соединение установлено
     * Попытка получить содержимое несуществующей таблицы вызовет
     * вызовет ошибку с кодом {@link RequestError#TABLE_DOESNT_EXISTS_ERROR}
     */
    public void testSelectTableDoesntExists() {
        short errCodeExpectin = RequestError.TABLE_DOESNT_EXISTS_ERROR;
        short errCode = -1;

        try {
            dataBaseManager.openConnection(
                    username(),
                    password()
            );

            dataBaseManager.selectTable(
                    TABLE
            );

        } catch (DataBaseRequestException e) {
            errCode = e.error();
        }

        assertEquals(errCodeExpectin, errCode);
    }

    /**
     * Соединение установлено
     * Менеджер отдает содержимое таблицы правильно
     */
    public void testSelectTable() {
        Throwable any = null;
        final String[] expectedTableContent = new String[]{"col_1", "col_2", "val_1", "val_2"};

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
                    TABLE,
                    new String[]{"col_1", "col_2"}
            );

            dataBaseManager.insertRow(
                    TABLE,
                    new String[]{"col_1", "val_1", "col_2", "val_2"}
            );

            ArrayList<ArrayList<String>> list = dataBaseManager.selectTable(
                    TABLE
            );


            int orderIndex = 0;
            for (ArrayList<String> rows : list) {
                for (String colValue : rows) {
                    assertEquals(expectedTableContent[orderIndex++], colValue);
                }
            }

        } catch (DataBaseRequestException e) {
            any = e;
        }

        assertTrue(any == null);
    }

}
