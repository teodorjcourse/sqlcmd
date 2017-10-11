package sqlcmd.int_test;

import org.junit.Ignore;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import sqlapp.AppConfig;
import sqlapp.Locale;
import sqlapp.TranslationBase;
import sqlapp.controller.SQLCmdController;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.view.View;
import sqlcmd.DataBaseManagerTestBase;

@Ignore
public class IntBase extends DataBaseManagerTestBase {
	protected View viewMock;
	protected ArgumentCaptor<String> argument;
	protected SQLCmdController controller;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		viewMock = Mockito.mock(View.class);
		AppConfig.translationBase = Mockito.mock(TranslationBase.class);
		Mockito.when(AppConfig.translationBase.getTranslation(Mockito.anyString())).thenAnswer(
				invocationOnMock -> {
					return invocationOnMock.getArgumentAt(0, String.class);
				}
		);

        Mockito.doCallRealMethod().when(AppConfig.translationBase).setLocale(Mockito.any(Locale.class));
        Mockito.doCallRealMethod().when(AppConfig.translationBase).locale();

		argument = ArgumentCaptor.forClass(String.class);
		controller = new SQLCmdController(dataBaseManager);
		controller.addView(viewMock);

	}

	protected void prepareDatabase()
			throws DataBaseRequestException
	{
		dataBaseManager.openConnection(
				username(),
				password()
		);

		dataBaseManager.createDataBase(
				TEST_DATABASE_NAME
		);

		dataBaseManager.closeConnection();
	}

	protected void openConnection()
			throws DataBaseRequestException
	{
		dataBaseManager.openConnection(
				username(),
				password()
		);
	}

	protected void closeConnection()
			throws DataBaseRequestException
	{
		dataBaseManager.closeConnection();
	}

	protected void setConnection()
			throws DataBaseRequestException
	{
		dataBaseManager.connect(
				TEST_DATABASE_NAME,
				username(),
				password()
		);
	}

	protected void prepairEmptyTable()
			throws DataBaseRequestException
	{
		try {
			prepareDatabase();
		} catch (Throwable any) {}
		try {
			setConnection();
			dataBaseManager.createTable(TEST_TABLE, new String[]{});
		} catch (Throwable any) {}

		dataBaseManager.closeConnection();
	}

	protected void prepairTable(String[] cols, String[] keyValues)
			throws DataBaseRequestException
	{
		try {
			prepareDatabase();
		} catch (Throwable any) {}

		setConnection();
		dataBaseManager.createTable(TEST_TABLE, cols);
		dataBaseManager.insertRow(TEST_TABLE, keyValues);
		dataBaseManager.closeConnection();
	}

}
