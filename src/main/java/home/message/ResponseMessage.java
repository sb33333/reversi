package home.message;

import home.reversi.ResponseStatus;
import lombok.Getter;

@Getter
public class ResponseMessage {
    private MessageType messageType;
    private ResponseStatus responseStatus;
    private String response;
    private ResponseMessage(Builder builder) {
        super();
        this.responseStatus= builder.getResponseStatus();
        this.response= builder.getResponse();
        this.messageType =builder.getMessageType();
    }
    public static class Builder {
        @Getter private ResponseStatus responseStatus;
        @Getter private String response;
        @Getter private MessageType messageType;
        public Builder(MessageType messageType, ResponseStatus responseStatus) {
            this.messageType = messageType;
            this.responseStatus = responseStatus;
        }
        public Builder response (String response) {
            this.response = response;
            return this;
        }
        public ResponseMessage build() {
            return new ResponseMessage(this);
        }
    }
}