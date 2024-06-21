package home.util;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import home.message.OutgoingMessage;

import java.io.IOException;

public class OutgoingMessageSerializer extends JsonSerializer<OutgoingMessage> {
    
    @Override
    public void serialize(OutgoingMessage value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("resultStatus");
        gen.writeObject(value.resultStatus());
        gen.writeFieldName("messageType");
        gen.writeObject(value.messageType());
        gen.writeFieldName("userMessagePayload");
        gen.writeObject(value.userMessagePayload());
        gen.writeEndObject();
    }
}