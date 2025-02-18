package org.example;


public class Main {
    public static void main(String[] args) {

        System.out.printf("Hello and welcome to the hotel management system!\n");
        Hotel hotel = new Hotel();

        System.out.println("Customers: " + hotel.getAllCustomers().size());
        System.out.println("Rooms: " + hotel.getAllRooms().size());
        System.out.println("Bookings: " + hotel.getAllBookings().size());
        System.out.println(hotel.getOldestCustomerName());
        hotel.exportToJson("C:\\Users\\Windows 11\\Desktop\\Internet-Engineering-Course-Projects\\CA1\\src\\main\\java\\org\\example\\outputFile.json");
        hotel.logState("C:\\Users\\Windows 11\\Desktop\\Internet-Engineering-Course-Projects\\CA1\\src\\main\\java\\org\\example\\log.json");

    }
}