package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Room {
    // Attributes (Fields)
    private int id;
    private int capacity;

    @JsonCreator
    public Room(@JsonProperty("id") int roomNumber, @JsonProperty("capacity") int capacity) {
        this.id = roomNumber;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void displayInfo() {
        System.out.println("Room Number: " + id);
        System.out.println("Capacity: " + capacity);
    }

}