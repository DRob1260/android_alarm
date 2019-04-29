package com.isu.android_alarm;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager pager;
    private FloatingActionButton fab;
    FloatingActionButton mSharedFab;
    BroadcastReceiver broadcastReceiver;
    private final SimpleDateFormat clockObj = new SimpleDateFormat("hh:mm");
    private final SimpleDateFormat dateObj = new SimpleDateFormat("MMMM d, yyyy");

    @Override
    public void onStart() {
        super.onStart();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    TextView clock = (TextView) findViewById(R.id.clock);
                    clock.setText(clockObj.format(new Date()));
                }
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(pager);

        TextView clock = (TextView) findViewById(R.id.clock);
        clock.setText(clockObj.format(new Date()));

        TextView date = (TextView) findViewById(R.id.date);
        date.setText(dateObj.format(new Date()));

        AlarmFragment alarmFragment = new AlarmFragment();
        TimerFragment timerFragment = new TimerFragment();
        LocationFragment locationFragment = new LocationFragment();

        mSharedFab = (FloatingActionButton) findViewById(R.id.fab);

        alarmFragment.shareFab(mSharedFab); // To init the FAB
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {  }
            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        mSharedFab.hide(); // Hide animation
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        switch (viewPager.getCurrentItem()) {
                            case 0:
                                timerFragment.shareFab(null); // Remove FAB from fragment
                                locationFragment.shareFab(null); // Remove FAB from fragment
                                alarmFragment.shareFab(mSharedFab); // Share FAB to new displayed fragment
                                break;
                            case 1:
                                alarmFragment.shareFab(null); // Remove FAB from fragment
                                locationFragment.shareFab(null); // Remove FAB from fragment
                                timerFragment.shareFab(mSharedFab); // Share FAB to new displayed fragment
                                break;
                            case 2:
                                timerFragment.shareFab(null); // Remove FAB from fragment
                                alarmFragment.shareFab(null); // Remove FAB from fragment
                                locationFragment.shareFab(mSharedFab); // Share FAB to new displayed fragment
                                break;
                            default:
                                timerFragment.shareFab(null); // Remove FAB from fragment
                                locationFragment.shareFab(null); // Remove FAB from fragment
                                alarmFragment.shareFab(mSharedFab); // Share FAB to new displayed fragment
                                break;
                        }
                        mSharedFab.show(); // Show animation
                        break;
                }
            }
        });


        /*pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));


        fab = (FloatingActionButton) findViewById(R.id.fab);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(pager);

        TextView clock = (TextView) findViewById(R.id.clock);
        clock.setText(clockObj.format(new Date()));

        TextView date = (TextView) findViewById(R.id.date);
        date.setText(dateObj.format(new Date()));*/
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return AlarmFragment.newInstance("Alarm Fragment");
                case 1: return TimerFragment.newInstance("Timer Fragment");
                case 2: return LocationFragment.newInstance("Location Fragment");
                default: return AlarmFragment.newInstance("Alarm Fragment");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override    public CharSequence getPageTitle(int position) {switch (position){
            case 0: return "Alarm";
            case 1: return "Timer";
            case 2: return "Location";
            default: return null;
            }
        }
    }

    public void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
}