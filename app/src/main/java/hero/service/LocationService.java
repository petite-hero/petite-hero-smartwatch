package hero.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import hero.api.DataCallback;
import hero.api.HttpRequestSender;
import hero.data.LocationDAO;
import hero.util.SPSupport;

public class LocationService extends Service {

    public static boolean isRunning = false;
    public static boolean isEmergency = false;
    private static boolean isUsingNetwork = true;  // test
    SPSupport spSupport;

    protected LocationManager locationManager;
    LocationListener locationListenerGps;
    LocationListener locationListenerNetwork;

    @Override
    public void onCreate() {

        super.onCreate();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        spSupport = new SPSupport(this);

        LocationDAO locDao = LocationDAO.getInstance(this);
        hero.util.Location.locList = locDao.getList();
        hero.util.Location.outerRadius = spSupport.getInt("outer_radius");

        // GPS listener
        locationListenerGps = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (!isRunning){
                    LocationService.this.stopSelf();
                    return;
                }
                handleLocationChanged(location, "gps");
            }
            public void onProviderDisabled(String provider) {}
            public void onProviderEnabled(String provider) {}
            public void onStatusChanged(String provider, int status, Bundle extras) {}
        };
        // network listener
        locationListenerNetwork = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (!isRunning){
                    LocationService.this.stopSelf();
                    return;
                }
                handleLocationChanged(location, "network");
            }
            public void onProviderDisabled(String provider) {}
            public void onProviderEnabled(String provider) {}
            public void onStatusChanged(String provider, int status, Bundle extras) {}
        };

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // start GPS listener
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, spSupport.getInt("report_interval"), 0, locationListenerGps);
        } catch (SecurityException e){
            e.printStackTrace();
        }
        // start network listener
        if (isUsingNetwork) {
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, spSupport.getInt("report_interval"), 0, locationListenerNetwork);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        // set isRunning
        isRunning = true;
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(locationListenerGps);
        if (isUsingNetwork) locationManager.removeUpdates(locationListenerNetwork);
        isRunning = false;
    }

    private void handleLocationChanged(Location location, String provider){

        Log.d("test", "Location: " + location.getLatitude() + " " + location.getLongitude());

        // build json object
        Calendar calendarNow = Calendar.getInstance();
        boolean status = hero.util.Location.isSafe(location.getLatitude(), location.getLongitude(), calendarNow);
        Log.d("test", "Time: " + (Calendar.getInstance().getTimeInMillis() - calendarNow.getTimeInMillis()) + " " + status);
        JSONObject locationJsonObj = new JSONObject();
        try {
            locationJsonObj.put("child", spSupport.get("child_id"))
                    .put("latitude", location.getLatitude())
                    .put("longitude", location.getLongitude())
                    .put("provider", provider)
                    .put("status", status)
                    .put("time", calendarNow.getTimeInMillis());
            Log.d("test", "Location updated | Provider: " + provider + " | Status: " + status + " | Emergency: " + isEmergency);
        } catch (JSONException e){
            e.printStackTrace();
        }

        // send POST request
        new HttpRequestSender("POST", spSupport.get("ip_port")+"/location/current-location/"+isEmergency, locationJsonObj.toString(),
            new DataCallback() {
                @Override
                public void onDataReceiving(JSONObject data) throws Exception {
                    Log.d("test", "Request response | Location service: " + data.toString());
                }
            }
        ).execute();

    }

}