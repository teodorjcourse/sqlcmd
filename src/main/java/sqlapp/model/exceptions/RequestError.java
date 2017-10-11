package sqlapp.model.exceptions;

public final class RequestError {
    // ошибка загрузки драйвера
    public static final short DRIVER_LOAD_ERROR = 2;

    // Ошибка соединения с БД, во время выполнения sql запроса
    public static final short CONNECTION_ERROR = 4;

    //Ошибка создания соединение с БД
    public static final short CONNECTION_SET_ERROR = 5;

    public static final short CONNECTION_DOESNT_SET = 6;

    public static final short CONNECTION_ALREADY_SET = 7;

    //Ощибка выполнения запроса
    public static final short REQUEST_ERROR = 8;
    public static final short SYNTAX_ERROR = 9;

    // ошибка создания стейтмента
    public static final short CREATE_STATEMENT_ERROR = 10;

    // ошибка недопустимого значения переменной или его отсутствия
    public static final short CMD_ARG_ERROR = 11;
    // ошибка во время закрытия соединения
    public static final short CLOSE_CONNECTION_ERROR = 12;


    public static final short DATABASE_ALREADY_EXISTS_ERROR = 16;
    public static final short DATABASE_DOESNT_EXISTS_ERROR = 17;
    public static final short DATABASE_IN_USE_DROP_ERROR = 18;

    public static final short TABLE_ALREADY_EXISTS_ERROR = 20;
    public static final short TABLE_DOESNT_EXISTS_ERROR = 21;



    private RequestError() {}
}
