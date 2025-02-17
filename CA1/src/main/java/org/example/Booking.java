package org.example;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Booking {

    String id;
    String room_id;
    String customer_id;
    Date check_in;
    Date check_out;


    public Booking(String NationalId, String resId, String roomNum, Date checkInDate, Date checkOutDate) {
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
}
