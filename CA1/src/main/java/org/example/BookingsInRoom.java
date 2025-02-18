package org.example;

import java.util.List;

public class BookingsInRoom {
    private int roomId;
    private int capacity;
    private List<BookingWithCustomer> bookings;

    public BookingsInRoom(int roomId, int capacity, List<BookingWithCustomer> bookings) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.bookings = bookings;
    }

    // Getters and Setters
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public List<BookingWithCustomer> getBookings() { return bookings; }
    public void setBookings(List<BookingWithCustomer> bookings) { this.bookings = bookings; }
}
