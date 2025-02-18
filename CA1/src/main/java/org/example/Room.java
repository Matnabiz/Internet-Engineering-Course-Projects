package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Room {
    private int number;
    private int capacity;

    @JsonCreator
    public Room(@JsonProperty("id") int roomNumber, @JsonProperty("capacity") int capacity) {
        this.number = roomNumber;
        this.capacity = capacity;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void displayInfo() {
        System.out.println("Room Number: " + this.number);
        System.out.println("Capacity: " + this.capacity);
    }

    public int getRoomNumber() { return this.number; }
}