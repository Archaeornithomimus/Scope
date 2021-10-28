package com.example.scope;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class Calculateur {
    private final OkHttpClient client = new OkHttpClient();


    public Calculateur(){

    }

    public void getCoordObjet(String objet, double latPosition, double longPosition, double altitude, Callback responseCallback) throws IOException {
        Calendar c = Calendar.getInstance();
        Date startDate = c.getTime();
        c.add(Calendar.HOUR,4);
        Date endDate = c.getTime();

        HttpUrl.Builder httpBuilder = HttpUrl.parse("https://ssd.jpl.nasa.gov/api/horizons.api").newBuilder();
        if (objet != null) {
            httpBuilder
                    .addQueryParameter("format","text")
                    .addQueryParameter("COMMAND","moon")
                    .addQueryParameter("OBJ_DATA","NO")
                    .addQueryParameter("MAKE_EPHEM","YES")
                    .addQueryParameter("EPHEM_TYPE","OBSERVER")
                    .addQueryParameter("CENTER","coord")
                    .addQueryParameter("SITE_COORD",latPosition +","+longPosition+','+altitude)
                    .addQueryParameter("START_TIME", String.valueOf(startDate))
                    .addQueryParameter("STOP_TIME", String.valueOf(endDate))
                    .addQueryParameter("STEP_SIZE","7200")
                    .addQueryParameter("QUANTITIES","4")
                    .addQueryParameter("ANG_FORMAT","DEG")
                    .addQueryParameter("APPARENT","REFRACTED")
                    .addQueryParameter("TIME_DIGITS","SECONDS")
                    .addQueryParameter("CSV_FORMAT","YES");

            Request request = new Request.Builder().url(httpBuilder.build()).build();
            try {
                ResponseBody response = client.newCall(request).execute().body();
                System.out.println(response.string());
            } catch (IOException e){
                System.out.println(e.getMessage());
            }
        }


    }
}
