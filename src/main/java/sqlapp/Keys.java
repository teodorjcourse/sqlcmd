package sqlapp;

public enum Keys {
	// commands
	CONNECT_COMMAND_DESC("connect_desc"),
	CREATE_COMMAND_DESC("create_desc"),
	DROP_COMMAND_DESC("drop_desc"),
	DELETE_COMMAND_DESC("delete_desc"),
	CLEAR_COMMAND_DESC("clear_desc"),
	FIND_COMMAND_DESC("find_desc"),
	INSERT_COMMAND_DESC("insert_desc"),
	TABLES_COMMAND_DESC("tables_desc"),
	UPDATE_COMMAND_DESC("update_desc"),
	HELP_COMMAND_DESC("help_desc"),
	EXIT_COMMAND_DESC("exit_desc"),

	// request errors
	CONNECTION_SET_ERROR("connection_set_error"),
	CONNECTION_ERROR("connection_error"),
	REQUEST_ERROR("request_error"),
	DRIVER_LOAD_ERROR("driver_load_error"),
	COMMAND_ARGUMENT_ERROR("cmd_arg_error"),
	HELP_INFO_TEXT("help_info_text"),
	WRONG_COMMAND_ERROR("wrong_command_error"),
	INPUT_ERROR("input_error"),
	DATABASE_NOT_EXIST_ERROR("database_not_exist_error"),
	DATABASE_EXISTS_ERROR("database_exist_error"),

	// request succsess
	CLEAR_TABLE_SUCCESS("clear_table_success"),
	DROP_TABLE_SUCCESS("drop_table_success"),
	CREATE_TABLE_SUCCESS("create_table_success"),
	INSERT_TABLE_SUCCESS("insert_table_success"),

	// common onsole texts
	GREETING("greeting_text"),
	GOODBYE("bye_bye_text"),
	INPUT("type_input"),


	CONNECTION_SET("connection_set");


	private String key;

	Keys(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return key;
	}
}
