package org.example;

import java.util.List;

public class BookingsOfRoom {
    private int roomNumber;
    private int capacity;
    private List<BookingWithCustomer> bookings;

    public BookingsOfRoom(int roomId, int capacity, List<BookingWithCustomer> bookings) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.bookings = bookings;
    }

    public int getRoomNumber() { return this.roomNumber; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public List<BookingWithCustomer> getBookings() { return bookings; }
    public void setBookings(List<BookingWithCustomer> bookings) { this.bookings = bookings; }
}
