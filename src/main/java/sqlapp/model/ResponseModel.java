package sqlapp.model;


import sqlapp.model.events.*;

public class ResponseModel implements Dispatcher {
	private Dispatcher eventDispatcher;


	public ResponseModel() {
		eventDispatcher = new EventDispatcher();
	}

	public void push(Data data) {
		dispatch(new Event(Events.CHANGED.toString(), data));
	}

	@Override
	public void subscribe(String eventType, Callback listener) {
		eventDispatcher.subscribe(eventType, listener);
	}

	@Override
	public void unsubscribe(String eventType, Callback listener) {
		eventDispatcher.unsubscribe(eventType, listener);
	}

	@Override
	public void dispatch(Event event) {
		eventDispatcher.dispatch(event);
	}
}
