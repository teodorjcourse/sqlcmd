package sqlcmd.model;

import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;
import sqlcmd.DataBaseManagerTestBase;

public class UpdateTablesRowsTest extends DataBaseManagerTestBase {
    private final String TABLE = "foo";

    /**
     * Попытка обновить данные в таблице при неуставновленном соединении
     * вызовет ошибку с кодом {@link RequestError#CONNECTION_DOESNT_SET}
     */
    public void testConnectionDoesntSet() {
        short errCodeExpectin = RequestError.CONNECTION_DOESNT_SET;
        short errCode = -1;

        try {
            dataBaseManager.updateRows(
                    TABLE,
                    "",
                    "",
                    new String[]{}
            );
        } catch (DataBaseRequestException e) {
            errCode = e.error();
        }

        assertEquals(errCodeExpectin, errCode);
    }

    /**
     * Попытка обновить данные в несуществующей таблице
     * вызовет ошибку с кодом {@link RequestError#TABLE_DOESNT_EXISTS_ERROR}
     */
    public void testUpdateWrongTableName() {
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

            dataBaseManager.updateRows(
                    "wrong",
                    "col_1",
                    "some",
                    new String[]{"col_2", "some"}
            );
        } catch (DataBaseRequestException e) {
            errCode = e.error();
        }

        assertEquals(errCodeExpectin, errCode);
    }

    /**
     * Попытка обновить данные успешна
     */
    public void testUpdateSuccess() {
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
                    TABLE,
                    new String[] {"col_1", "col_2"}
            );

            dataBaseManager.insertRow(
                    TABLE,
                    new String[] {"col_1", "some"}
            );

            int result = 0;

            result = dataBaseManager.updateRows(
                    TABLE,
                    "col_1",
                    "wrong_key",
                    new String[]{"col_2", "value"}
            );

            assertEquals(0, result);

            result = dataBaseManager.updateRows(
                    TABLE,
                    "col_1",
                    "some",
                    new String[]{"col_2", "value"}
            );

            assertEquals(1, result);
        } catch (DataBaseRequestException e) {
            any = e;
        }

        assertTrue(any == null);
    }

}
