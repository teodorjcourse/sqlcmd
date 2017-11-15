package sqlcmd;

import org.junit.Ignore;
import sqlapp.model.managers.Configuration;
import junit.framework.TestCase;
import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.managers.DataBaseManager;
import sqlapp.model.managers.PostgreSQLManager;

import java.util.Properties;

@Ignore
public class DataBaseManagerTestBase extends TestCase {
	// UNCOMMENT THIS OR SET DATABASE NAME VALUES
	protected static final String TEST_DATABASE_NAME = //"test_junit_auto";
	protected static final String TEST_DATABASE_NAME_2 = //"test_junit_auto_2";
	protected static final String TEST_TABLE = "foo";

	protected DataBaseManager dataBaseManager;
	protected Configuration testConfiguration;

	@Override
	protected void setUp()
			throws Exception
	{

		// SET THE PROPS OR UNCOMMENT CODE BELLOW TO RUN TESTS
		// BE AWARE THAT DURING THIS TEST <TEST_DATABASE_NAME> and <TEST_DATABASE_NAME_2>
		// WILL BE CREATED AND DELETED MULTIPLE TIMES !!!
		// DO NOT UNCOMMENT CODE BELOW IF YOU DONT WANT THIS OPERATIONS TO BE EXECUTED !!!

//		Properties testDBProps = new Properties();

		testDBProps.setProperty("jdbc.driver", "org.postgresql.Driver");
		testDBProps.setProperty("jdbc.connection_string", "jdbc:postgresql://");
		testDBProps.setProperty("jdbc.host", "localhost");
		testDBProps.setProperty("jdbc.port", "5432");
		testDBProps.setProperty("jdbc.username", "postgres");
		testDBProps.setProperty("jdbc.password", "qwerty");

		testConfiguration = new Configuration(testDBProps);
		dataBaseManager = new PostgreSQLManager(testConfiguration);
		clearAll();
	}

	@Override
	protected void tearDown()
			throws Exception
	{
		clearAll();
	}

	protected String username() {
		return testConfiguration.username();
	}

	protected String password() {
		return testConfiguration.password();
	}

	private void clearAll()
			throws Exception
	{
		try {
			dataBaseManager.closeConnection();

			dataBaseManager.openConnection(
					username(),
					password()
			);

			try {
				dataBaseManager.dropDataBase(
						TEST_DATABASE_NAME
				);
			} catch (Throwable any) {
			}

			try {
				dataBaseManager.dropDataBase(
						TEST_DATABASE_NAME_2
				);
			} catch (Throwable any) {}

		} catch (DataBaseRequestException e) {

		}

		dataBaseManager.closeConnection();
	}
}
