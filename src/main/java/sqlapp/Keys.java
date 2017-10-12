package sqlapp;

public enum Keys {
	// sqlcmd.commands
	LANGUAGE_COMMAND_DESC("lang_desc"),
	CONNECT_COMMAND_DESC("connect_desc"),
	OPEN_CONNECTION_COMMAND_DESC("open_connection_desc"),
	CLOSE_CONNECTION_COMMAND_DESC("close_connection_desc"),
	CREATE_DATABASE_COMMAND_DESC("create_db_desc"),
	DROP_DATABASE_COMMAND_DESC("drop_db_desc"),
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
	SYNTAX_ERROR("synax_error"),
	CONNECTION_ERROR("connection_error"),
	REQUEST_ERROR("request_error"),
	DRIVER_LOAD_ERROR("driver_load_error"),
	COMMAND_ARGUMENT_ERROR("cmd_arg_error"),
	HELP_INFO_TEXT("help_info_text"),
	WRONG_COMMAND_ERROR("wrong_command_error"),
	INPUT_ERROR("input_error"),
	TABLE_DOESNT_EXIST_ERROR("table_doesnt_exist_error"),
	TABLE_ALREADY_EXISTS_ERROR("table_already_exists_error"),
	DATABASE_DOESNT_EXISTS_ERROR("database_doesnt_exists_error"),
	DATABASE_ALREADY_EXISTS_ERROR("database_already_exists_error"),

	// request succsess
	CLEAR_TABLE_SUCCESS("clear_table_success"),
	DROP_TABLE_SUCCESS("drop_table_success"),
	CREATE_TABLE_SUCCESS("create_table_success"),
	INSERT_TABLE_SUCCESS("insert_table_success"),
	DELETE_ROW_SUCCESS("delete_row_success"),
	DELETE_ROW_SUCCESS_ZERO("delete_row_success_zero"),
	UPDATE_ROW_SUCCESS("update_row_success"),
	UPDATE_ROW_SUCCESS_ZERO("update_row_success_zero"),
	CONNECTION_CLOSED("connection_closed"),
	DATABASE_CREATE_SUCCESS("database_create_success"),
	DATABASE_DROP_SUCCESS("database_drop_success"),
	LANGUAGE_CHANGED_SUCCESS("language_changed_success"),
	LANGUAGE_CHANGED_ERROR("language_changed_error"),

	// common onsole texts
	GREETING("greeting_text"),
	GOODBYE("bye_bye_text"),
	INPUT("type_input"),

	CONNECTION_SET("connection_set"),
	CONNECTION_ALREADY_SET("connection_already_set"),

	EMPTY_TABLE_TEXT("empty_tables_text");


	private String key;

	Keys(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return key;
	}
}
