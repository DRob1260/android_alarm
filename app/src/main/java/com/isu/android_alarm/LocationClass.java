package com.isu.android_alarm;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class LocationClass extends Fragment {
    private boolean needToMove;

    public boolean getNeedToMove() {
        return needToMove;
    }

    public void toggleNeedToMove() {
        needToMove = !needToMove;
    }

    public void locationChanged() {
        if(!LocationFragment.initHit) {

            toggleNeedToMove();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(GPSTracker.aContext);

            // Setting Dialog Title
            alertDialog.setTitle("Congratulations you have moved");

            // Setting Dialog Message
            alertDialog.setMessage("Hit the button to restart the timer");

            alertDialog.show();

            LocationFragment.locationTimer.cancel();

        }
        LocationFragment.initHit = !LocationFragment.initHit;
    }


}
