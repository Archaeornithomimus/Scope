package com.example.scope.ui.dashboard;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MyLocationListener implements LocationListener{
    @Override
    public void onLocationChanged(Location location) {
        Log.d("GPS", "localisation : " + location.toString());
        String coordonnees = String.format("Latitude : %f - Longitude : %f\n", location.getLatitude(), location.getLongitude());
        Log.d("GPS", "coordonnees : " + coordonnees);
    }

    @Override
    public void onStatusChanged(String fournisseur, int status, Bundle bundle) {
        }

    @Override
    public void onProviderEnabled(String fournisseur) {
        }

    @Override
    public void onProviderDisabled(String s) {

    }
}
