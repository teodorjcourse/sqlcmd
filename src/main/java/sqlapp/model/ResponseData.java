package sqlapp.model;

import sqlapp.model.events.Data;

public class ResponseData implements Data {
    private Object message;

    public ResponseData(Object message) {
        if (message == null) {
            this.message = "";
        } else {
            this.message = message;
        }
    }

    @Override
    public String toString() {
        return message.toString();
    }

    @Override
    public Object data() {
        return message;
    }
}
