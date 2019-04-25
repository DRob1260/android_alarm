package com.isu.android_alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class LocationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_location, container, false);

        TextView tv = (TextView) v.findViewById(R.id.locationFrag);
        tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static LocationFragment newInstance(String text) {

        LocationFragment f = new LocationFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

}
