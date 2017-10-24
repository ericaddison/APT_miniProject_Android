package com.example.apt_miniproject_android;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;


public class FindNearbyActivity extends AbstractLocationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_nearby);
    }

    @Override
    protected void handleNewLocation(Location location) {
        Log.d(TAG, "handleNewLocation");
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        Log.d(TAG, "Lat:" + currentLatitude + ", Lng:" + currentLongitude);

        //Updates the TextView display with the current location
        TextView text = (TextView) findViewById(R.id.locTextView);
        text.setText("CurrLoc: " + currentLatitude + ", " + currentLongitude);
    }

}
