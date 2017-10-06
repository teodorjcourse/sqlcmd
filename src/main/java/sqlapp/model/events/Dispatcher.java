package sqlapp.model.events;

public interface Dispatcher {
	void subscribe(String eventType, Callback listener);
	void unsubscribe(String eventType, Callback listener);
	void dispatch(Event event);
}
