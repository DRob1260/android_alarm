package com.isu.android_alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimerFragment extends Fragment {

    private static FloatingActionButton mSharedFab;
    public int index;
    private ArrayList<Timer> timers = new ArrayList<>();
    private RecyclerView recyclerView;
    private TimerRecyclerViewAdapter mAdapter;
    private static Activity activity;
    private String message;
    public static Context timerContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timer, container, false);
        activity = getActivity();
        recyclerView = (RecyclerView) v.findViewById(R.id.timerRecyclerView);

        mAdapter = new TimerRecyclerViewAdapter(activity, timers);
        recyclerView.setAdapter(mAdapter);
        System.out.println(mAdapter.getItemCount());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        timerContext = v.getContext();

        FloatingActionButton fab = v.findViewById(R.id.timerFab);
        fab.setOnClickListener(view -> {
            buildMessageDialog();
        });

        new CountDownTimer(100 * 60 * 1000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                String timeString = ""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
                //recyclerView.addItemDecoration();
            }

            public void onFinish() {
            }
        }.start();

        return v;
    }

    private void buildMessageDialog(){
        View messageDialogView = View.inflate(activity, R.layout.timer_dialog_layout, null);
        EditText editText = (EditText) messageDialogView.findViewById(R.id.messageEditText);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);;
        EditText editText2 = (EditText) messageDialogView.findViewById(R.id.timerMins);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Timer Message");
        builder.setView(messageDialogView);
        builder.setPositiveButton("OK", (dialog, which) -> {
            message = editText.getText().toString();
            if (message.equals(""))
                message = "Timer";
            int mins = Integer.parseInt(editText2.getText().toString());

            Timer timer = new Timer(message,mins, 2.1, 2.1);
            mAdapter.add(timer);

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static TimerFragment newInstance(String text) {

        TimerFragment f = new TimerFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedFab = null; // To avoid keeping/leaking the reference of the FAB
    }

    public static void shareFab(FloatingActionButton fab) {
        if (fab == null) { // When the FAB is shared to another Fragment
            if (mSharedFab != null) {
                mSharedFab.setOnClickListener(null);
            }
            mSharedFab = null;
        }
        else {
            mSharedFab = fab;
            mSharedFab.setImageResource(R.drawable.ic_add_alert_24dp);
            mSharedFab.setOnClickListener((fabView) -> {
                // code
            });
        }
    }

}
