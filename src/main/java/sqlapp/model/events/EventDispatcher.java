package sqlapp.model.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventDispatcher implements Dispatcher {
	HashMap<String, List<Callback>> eventList;

	public EventDispatcher() {
		eventList = new HashMap<>();
	}

	@Override
	public void subscribe(String eventType, Callback listener) {
		List<Callback> listeners = eventList.get(eventType);

		if (listeners == null) {
			listeners = new ArrayList<>();
			eventList.put(eventType, listeners);
		}

		listeners.add(listener);
	}

	@Override
	public void unsubscribe(String eventType, Callback listener) {
		List<Callback> listeners = eventList.get(eventType);

		if (listeners != null) {
			int index = listeners.indexOf(listener);
			listeners.remove(index);
		}
	}

	@Override
	public void dispatch(Event event) {
		List<Callback> listeners = eventList.get(event.type());
		if (listeners != null) {
			for (Callback listener : listeners) {
				listener.callback(event);
			}
		}
	}
}
