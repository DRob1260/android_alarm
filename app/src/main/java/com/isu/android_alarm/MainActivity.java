package com.isu.android_alarm;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends FragmentActivity {

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(pager);
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