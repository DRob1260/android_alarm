package com.isu.android_alarm;

import android.app.AlertDialog;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class Timer {
    private String message;
    private double latitude;
    private double longitude;
    private int mins;
    private CountDownTimer cdTimer;
    private String timeString;

    public Timer(String m, int min, double la, double lo) {
        message = m;
        mins = min;
        latitude = la;
        longitude = lo;

        this.cdTimer = new CountDownTimer(min * 60 * 1000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                timeString = ""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            }

            public void onFinish() {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(GPSTracker.aContext);

                // Setting Dialog Title
                alertDialog.setTitle("TIMER!");

                // Setting Dialog Message
                alertDialog.setMessage(message);

                alertDialog.show();
                cdTimer = null;
            }
        }.start();
    }

    public String getMessage() {
        return message;
    }

    public String getTimeStr() {
        return timeString;
    }

    public void CancelCountDown() {
        cdTimer.cancel();
    }

}
