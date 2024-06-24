package home.output_boundary;

import home.input_boundary.IncomingMessage;

public interface UserMessageHandler {
    /**
     * handle incoming message and create {@link OutgoingMessage outgoing} message
     */
    OutgoingMessage handleMessage(IncomingMessage incomingMessage);
}