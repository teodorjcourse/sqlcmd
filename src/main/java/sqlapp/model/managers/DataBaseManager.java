package sqlapp.model.managers;

import sqlapp.model.exceptions.DataBaseRequestException;
import java.util.ArrayList;
import java.util.List;

public abstract class DataBaseManager {

	public abstract void openConnection(String userName, String password) throws DataBaseRequestException;

	public abstract void connect(String database, String userName, String password) throws DataBaseRequestException;

	public abstract void closeConnection() throws DataBaseRequestException;

	public abstract void createDataBase(String database) throws DataBaseRequestException;

	public abstract void dropDataBase(String database) throws DataBaseRequestException;

	public abstract void createTable(String tableName, String[] columns) throws DataBaseRequestException;

	public abstract void clearTable(String tableName) throws DataBaseRequestException;

    public abstract void insertRow(String tableName, String[] keyValue) throws DataBaseRequestException;

	public abstract int deleteRows(String tableName, String[] params) throws DataBaseRequestException;

	public abstract void dropTable(String tableName) throws DataBaseRequestException;

	public abstract List<String> getTables() throws DataBaseRequestException;

	public abstract ArrayList<ArrayList<String>> selectTable(String tableName) throws DataBaseRequestException;

	public abstract int updateRows(String tableName, String column, String value, String[] keyValue) throws DataBaseRequestException;
}
