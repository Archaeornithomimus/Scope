package com.example.scope.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.scope.MainActivity;
import com.example.scope.R;
import com.example.scope.databinding.ActivityMainBinding;

public class MyLocationListener implements LocationListener{
    public String latitude;
    public String longitude;
    public String altitude;
    private Context context;

    public MyLocationListener(Context context){
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("GPS", "localisation : " + location.toString());
        String coordonnees = String.format("Latitude : %f - Longitude : %f\n", location.getLatitude(), location.getLongitude());
        Log.d("GPS", "coordonnees : " + coordonnees);
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        altitude = String.valueOf(location.getAltitude());


        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("position Update");
        // You can also include some extra data.
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
        intent.putExtra("altitude", altitude);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


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
