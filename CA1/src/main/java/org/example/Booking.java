package org.example;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Booking {

    String NationalId;
    String resId;
    String roomNum;
    Date checkInDate;
    Date checkOutDate;


    public Booking(String NationalId, String resId, String roomNum, Date checkInDate, Date checkOutDate) {
        this.NationalId = NationalId;
        this.resId = resId;
        this.roomNum = roomNum;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;

    }

    public int getStayDurationInDays() {
        long diffInMillis = checkOutDate.getTime() - checkInDate.getTime();
        return (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }
}
