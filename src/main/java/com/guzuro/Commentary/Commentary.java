package com.guzuro.Commentary;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonSerialize(using = CommentaryObjectToJson.class)
@JsonDeserialize(using = JsonToCommentaryObject.class)
public class Commentary {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long id;

    private String text;

    private long todo_id;

    private LocalDateTime created_at;

    public Commentary(){}

    public Commentary(long id, String text, long todo_id, LocalDateTime created_at) {
        this.id = id;
        this.text = text;
        this.todo_id = todo_id;
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(long todo_id) {
        this.todo_id = todo_id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}

class CommentaryObjectToJson extends JsonSerializer<Commentary> {
    @Override
    public void serialize(Commentary commentary, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", commentary.getId());
        jsonGenerator.writeStringField("text", commentary.getText());
        jsonGenerator.writeNumberField("todo_id", commentary.getTodo_id());
        jsonGenerator.writeStringField("created_at", commentary.getCreated_at().toString());
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
    }
}

class JsonToCommentaryObject extends JsonDeserializer {

    @Override
    public Commentary deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Long id = 0L;
        if (node.has("id")) {
            id = node.get("id").longValue();
        }
        String text = node.get("text").textValue();
        Long todo_id = node.get("todo_id").longValue();
        LocalDateTime ldt = LocalDateTime.parse(node.get("created_at").textValue(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return new Commentary(id, text, todo_id, ldt);
    }
}
