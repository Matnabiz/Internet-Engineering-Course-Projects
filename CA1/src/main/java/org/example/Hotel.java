package org.example;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class Hotel {
    private List<Room> rooms;
    private List<Customer> customers;
    private List<Booking> bookings;

    public Hotel() {
        this.rooms = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.bookings = new ArrayList<>();

        HotelData data = InputHandler.readJsonFile("C:\\Users\\Windows 11\\Desktop\\Internet-Engineering-Course-Projects\\CA1\\src\\main\\java\\org\\example\\data.json");

        if (data != null) {
            Set<String> uniqueRoomNumbers = new HashSet<>();
            Set<String> uniqueCustomerSSNs = new HashSet<>();
            Set<String> uniqueBookingIds = new HashSet<>();

            for (Room room : data.getRooms()) {
                if (uniqueRoomNumbers.add(String.valueOf(room.getRoomNumber()))) {
                    this.rooms.add(room);
                } else {
                    System.err.println("Duplicate room found: " + room.getRoomNumber() + " → Skipping");
                }
            }

            // Ensure Unique Customers
            for (Customer customer : data.getCustomers()) {
                if (uniqueCustomerSSNs.add(customer.getSsn())) {
                    this.customers.add(customer);
                } else {
                    System.err.println("Duplicate customer found (SSN: " + customer.getSsn() + ") → Skipping");
                }
            }

            // Ensure Unique Bookings
            for (Booking booking : data.getBookings()) {
                if (uniqueBookingIds.add(booking.getResId())) {
                    this.bookings.add(booking);
                } else {
                    System.err.println("Duplicate booking found (Reservation ID: " + booking.getResId() + ") → Skipping");
                }
            }
        }
        else {
            this.customers = new ArrayList<>();
            this.rooms = new ArrayList<>();
            this.bookings = new ArrayList<>();
        }
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

    public List<Booking> getAllBookings() {
        List<Booking> allBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            allBookings.add(booking);
        }
        return allBookings;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> allCustomers = new ArrayList<>();
        for (Customer customer : customers) {
            allCustomers.add(customer);
        }
        return allCustomers;
    }

    public List<Room> getAllRooms(){
        List<Room> allRooms = new ArrayList<>();
        for (Room room : rooms) {
            allRooms.add(room);
        }
        return allRooms;
    }

    public List<Room> getRooms(int minCapacity) {
        Collectors Collectors = null;
        return rooms.stream()
                .filter(room -> room.getCapacity() >= minCapacity)
                .collect(Collectors.toList());
    }

    public String getOldestCustomerName() {
        return customers.stream()
                .max(Comparator.comparingInt(Customer::getAge))
                .map(Customer::getName)
                .orElse("No customers found");
    }

    public String getCustomerPhonesByRoomNumber(String roomNumber) {
        List<String> phoneNumbers = new ArrayList<>();

        for (Booking booking : bookings) {
            if (booking.roomNumber.equals(roomNumber)) {
                for (Customer customer : customers) {
                    if (customer.getSsn().equals(booking.ssn)) {
                        phoneNumbers.add(customer.getPhone());
                    }
                }
            }
        }
        return phoneNumbers.isEmpty() ? "No customers found for this room." : String.join(", ", phoneNumbers);
    }

    public void exportToJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HotelData hotelData = new HotelData();
            hotelData.setRooms(this.rooms);
            hotelData.setBookings(this.bookings);
            hotelData.setCustomers(this.customers);

            objectMapper.writeValue(new File(filePath), hotelData);
            System.out.println("Data successfully exported to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error exporting data to JSON: " + e.getMessage());
        }
    }

    public void logState(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<BookingsInRoom> BookingsInRoomList = rooms.stream().map(room -> {
                List<BookingWithCustomer> roomBookings = bookings.stream()
                        .filter(booking -> booking.getRoomNumber().equals(String.valueOf(room.getRoomNumber())))
                        .map(booking -> new BookingWithCustomer(
                                Integer.parseInt(booking.getResId()),
                                customers.stream()
                                        .filter(c -> c.getSsn().equals(booking.getSsn()))
                                        .findFirst()
                                        .orElse(null),
                                booking.getCheckInDate(),
                                booking.getCheckOutDate()
                        ))
                        .collect(Collectors.toList());

                return new BookingsInRoom(room.getRoomNumber(), room.getCapacity(), roomBookings);
            }).collect(Collectors.toList());

            objectMapper.writeValue(new File(filePath), BookingsInRoomList);
            System.out.println("State Log successfully logged to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error exporting state log to JSON: " + e.getMessage());
        }
    }


}




