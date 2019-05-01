package com.isu.android_alarm;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.provider.Settings;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AlarmFragment extends Fragment {

    private RecyclerView recyclerView;

    private static Activity activity;
    private static FloatingActionButton mSharedFab;
    static final int TIME_DIALOG_ID = 1111;
    static final int CAL_DIALOG_ID = 1112;
    static final int MSG_DIALOG_ID = 1113;
    private int hr;
    private int min;
    private int year;
    private int month;
    private int day;
    private String message;
    private ArrayList<Alarm> alarms = new ArrayList<>();
    private AlarmRecyclerViewAdapter mAdapter;
    private View v;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.fragment_alarm, container, false);
        activity = getActivity();

        final Calendar c = Calendar.getInstance();
        hr = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        updateTime(hr, min);

        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        mAdapter = new AlarmRecyclerViewAdapter(activity, alarms);
        recyclerView.setAdapter(mAdapter);
        System.out.println(mAdapter.getItemCount());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        FloatingActionButton fab = v.findViewById(R.id.alarmFab);
        fab.setOnClickListener(view -> {
            createdDialog(MSG_DIALOG_ID);
            createdDialog(TIME_DIALOG_ID).show();
            createdDialog(CAL_DIALOG_ID).show();
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedFab = null; // To avoid keeping/leaking the reference of the FAB
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected Dialog createdDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(activity, timePickerListener, hr, min, false);
            case CAL_DIALOG_ID:
                return new DatePickerDialog(activity, datePickerListener, year, month, day);
            case MSG_DIALOG_ID:
                buildMessageDialog();
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            hr = hourOfDay;
            min = minutes;
            updateTime(hr, min);
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int y, int m, int d) {
            year = y;
            month = m;
            day = d;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void buildMessageDialog(){
        View messageDialogView = View.inflate(activity, R.layout.message_dialog_layout, null);
        EditText editText = (EditText) messageDialogView.findViewById(R.id.messageEditText);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        final Boolean[] checked = {false};
        CheckBox checkBox = (CheckBox) messageDialogView.findViewById(R.id.repeatingCheckbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked[0] = true;
            }
        });
        checkBox.setText("Repeat Alarm");

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Alarm Message");
        builder.setView(messageDialogView);
        builder.setView(messageDialogView);
        builder.setPositiveButton("OK", (dialog, which) -> {
            message = editText.getText().toString();
            if (message.equals(""))
                message = "Alarm";

            android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.HOUR_OF_DAY, hr);
            cal.set(Calendar.MINUTE, min);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            intent.putExtra("beginTime", cal.getTimeInMillis());
            intent.putExtra("allDay", false);
            if(checked[0])
            intent.putExtra("rrule", "FREQ=DAILY");
            intent.putExtra("endTime", cal.getTimeInMillis()+60*1000);
            intent.putExtra("title", message);
            startActivity(intent);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

            // Setting Dialog Title
            alertDialog.setTitle("Google Calendar");

            // Setting Dialog Message
            alertDialog.setMessage("Adding event to Google Calendar");

            alertDialog.show();

            LocationFragment.locationTimer.cancel();

            Alarm alarm = new Alarm(new Date(year, month, day, hr, min), message, checked[0], activity);
            mAdapter.add(alarm);

            newAlarmManager(alarm);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    private void newAlarmManager(Alarm alarm) {
       /// Calendar calendar = Calendar.getInstance();
       // calendar.setTimeInMillis(System.currentTimeMillis());
       // calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
       // calendar.set(Calendar.MINUTE, alarm.getMinute());

       // Intent notificationIntent = new Intent(activity, NotificationPublisher.class);
        //int notificationId = Integer.parseInt(alarm.getTimeStamp());
       /// notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId);
       // notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, message);

        //AlarmManager alarmMgr = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        //PendingIntent alarmIntent = PendingIntent.getBroadcast(activity,notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

       // if (alarm.getRepeating())
       //     alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
       // else alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

      //  alarm.setAlarmManager(alarmMgr);
       // alarm.setAlarmIntent(alarmIntent);
    }

    private static String utilTime(int value) {
        if (value < 10) return "0" + String.valueOf(value); else return String.valueOf(value); } private void updateTime(int hours, int mins) { String timeSet = ""; if (hours > 12) {
        hours -= 12;
        timeSet = "PM";
    } else if (hours == 0) {
        hours += 12;
        timeSet = "AM";
    } else if (hours == 12)
        timeSet = "PM";
    else
        timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        String aTime = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
        //view.setText(aTime);
    }

    public static AlarmFragment newInstance(String text) {

        AlarmFragment f = new AlarmFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

}


