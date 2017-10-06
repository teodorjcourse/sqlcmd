package sqlapp.model.events;

public class Event {
	private final String eType;
	private Data eData;

	public Event(String type, Data data) {
		eType = type;
		eData = data;
	}

	public Event(String type) {
		this(type, null);
	}

	public Event() {
		this(null, null);
	}

	public final String type() {
		return eType;
	}

	public final Data data() {
		return eData;
	}
}
