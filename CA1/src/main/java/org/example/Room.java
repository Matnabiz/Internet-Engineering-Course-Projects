package org.example;

public class Room {
    // Attributes (Fields)
    private int roomNumber;
    private int capacity;

    public Room(int roomNumber, int capacity) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void displayInfo() {
        System.out.println("Room Number: " + roomNumber);
        System.out.println("Capacity: " + capacity);
    }

}