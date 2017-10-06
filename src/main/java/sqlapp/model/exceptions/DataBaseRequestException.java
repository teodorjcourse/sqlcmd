package sqlapp.model.exceptions;

public class DataBaseRequestException extends Exception {
    private short error;

    public DataBaseRequestException(short errCode, String message) {
        super(message);

        this.error = errCode;
    }

    public DataBaseRequestException(short errCode) {
        this(errCode, "Request exception");
    }

    public short error() {
        return error;
    }
}
