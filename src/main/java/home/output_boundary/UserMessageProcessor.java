package home.output_boundary;

import home.input_boundary.IncomingMessage;

public interface UserMessageProcessor {
    /**
     * delegate {@link IncomingMessage} to {@link UserMessageHandler}
     */
    OutgoingMessage delegate(IncomingMessage incomingMessage);
}