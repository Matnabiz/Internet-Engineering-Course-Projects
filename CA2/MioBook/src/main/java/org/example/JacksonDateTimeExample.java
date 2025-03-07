package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.time.LocalDateTime;

public class JacksonDateTimeExample {
    public static void main(String[] args) {
        try {
            // Create ObjectMapper and register the JavaTimeModule
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Registering the module for Java 8 Time API

            // Serialize a LocalDateTime object
            LocalDateTime dateTime = LocalDateTime.now();
            String jsonDateTime = objectMapper.writeValueAsString(dateTime);
            System.out.println("Serialized LocalDateTime: " + jsonDateTime);

            // Deserialize the JSON back into a LocalDateTime object
            LocalDateTime deserializedDateTime = objectMapper.readValue(jsonDateTime, LocalDateTime.class);
            System.out.println("Deserialized LocalDateTime: " + deserializedDateTime);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
