package com.example.scope;

import android.service.dreams.DreamService;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Calculateur {
    private final OkHttpClient client = new OkHttpClient();
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
        this.longitude="";
        this.altitude="";
        this.altitude="";
        this.objetVise="";

    }

    public void getCoordObjet() throws IOException {
        Calendar c = Calendar.getInstance();
        Date startDate = c.getTime();
        c.add(Calendar.HOUR,4);
        Date endDate = c.getTime();

        HttpUrl.Builder httpBuilder = HttpUrl.get("https://www.google.fr/").newBuilder();
        if (objetVise != null) {
            /*httpBuilder
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
                    .addQueryParameter("CSV_FORMAT","YES");*/
            Request request = new Request.Builder().url(httpBuilder.build()).build();
            try {
                Call request2 = client.newCall(request);
                Response response = request2.execute();
                System.out.println("toto");
                //Response response = client.newCall(request).execute();
                //System.out.println(response.string());
            } catch (IOException e){
                e.printStackTrace();
            }

        }


    }
}
