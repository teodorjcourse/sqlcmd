package sqlapp.controller.exceptions;

public class CommandNotFoundException extends Exception {
	private String request;

	public CommandNotFoundException(String message, String request) {
		super(message);

		this.request = request;
	}

	public CommandNotFoundException(String request) {
		this("Command not found exception", request);
	}

	public String request() {
		return request;
	}
}
