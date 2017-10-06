package sqlapp.model.managers;

import sqlapp.model.exceptions.DataBaseRequestException;
import sqlapp.model.exceptions.RequestError;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DataBaseManager {
	private Connection connection;

	protected abstract String driver();
	protected abstract String connectionString();
	protected abstract String truncateTableTemplate();
	protected abstract String deleteTableTemplate();
	protected abstract String createTableTemplate();
	protected abstract String selectTableTemplate();
	protected abstract String insertRawTemplate();

	public void connect(String database, String userName, String password)
			throws DataBaseRequestException
	{
		loadDriver(driver());
		setConnection(database, userName, password);
	}

	private void loadDriver(String driver)
			throws DataBaseRequestException
	{
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {

			DataBaseRequestException exc =
					new DataBaseRequestException(
							RequestError.DRIVER_LOAD_ERROR,
							String.format("Driver \"%s\" load error", driver)
					);

			exc.addSuppressed(e);

			throw exc;
		}
	}

	private void setConnection(String database, String userName, String password)
			throws DataBaseRequestException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(connectionString()).append(database);

		try {
			connection = DriverManager.getConnection(sb.toString(), userName, password);
		} catch (SQLException e) {
			DataBaseRequestException exc = new DataBaseRequestException(RequestError.CONNECTION_SET_ERROR);
			exc.addSuppressed(e);

			throw exc;
		}
	}

	public void closeConnection()
			throws DataBaseRequestException

	{
		try {
			if (connection == null || connection.isClosed()) {
				return;
			}

			connection.close();
		} catch (SQLException e) {
			DataBaseRequestException requestException =
					new DataBaseRequestException(RequestError.CLOSE_CONNECTION_ERROR);
			requestException.addSuppressed(e);

			throw new DataBaseRequestException(RequestError.CLOSE_CONNECTION_ERROR);
		}
	}


	public void createTable(String tableName, String columns)
			throws DataBaseRequestException
	{

		String sqlQuery = String.format(createTableTemplate(), tableName, columns);
        execute(sqlQuery);
	}

	public final void clearTable(String tableName)
			throws DataBaseRequestException
	{
		String sqlQuery = String.format(truncateTableTemplate(), tableName);
        execute(sqlQuery);
	}

    public void insert(String tableName, String keys, String values)
            throws DataBaseRequestException
    {

        String sqlQuery = String.format(insertRawTemplate(), tableName, keys, values);
        execute(sqlQuery);
    }

	public final void dropTable(String tableName)
			throws DataBaseRequestException
	{
		String sqlQuery = String.format(deleteTableTemplate(), tableName);
        execute(sqlQuery);
	}

	private void checkConnection()
		throws DataBaseRequestException
	{
		try {
			if (connection == null || connection.isClosed()) {
				throw new DataBaseRequestException(RequestError.CONNECTION_ERROR);
			}
		} catch (SQLException e) {
			DataBaseRequestException exc = new DataBaseRequestException(RequestError.CONNECTION_ERROR);
			exc.addSuppressed(exc);
			throw exc;
		}
	}

	public List<String> getTables()
			throws DataBaseRequestException
	{
		checkConnection();

		final String TABLE_TYPE = "TABLE";
		final String COULMN_LABEL = "TABLE_NAME";

		List<String> result = new ArrayList<>();

		try {
			DatabaseMetaData md = connection.getMetaData();
			try (ResultSet rs = md.getTables(null, null, "%", new String[]{TABLE_TYPE})) {
				while (rs.next()) {
					result.add(rs.getString(COULMN_LABEL));
				}
			}
		} catch (Exception e) {
			DataBaseRequestException exc = new DataBaseRequestException(
					RequestError.REQUEST_ERROR,
					"Get tables request error"
			);

			exc.addSuppressed(exc);

			throw exc;
		}

		return result;
	}

	private void execute(String sqlQuery)
			throws DataBaseRequestException
	{
		checkConnection();

		try (Statement statement = connection.createStatement()) {
			try {
				statement.execute(sqlQuery);
			} catch (SQLException e) {
				DataBaseRequestException requestException =
						new DataBaseRequestException(
								RequestError.REQUEST_ERROR,
								String.format("SQL query error: %s", sqlQuery)
						);

				requestException.addSuppressed(e);

				throw requestException;
			}

		} catch (SQLException e) {
			DataBaseRequestException requestException =
					new DataBaseRequestException(RequestError.CREATE_STATEMENT_ERROR);
			requestException.addSuppressed(e);

			throw requestException;
		}
	}

	public ArrayList<ArrayList<String>> selectTable(String tableName)
			throws DataBaseRequestException
	{
        checkConnection();

        ArrayList<ArrayList<String>> result = new ArrayList<>();
        String sqlQuery = String.format(selectTableTemplate(), tableName);

		try {
			Statement st = connection.createStatement();
			ResultSet rset = st.executeQuery(sqlQuery);
			ResultSetMetaData md = rset.getMetaData();

			int columnsCount = md.getColumnCount();

            if (columnsCount > 0) {
                ArrayList<String> columns = new ArrayList<>();

                for (int i = 1; i <= columnsCount; i++) {
                    columns.add(md.getColumnName(i));
                }
                result.add(columns);
            }

			while (rset.next()) {
                ArrayList<String> row = new ArrayList<>();
				for (int i = 1; i <= columnsCount; i++) {
                    row.add(rset.getString(i));
				}
                result.add(row);
			}
		} catch (SQLException e) {
            DataBaseRequestException requestException =
                    new DataBaseRequestException(
                            RequestError.REQUEST_ERROR,
                            String.format("SQL query error: %s", sqlQuery)
                    );

            requestException.addSuppressed(e);

            throw requestException;
		}

        return result;
	}
}
