package com.example.scope;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.service.dreams.DreamService;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Calculateur {
    private String longitude;
    private String latitude;
    private String altitude;
    private String objetVise;
    private String ephemeride;
    DownloadManager manager;
    private Context context;

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public void setObjectVise(String objectVise) {
        this.objetVise = objectVise;
    }

    public Calculateur(Context context){
        this.longitude="0";
        this.altitude="0";
        this.altitude="0";
        this.objetVise="";
        this.context=context;
    }

    public void getCoordObjet() {
        LocalDateTime localDateTime = LocalDateTime.now();

        //SimpleDateFormat format = new SimpleDateFormat("uuuu-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String startDate = dateTimeFormatter.format(localDateTime);
        Log.d("StartDate","Date de début : " + startDate);

        localDateTime = localDateTime.plusHours(4);
        Log.d("localDatePlus4","Date local +4 : " + dateTimeFormatter.format(localDateTime));
        String endDate = dateTimeFormatter.format(localDateTime);
        Log.d("EndDate","Date de fin : " + endDate);
        if (objetVise != null) {
            try {


                URL url = new URL("https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND="+
                        "'301'"+
                        "&OBJ_DATA='NO'&MAKE_EPHEM='YES'&EPHEM_TYPE='OBSERVER'&CENTER='coord'&SITE_COORD='"
                        +longitude +','+latitude+','+altitude +
                        "'&START_TIME='"+startDate+
                        "'&STOP_TIME='"+endDate+
                        "'&STEP_SIZE='7200'&QUANTITIES='4'&ANG_FORMAT='DEG'&APPARENT='REFRACTED'&TIME_DIGITS='SECONDS'");


                manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(url.toString());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                long reference = manager.enqueue(request);
                new CalculateurPositionObjectTask(url).execute();
// URL VALIDE : https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND=%27499%27&OBJ_DATA=%27YES%27&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=%27OBSERVER%27&CENTER=%27coord%27&SITE_COORD=%2748,59,120%27&START_TIME=%272021-12-24%27&STOP_TIME=%272021-12-25%27&STEP_SIZE=%277200%27&QUANTITIES=%274%27&ANG_FORMAT=%27DEG%27&APPARENT=%27REFRACTED%27&TIME_DIGITS=%27SECONDS%27
                Log.d("URL", "URL Valide : " + url);
                Log.d("ResultatRecherche","Donnée : " + ephemeride);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private class CalculateurPositionObjectTask extends AsyncTask<Void,Void,Void> {
        String result;
        URL url;

        public CalculateurPositionObjectTask(URL url) {
            this.url = url;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                InputStreamReader reader = new InputStreamReader(url.openStream());
                StringBuilder result = new StringBuilder();

            } catch (IOException e){
                e.printStackTrace();
                result = e.toString();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            ephemeride = result; // Ajout dans une bdd local des tableaux ? ou fichier simple ? ou tableau ? (c'est gros pour un tableau)
            super.onPostExecute(aVoid);

        }
    }

}
