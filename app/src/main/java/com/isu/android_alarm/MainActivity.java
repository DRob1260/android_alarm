package com.isu.android_alarm;

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
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends FragmentActivity {

    private ViewPager pager;
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

        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(pager);

        TextView clock = (TextView) findViewById(R.id.clock);
        clock.setText(clockObj.format(new Date()));

        TextView date = (TextView) findViewById(R.id.date);
        date.setText(dateObj.format(new Date()));
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
}