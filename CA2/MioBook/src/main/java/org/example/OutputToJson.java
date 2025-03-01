package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Map;

public class OutputToJson {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static String generateJson(boolean success, String message, Map<String, Object> data) {
        try {
            Map<String, Object> response = Map.of(
                    "success", success,
                    "message", message,
                    "data", data
            );

            String jsonOutput = objectMapper.writeValueAsString(response);

            System.out.println(jsonOutput);

            return jsonOutput;
        } catch (Exception e) {
            String errorJson = "{\"success\": false, \"message\": \"Failed to generate JSON\", \"data\": {}}";
            System.out.println(errorJson); // Print error JSON
            return errorJson;
        }
    }
}
