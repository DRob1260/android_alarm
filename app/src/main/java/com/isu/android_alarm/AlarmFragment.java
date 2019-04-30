package com.isu.android_alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private AlarmRecyclerViewAdapter mAdapter = new AlarmRecyclerViewAdapter(alarms);
    private View v;
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
        //recyclerView.setHasFixedSize(true);

        alarms.add(new Alarm(new Date(1999,1,26, 12, 00), "Hello"));
        mAdapter = new AlarmRecyclerViewAdapter(alarms);
        recyclerView.setAdapter(mAdapter);
        System.out.println(mAdapter.getItemCount());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            createdDialog(MSG_DIALOG_ID);
            createdDialog(TIME_DIALOG_ID).show();
            createdDialog(CAL_DIALOG_ID).show();
        });

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedFab = null; // To avoid keeping/leaking the reference of the FAB
    }

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

    private void buildMessageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Alarm Message");

        // Set up the input
        final EditText input = new EditText(activity);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            message = input.getText().toString();
            if (message.equals(""))
                message = "Alarm";

            System.out.println("Year: " + year);
            System.out.println("Month: " + month);
            System.out.println("Day: " + day);
            System.out.println("Hour: " + hr);
            System.out.println("Minute: " + min);
            System.out.println("Message: " + message);
            Alarm alarm = new Alarm(new Date(year, month, day, hr, min), message);

            mAdapter.add(alarm);
            System.out.println("Size of alarms" + mAdapter.getItemCount());
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
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


