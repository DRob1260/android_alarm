package com.isu.android_alarm;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static Activity activity;
    private static FloatingActionButton mSharedFab;


    static final int TIME_DIALOG_ID = 1111;
    static final int CAL_DIALOG_ID = 1112;
    private int hr;
    private int min;
    private int year;
    private int month;
    private int day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();

        View v = inflater.inflate(R.layout.fragment_alarm, container, false);

        final Calendar c = Calendar.getInstance();
        hr = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        updateTime(hr, min);

        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        List<String> input = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            input.add("Message " + i + ", every Tuesday");
        }
        mAdapter = new AlarmRecyclerViewAdapter(input);
        recyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedFab = null; // To avoid keeping/leaking the reference of the FAB
    }

    public void shareFab(FloatingActionButton fab) {
        if (fab == null) { // When the FAB is shared to another Fragment
            if (mSharedFab != null) {
                mSharedFab.setOnClickListener(null);
            }
            mSharedFab = null;
        }
        else {
            mSharedFab = fab;
            mSharedFab.setImageResource(R.drawable.ic_alarm_add_24dp);
            mSharedFab.setOnClickListener((fabView) -> {
                createdDialog(TIME_DIALOG_ID).show();
                createdDialog(CAL_DIALOG_ID).show();
            });
        }
    }

    protected Dialog createdDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(activity, timePickerListener, hr, min, false);
            case CAL_DIALOG_ID:
                return new DatePickerDialog(activity, datePickerListener, year, month, day);
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


