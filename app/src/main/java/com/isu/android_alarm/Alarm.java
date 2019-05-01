package com.isu.android_alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Alarm {

    private Date dateAndTime;
    private String message = "Alarm";
    private Boolean repeating = false;
    private AlarmManager alarmMgr = null;
    private PendingIntent alarmIntent = null;

    public Alarm(Date dateAndTime, String message, Boolean repeating, Context context){
        this.dateAndTime = dateAndTime;
        this.message = message;
        this.repeating = repeating;
    }

    public void setAlarmManager(AlarmManager alarmMgr) {
   //     this.alarmMgr = alarmMgr;
    }

    public void setAlarmIntent(PendingIntent alarmIntent){
        //this.alarmIntent = alarmIntent;
    }

    public String getTimeStr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String time = dateFormat.format(dateAndTime);
        return time;
    }

    public String getDayStr(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        String day = dateFormat.format(dateAndTime);
        return day;
    }

    public int getHour(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("kk");
        return Integer.parseInt(dateFormat.format(dateAndTime));
    }

    public int getMinute(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm");
        return Integer.parseInt(dateFormat.format(dateAndTime));
    }

    public String getMessage(){
        return message;
    }

    public Boolean getRepeating() {
        return repeating;
    }

    public void cancelAlarm() {
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
    }

    public String getTimeStamp(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yymmddkkmm");
        return dateFormat.format(dateAndTime);
    }


}
