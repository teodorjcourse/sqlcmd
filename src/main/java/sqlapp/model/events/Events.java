package sqlapp.model.events;

public enum Events {
	CHANGED("changed"),
	ON_EXIT("exit");

	private final String type;

	Events(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}
}
