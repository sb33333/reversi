package home.util;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import home.output_boundary.OutgoingMessage;

import java.io.IOException;

/**
 * {@link home.output_boundary.OutgoingMessage} json serializer
 */
public class OutgoingMessageSerializer extends JsonSerializer<OutgoingMessage> {
    
    /**
     * exclude {@link home.output_boundary.OutgoingMessage#getReceiverIds}
     */
    @Override
    public void serialize(OutgoingMessage value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("resultStatus");
        gen.writeObject(value.getResultStatus());
        gen.writeFieldName("messageType");
        gen.writeObject(value.getMessageType());
        gen.writeFieldName("userMessagePayload");
        gen.writeObject(value.getUserMessagePayload());
        gen.writeEndObject();
    }
}