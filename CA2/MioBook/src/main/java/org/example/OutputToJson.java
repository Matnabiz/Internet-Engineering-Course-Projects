package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Map;

public class OutputToJson {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // Enable pretty print (indent output)
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Register JavaTimeModule for Java 8 Date/Time support
        objectMapper.registerModule(new JavaTimeModule()); // Registering JavaTimeModule to handle LocalDate, LocalDateTime etc.
    }

    public static String generateJson(boolean success, String message, Map<String, Object> data) {
        try {
            Map<String, Object> response;
            if (data == null) {
                response = Map.of(
                        "success", success,
                        "message", message
                );
            } else {
                response = Map.of(
                        "success", success,
                        "message", message,
                        "data", data
                );
            }

            // Convert the response map to JSON string
            String jsonOutput = objectMapper.writeValueAsString(response);

            System.out.println(jsonOutput);

            return jsonOutput;
        } catch (Exception e) {
            e.printStackTrace();
            String errorJson = "{\"success\": false, \"message\": \"Failed to generate JSON\", \"data\": {}}";
            System.out.println(errorJson);
            return errorJson;
        }
    }
}
