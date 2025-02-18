package org.example;


public class Main {
    public static void main(String[] args) {

        System.out.printf("Hello and welcome!\n");
        Hotel hotel = new Hotel();

        System.out.println("Customers: " + hotel.getAllCustomers().size());
        System.out.println("Rooms: " + hotel.getAllRooms().size());
        System.out.println("Bookings: " + hotel.getAllBookings().size());
        System.out.println(hotel.getOldestCustomerName());


    }
}