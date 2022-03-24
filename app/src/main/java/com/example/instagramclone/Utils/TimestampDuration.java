package com.example.instagramclone.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class TimestampDuration {

    private Long difference_In_Time;
    public TimestampDuration(Date createdDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date d2 = new Date();
        difference_In_Time = d2.getTime() - createdDate.getTime();
    }
   public Long DiffDay(){
        return (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
    }
    public Long DiffHour(){
        return (difference_In_Time / (1000 * 60 * 60)) % 24;
    }
    public Long DiffMinute(){
        return (difference_In_Time / (1000 * 60)) % 60;
    }
    public Long DiffSecond(){
        return (difference_In_Time / 1000) % 60;
    }

    @Override
    public String toString() {
        return  DiffDay()
                + " days, "
                + DiffHour()
                + " hours, "
                + DiffMinute()
                + " minutes, "
                + DiffSecond()
                + " seconds";
    }
}
