package org.example;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class Booking {

    String id;
    String room_id;
    String customer_id;
    Date check_in;
    Date check_out;

    @JsonCreator
    public Booking(@JsonProperty("customer_id") String NationalId, @JsonProperty("id") String resId, @JsonProperty("room_id") String roomNum, @JsonProperty("check_in") Date checkInDate, @JsonProperty("check_out") Date checkOutDate) {
        this.customer_id = NationalId;
        this.id = resId;
        this.room_id = roomNum;
        this.check_in = checkInDate;
        this.check_out = checkOutDate;

    }

    public int getStayDurationInDays() {
        long diffInMillis = check_out.getTime() - check_in.getTime();
        return (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public Date getCheck_in() { return check_in; }
    public Date getCheck_out() { return check_out; }
}
