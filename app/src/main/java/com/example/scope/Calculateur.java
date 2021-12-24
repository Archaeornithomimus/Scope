package com.example.scope;

import android.graphics.Color;
import android.os.AsyncTask;
import android.service.dreams.DreamService;
import android.util.Log;

import java.io.BufferedReader;
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

    public Calculateur(){
        this.longitude="0";
        this.altitude="0";
        this.altitude="0";
        this.objetVise="";
    }



    public void getCoordObjet() {
        LocalDateTime localDateTime = LocalDateTime.now();

        SimpleDateFormat format = new SimpleDateFormat("uuuu-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String startDate = dateTimeFormatter.format(localDateTime);
        localDateTime.plusHours(4);
        String endDate = dateTimeFormatter.format(localDateTime);
        if (objetVise != null) {
            try {
               /* HttpUrl url = new HttpUrl.Builder()
                        .scheme("https")
                        .host("ssd.jpl.nasa.gov")
                        .addPathSegment("api/horizons.api")
                        .addQueryParameter("format","text")
                        .addQueryParameter("COMMAND","moon")
                        .addQueryParameter("OBJ_DATA","NO")
                        .addQueryParameter("MAKE_EPHEM","YES")
                        .addQueryParameter("EPHEM_TYPE","OBSERVER")
                        .addQueryParameter("CENTER","coord")
                        .addQueryParameter("SITE_COORD",latitude +","+longitude+','+altitude)
                        .addQueryParameter("START_TIME", String.valueOf(startDate))
                        .addQueryParameter("STOP_TIME", String.valueOf(endDate))
                        .addQueryParameter("STEP_SIZE","7200")
                        .addQueryParameter("QUANTITIES","4")
                        .addQueryParameter("ANG_FORMAT","DEG")
                        .addQueryParameter("APPARENT","REFRACTED")
                        .addQueryParameter("TIME_DIGITS","SECONDS")
                        .build();*/

                URL url = new URL("https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND="+
                        "'499'"+
                        "&OBJ_DATA='YES'&MAKE_EPHEM='YES'&EPHEM_TYPE='OBSERVER'&CENTER='coord'&SITE_COORD='"
                        +latitude +","+longitude+','+altitude +
                        "'&START_TIME='"+startDate+
                        "'&STOP_TIME='"+endDate+
                        "'&STEP_SIZE='7200'&QUANTITIES='4'&ANG_FORMAT='DEG'&APPARENT='REFRACTED'&TIME_DIGITS='SECONDS'");
// URL VALIDE : https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND=%27499%27&OBJ_DATA=%27YES%27&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=%27OBSERVER%27&CENTER=%27coord%27&SITE_COORD=%2748,59,120%27&START_TIME=%272021-12-24%27&STOP_TIME=%272021-12-25%27&STEP_SIZE=%277200%27&QUANTITIES=%274%27&ANG_FORMAT=%27DEG%27&APPARENT=%27REFRACTED%27&TIME_DIGITS=%27SECONDS%27
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                httpURLConnection.disconnect();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
