package org.example;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class Booking {

    String resId;
    String roomNumber;
    String ssn;
    Date checkInDate;
    Date checkOutDate;

    @JsonCreator
    public Booking(@JsonProperty("customer_id") String ssn, @JsonProperty("id") String resId, @JsonProperty("room_id") String roomNum, @JsonProperty("check_in") Date checkInDate, @JsonProperty("check_out") Date checkOutDate) {
        this.ssn = ssn;
        this.resId = resId;
        this.roomNumber = roomNum;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;

    }

    public int getStayDurationInDays() {
        long diffInMillis = this.checkOutDate.getTime() - this.checkInDate.getTime();
        return (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public Date getCheckInDate() { return this.checkInDate; }
    public Date getCheckOutDate() { return this.checkOutDate; }

    public String getRoomNumber() { return this.roomNumber; }
}
