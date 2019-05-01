package com.isu.android_alarm;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.concurrent.TimeUnit;

public class LocationFragment extends Fragment {

    private static FloatingActionButton mSharedFab;
    Button btnCancelTimer;
    Button btnShowLocation;
    public static CountDownTimer locationTimer;
    public static boolean initHit;
    EditText mEdit;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;

    // GPSTracker class
    GPSTracker gps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_location, container, false);

        try {
            if (ActivityCompat.checkSelfPermission(v.getContext(), mPermission)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnShowLocation = (Button) v.findViewById(R.id.button);
        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                initHit = true;
                gps = new GPSTracker(getView().getContext());

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(v.getContext(), "Your Location is - \nLat: "
                            + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
                mEdit = (EditText)v.findViewById(R.id.minutesBox);


                startTimer(Integer.parseInt(mEdit.getText().toString()));

            }
        });

        btnCancelTimer = (Button) v.findViewById(R.id.cancelButton);
        // show location button click event
        btnCancelTimer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            locationTimer.cancel();
                final TextView _tv = (TextView) getView().findViewById( R.id.textView2 );
                _tv.setText("Timer cancelled");

            }
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

    public void startTimer(int minutes) {
        final TextView _tv = (TextView) getView().findViewById( R.id.textView2 );
        this.locationTimer = new CountDownTimer(minutes * 60 * 1000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                _tv.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {

                _tv.setText("");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(GPSTracker.aContext);

                // Setting Dialog Title
                alertDialog.setTitle("ALARM!");

                // Setting Dialog Message
                alertDialog.setMessage("Get up and walk!");

                alertDialog.show();
                locationTimer = null;
            }
        }.start();
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
