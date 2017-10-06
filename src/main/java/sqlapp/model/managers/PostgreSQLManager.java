package sqlapp.model.managers;

import java.sql.PreparedStatement;
import java.util.HashMap;

/**
 * https://www.postgresql.org/docs/10/static/sql-commands.html
 */
public class PostgreSQLManager extends DataBaseManager {
	@Override
	protected String driver() {
		return "org.postgresql.Driver";
	}

	@Override
	protected String connectionString() {
		return "jdbc:postgresql://localhost:5432/";
	}

	@Override
	protected String truncateTableTemplate() {
		return "TRUNCATE TABLE %s;";
	}

	@Override
	protected String deleteTableTemplate() {
		return "DROP TABLE %s;";
	}

	@Override
	protected String createTableTemplate() {
		return "CREATE TABLE %s(%s);";
	}

	@Override
	protected  String selectTableTemplate() {
		return "SELECT * FROM %s;";
	}

	@Override
	protected  String insertRawTemplate() {
		return "INSERT INTO %s(%s) VALUES(%s);";
	}
}