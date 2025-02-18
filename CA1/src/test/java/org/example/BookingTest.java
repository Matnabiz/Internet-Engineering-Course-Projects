package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
public class BookingTest {

    @Test
    void testDeserializeBooking() {
        String json = "{\"check_in\": \"2024-02-01T14:00:00\",\"check_out\": \"2024-02-03T14:00:00\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Booking booking = objectMapper.readValue(json, Booking.class);
            assertNotNull(booking.getCheckInDate());
            assertTrue(booking.getStayDurationInDays()>0);
        } catch (Exception e) {
            fail("Error during deserialization: " + e.getMessage());
        }
    }
}
