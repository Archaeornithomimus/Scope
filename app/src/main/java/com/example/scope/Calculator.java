package com.example.scope;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Calculator {
    private String longitude;
    private String latitude;
    private String altitude;
    private int objetVise;
    public Boolean trackingOn;
    private Context context;
    public String[][] ephemerid = new String[7201][3];

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public void setObjectVise(int objectIndexInList) {
        switch (objectIndexInList){
            case 0 :
                //Mercury
                this.objetVise = 199;
                Log.d("object visé :","Mercury");
                break;
            case 1:
                //Venus
                this.objetVise = 299;
                Log.d("object visé :","Venus");
                break;
            case 2:
                //ISS
                this.objetVise = -125544;
                Log.d("object visé :","ISS");
                break;
            case 3:
                //Moon
                this.objetVise = 301;
                Log.d("object visé :","Moon");
                break;
            case 4:
                //Mars
                this.objetVise = 499;
                Log.d("object visé :","Mars");
                break;
            case 5:
                //Jupiter
                this.objetVise = 599;
                Log.d("object visé :","Jupiter");
                break;
            case 6:
                //Saturn
                this.objetVise = 699;
                Log.d("object visé :","Saturn");
                break;
            case 7:
                //Uranus
                this.objetVise = 799;
                Log.d("object visé :","Uranus");
                break;
            case 8:
                //Neptune
                this.objetVise = 899;
                Log.d("object visé :","Neptune");
                break;
            default:
                Log.d("object visé :","error");
                this.objetVise= -1;
                break;

        }
    }

    public Calculator(Context context) {
        this.longitude = "0";
        this.latitude = "0";
        this.altitude = "0";
        this.objetVise = -1;
        this.context = context;
    }

    public int getObjetVise() {
        return objetVise;
    }

    public void getCoordObjet() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String startDate = dateTimeFormatter.format(localDateTime);
        Log.d("StartDate", "Date de début : " + startDate);

        localDateTime = localDateTime.plusHours(4);
        Log.d("localDatePlus4", "Date local +4 : " + dateTimeFormatter.format(localDateTime));
        String endDate = dateTimeFormatter.format(localDateTime);
        Log.d("EndDate", "Date de fin : " + endDate);
        if (objetVise != -1) {
            try {
                // define cache folder
                File rootCache = context.getExternalCacheDir();
                if (rootCache == null) {
                    rootCache = context.getCacheDir();
                }
                File cacheDir = new File(rootCache, "/");
                cacheDir.mkdirs();

                DiskBasedCache cache = new DiskBasedCache(rootCache, 1024 * 1024); // 1MB cap
                Network network = new BasicNetwork(new HurlStack());

                RequestQueue queue = new RequestQueue(cache,network);

                URL url = new URL("https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND='" +
                        String.valueOf(objetVise)+
                        "'&OBJ_DATA='NO'&MAKE_EPHEM='YES'&EPHEM_TYPE='OBSERVER'&CENTER='coord'&SITE_COORD='"
                        + longitude + ',' + latitude + ',' + altitude +
                        "'&START_TIME='" + startDate +
                        "'&STOP_TIME='" + endDate +
                        "'&STEP_SIZE='7200'&QUANTITIES='4'&ANG_FORMAT='DEG'&APPARENT='REFRACTED'&TIME_DIGITS='SECONDS'");
                //String url = "https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND=%27499%27&OBJ_DATA=%27YES%27&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=%27OBSERVER%27&CENTER=%27coord%27&SITE_COORD=%2748,59,120%27&START_TIME=%272021-12-24%27&STOP_TIME=%272021-12-25%27&STEP_SIZE=%277200%27&QUANTITIES=%274%27&ANG_FORMAT=%27DEG%27&APPARENT=%27REFRACTED%27&TIME_DIGITS=%27SECONDS%27";

                Log.d("URL", "URL Valide : " + url);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url.toString(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                int ephemeridStart = response.indexOf("$$SOE")+7;
                                int ephemeridStop = response.indexOf("$$EOE")-1;
                                String test = response.substring(ephemeridStart,ephemeridStop);
                                test = test.replace("   "," ").replace("  "," ").replace("\n ","|").replace(" ",";");
                                /*test = test.replace("  "," ");
                                test = test.replace("\n ","|");
                                test = test.replace(" ",";");*/

                                String[] lines = test.split("\\|");
                                for(int i=0;i<7201;i++){
                                    String[] line = lines[i].split(";");
                                    //2021-Dec-24 00:00:00.000


                                    String date = line[0]+" " +line[1] + " Z";
                                    /*DateTimeFormatter inputFormatTime = DateTimeFormatter.ofPattern("yyyy-LLL-dd HH:mm:ss.SSS X", Locale.US);
                                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(date,inputFormatTime);
                                    Log.d("Date au format",zonedDateTime.toString());*/

                                    /*try{
                                        Date dateDate = inputFormat.parse(date);
                                        String stringDate = outputFormat.format(dateDate);
                                        Log.d("Date au format :",stringDate);
                                        ephemerid[i][0] = stringDate;
                                    } catch (ParseException e){
                                        e.getMessage();
                                    }*/
                                    ephemerid[i][0] = date;
                                    ephemerid[i][1] = line[3];
                                    ephemerid[i][2] = line[4];
                                    /*ephemerid[i][0] = line[0];
                                    ephemerid[i][1] = line[1];
                                    ephemerid[i][2] = line[3];
                                    ephemerid[i][3] = line[4];*/
                                }

                                for (int i=0;i<7201;i++){
                                    Log.d("Table Line",ephemerid[i][0] + ephemerid[i][1] + ephemerid[i][2]);
                                }
                                Log.d("Ephemerid status :", "File acquired");
                                trackingOn=true;
                                trackingTask(objetVise);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Request Error", error.getMessage());
                    }
                });
                queue.add(stringRequest);
                queue.start();
            } catch (Exception e) {
                e.getMessage();
            }
// URL VALIDE : https://ssd.jpl.nasa.gov/api/horizons.api?format=text&COMMAND=%27499%27&OBJ_DATA=%27YES%27&MAKE_EPHEM=%27YES%27&EPHEM_TYPE=%27OBSERVER%27&CENTER=%27coord%27&SITE_COORD=%2748,59,120%27&START_TIME=%272021-12-24%27&STOP_TIME=%272021-12-25%27&STEP_SIZE=%277200%27&QUANTITIES=%274%27&ANG_FORMAT=%27DEG%27&APPARENT=%27REFRACTED%27&TIME_DIGITS=%27SECONDS%27

        }
    }

    public void startTracking(int objectIndexInList){
        if (objetVise==-1){
            resetTracking();
        }
        setObjectVise(objectIndexInList);
        getCoordObjet();
    }

    public void stopTracking(){
        trackingOn = false;
        resetTracking();
    }

    public void resetTracking(){
        Log.d("coordonnée Send : ", "0 et 0");
    }

    public void trackingTask(int objetVise){
        int sightObject = objetVise;
        double azimuth = 12; // change to string maybe
        double elevation = 15;
        int ephemeridLine = findFirstRow();
        azimuth = Double.parseDouble(ephemerid[ephemeridLine][1]);
        elevation = Double.parseDouble(ephemerid[ephemeridLine][2]);
        Log.d("Send azimuth : ", String.valueOf(azimuth));
        Log.d("Send elevation: ", String.valueOf(elevation));
        while (trackingOn){
            if (sightObject != this.objetVise){
                trackingOn = false;
                stopTracking();
            }
            ZonedDateTime actualTime = LocalDateTime.now().atZone(ZoneId.of("UTC"));


            if (ephemeridLine+1<ephemerid.length-2){
                String evaluateNextEphemeridDateString = ephemerid[ephemeridLine+1][0];
                DateTimeFormatter inputFormatTime = DateTimeFormatter.ofPattern("yyyy-LLL-dd HH:mm:ss.SSS X", Locale.US);
                ZonedDateTime evaluateNextEphemeridDate = ZonedDateTime.parse(evaluateNextEphemeridDateString,inputFormatTime);

                //Log.d("Evaluate date Time", evaluateNextEphemeridDate.toString());
                //Log.d("Actual Time", actualTime.toString());

                if (evaluateNextEphemeridDate.isBefore(actualTime)){
                    ephemeridLine= ephemeridLine+1;
                    azimuth = Double.parseDouble(ephemerid[ephemeridLine][1]);
                    elevation = Double.parseDouble(ephemerid[ephemeridLine][2]);
                    Log.d("Nouvelle Ligne", String.valueOf(ephemeridLine+1));
                    Log.d("Send azimuth : ", String.valueOf(azimuth));
                    Log.d("Send elevation: ", String.valueOf(elevation));
                }

            } else{
                stopTracking();
            }



            /*  Current Date and Hour
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            String startDate = dateTimeFormatter.format(localDateTime);
            Log.d("StartDate", "Date de début : " + startDate);
            */
        }

    }




    public int findFirstRow(){
        int row = 0;
        for (int i=0;i< ephemerid.length-1;i++){
            ZonedDateTime actualTime = LocalDateTime.now().atZone(ZoneId.of("UTC"));
            //Log.d("Actual Time", actualTime.toString());

            String evaluateEphemeridDateString = ephemerid[i][0];
            DateTimeFormatter inputFormatTime = DateTimeFormatter.ofPattern("yyyy-LLL-dd HH:mm:ss.SSS X", Locale.US);
            ZonedDateTime evaluateEphemeridDate = ZonedDateTime.parse(evaluateEphemeridDateString,inputFormatTime);

            if(evaluateEphemeridDate.isAfter(actualTime) && i!=ephemerid.length-2){
                String evaluateNextEphemeridDateString = ephemerid[i+1][0];
                ZonedDateTime evaluateNextEphemeridDate = ZonedDateTime.parse(evaluateNextEphemeridDateString,inputFormatTime);

                if (evaluateNextEphemeridDate.isBefore(actualTime)){
                    Log.d("start line", String.valueOf(i));
                    row = i;
                    return row;
                }
            }
        }
        return row;
    }
}
