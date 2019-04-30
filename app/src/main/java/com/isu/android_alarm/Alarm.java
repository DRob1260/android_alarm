package com.isu.android_alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Alarm {

    private Date dateAndTime;
    private String message = "Alarm";
    private Boolean repeating = false;

    public Alarm(Date dateAndTime){
        this.dateAndTime = dateAndTime;
    }

    public Alarm(Date dateAndTime, String message, Boolean repeating){
        this.dateAndTime = dateAndTime;
        this.message = message;
        this.repeating = repeating;
    }

    String getTimeStr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String time = dateFormat.format(dateAndTime);
        return time;
    }

    String getDay(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        String day = dateFormat.format(dateAndTime);
        return day;
    }

    String getMessage(){
        return message;
    }

    Boolean getRepeating() {
        return repeating;
    }

}
