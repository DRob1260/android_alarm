package com.isu.android_alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;

public class LocationFragment extends Fragment {

    private static FloatingActionButton mSharedFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_location, container, false);

        TextView tv = (TextView) v.findViewById(R.id.locationFrag);
        tv.setText(getArguments().getString("msg"));

        FloatingActionButton fab = v.findViewById(R.id.locationFab);
        fab.setOnClickListener(view -> {
            // action on click
        });

        return v;
    }

    public static LocationFragment newInstance(String text) {

        LocationFragment f = new LocationFragment();
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
            mSharedFab.setImageResource(R.drawable.ic_add_location_24dp);
            mSharedFab.setOnClickListener((fabView) -> {
                // code
            });
        }
    }

}
