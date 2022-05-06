package com.example.scope;



import android.Manifest;

import android.content.Context;
import android.content.pm.PackageManager;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import com.example.scope.ui.dashboard.MyLocationListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.scope.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager = null;
    public MyLocationListener myLocationListener; // écouteur
    private ActivityMainBinding binding;
    private String fournisseur;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    private static final int PERMISSIONS_REQUEST_ACCESS_INTERNET = 100;
    public static Calculator calculator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.calculator = new Calculator(getApplicationContext());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            requestPermissions(new String[]{Manifest.permission.INTERNET}, PERMISSIONS_REQUEST_ACCESS_INTERNET);
            //Après ce point, vous attendez le callback dans onRequestPermissionsResult
            checkAndRequestPermissions(); //verifie la permission
        } else {
        }
        //position update
        findLocation();
    }


    private void findLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(true);
            criteria.setBearingRequired(true);

            fournisseur = locationManager.getBestProvider(criteria, true);
            Log.d("GPS", "fournisseur : " + fournisseur);

            if(fournisseur != null)
            {
                // dernière position connue
                Location localisation = locationManager.getLastKnownLocation(fournisseur);
                myLocationListener = new MyLocationListener(this);

                if(localisation != null)
                {
                    // on notifie la localisation
                    myLocationListener.onLocationChanged(localisation);
                    calculator.setAltitude(myLocationListener.altitude);
                    calculator.setLatitude(myLocationListener.latitude);
                    calculator.setLongitude(myLocationListener.longitude);
                }

                // on configure la mise à jour automatique : au moins 10 mètres et 15 secondes
                locationManager.requestLocationUpdates(fournisseur, 15000, 2, myLocationListener);
            }
        }

    }
    private  boolean checkAndRequestPermissions() {

        List<String> listPermissionsNeeded = new ArrayList<>();
        listPermissionsNeeded.clear();
        int contact= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int contactInternet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (contact != PackageManager.PERMISSION_GRANTED || contactInternet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }







}