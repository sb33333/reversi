package home.util;


public class OutgoingMessageSerializer extends JsonSerializer<OutgoingMessage> {
    
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