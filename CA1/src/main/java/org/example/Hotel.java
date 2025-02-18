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
            this.customers = data.getCustomers();
            this.rooms = data.getRooms();
            this.bookings = data.getBookings();
        } else {
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
            List<Room> roomList = rooms.stream().map(room -> {
                List<Booking> roomBookings = bookings.stream()
                        .filter(booking -> booking.getRoomNumber().equals(String.valueOf(room.getRoomNumber())))
                        .map(booking -> new Booking(
                                customers.stream()
                                        .filter(c -> c.getSsn().equals(booking.ssn))
                                        .findFirst()
                                        .orElse(null),
                                Integer.parseInt(booking.resId),
                                null,
                                booking.checkInDate,
                                booking.checkOutDate
                        ))
                        .collect(Collectors.toList());
                Room currentRoom = new Room(room.getRoomNumber(), room.getCapacity());

                currentRoom.setRelatedBookings(roomBookings);
                return currentRoom;
            }).collect(Collectors.toList());

            objectMapper.writeValue(new File(filePath), roomList);
            System.out.println("✅ State successfully logged to: " + filePath);
        } catch (IOException e) {
            System.err.println("❌ Error exporting state to JSON: " + e.getMessage());
        }
    }
}




