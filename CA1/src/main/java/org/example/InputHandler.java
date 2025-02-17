package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class InputHandler {
    public static HotelData readJsonFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), HotelData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
