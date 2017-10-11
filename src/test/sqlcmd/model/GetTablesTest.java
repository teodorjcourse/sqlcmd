package sqlcmd.model;

import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;
import sqlcmd.DataBaseManagerTestBase;

import java.util.List;

public class GetTablesTest extends DataBaseManagerTestBase {

    /**
     * Попытка отправить заспрос на получение списка таблиц при неустановленном соединении
     * вызовет ошибку с кодом {@link RequestError#CONNECTION_DOESNT_SET}
     */
    public void testGetTablesFail() {
        short errCodeExpectin = RequestError.CONNECTION_DOESNT_SET;
        short errCode = -1;

        try {
            dataBaseManager.getTables();
        } catch (DataBaseRequestException e) {
            errCode = e.error();
        }

        assertEquals(errCodeExpectin, errCode);
    }

    /**
     * соединение установлено, получаем пустой список таблиц
     */
    public void testGetTablesSuccess() {
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

            List<String> tables = dataBaseManager.getTables();

            assertEquals(0, tables.size());
        } catch (DataBaseRequestException e) {
            any = e;
        }

        assertTrue(any == null);
    }


}
