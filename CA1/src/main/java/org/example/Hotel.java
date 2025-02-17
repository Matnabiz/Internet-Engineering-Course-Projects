package org.example;
import java.util.*;

public class Hotel {
    private List<Room> rooms;
    private List<Customer> customers;
    private List<Booking> bookings;

    public Hotel() {
        this.rooms = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public List<Room> getRooms(int minCapacity) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getCapacity() >= minCapacity) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public String getOldestCustomerName() {
        if (customers.isEmpty()) return "No customers available.";

        Customer oldest = customers.get(0);
        for (Customer customer : customers) {
            if (customer.getAge() > oldest.getAge()) {
                oldest = customer;
            }
        }
        return oldest.getName();
    }

    public String getCustomerPhonesByRoomNumber(String roomNumber) {
        List<String> phoneNumbers = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.roomNum.equals(roomNumber)) {
                for (Customer customer : customers) {
                    if (customer.getNationalNumber().equals(booking.NationalId)) {
                        phoneNumbers.add(customer.getPhoneNumber());
                    }
                }
            }
        }
        return phoneNumbers.isEmpty() ? "No customers found for this room." : String.join(", ", phoneNumbers);
    }
}
