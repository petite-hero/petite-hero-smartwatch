package hero.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import hero.api.DataCallback;
import hero.api.HttpRequestSender;

public class LocationService extends Service {

    public static boolean isRunning = false;
    public static boolean isEmergency = false;
    SharedPreferences ref;

//    private static boolean isSafe(Location location){
//        if (location.getLatitude() <= 10.8355) return false;
//        return true;
//    }

    protected LocationManager locationManager;
    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (!isRunning){
                stopSelf();
                return;
            }
            handleLocationChanged(location, "gps");
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };
    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (!isRunning){
                stopSelf();
                return;
            }
            handleLocationChanged(location, "network");
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    private void handleLocationChanged(Location location, String provider){
        JSONObject locationJsonObj = new JSONObject();
        try {
            Calendar calendarNow = Calendar.getInstance();
            locationJsonObj.put("child", ref.getString("child_id", null))
                    .put("latitude", location.getLatitude())
                    .put("longitude", location.getLongitude())
                    .put("provider", provider)
                    .put("status", hero.util.Location.isSafe(location.getLatitude(), location.getLongitude(), calendarNow))
                    .put("time", calendarNow.getTimeInMillis());
//            Log.d("test", "gps updated, emergency: " + isEmergency);
            Log.d("test", "gps updated, status: " + hero.util.Location.isSafe(location.getLatitude(), location.getLongitude(), calendarNow));
        } catch (JSONException e){
            e.printStackTrace();
        }
        new HttpRequestSender("POST", ref.getString("ip_port", null)+"/location/current-location/"+isEmergency, locationJsonObj.toString(),
//        new HttpRequestSender("POST", ref.getString("ip_port", null) + "/location/current-location/true", locationJsonObj.toString(),
            new DataCallback() {
                @Override
                public void onDataReceiving(JSONObject data) throws Exception {
                    Log.d("test", "Request response | Location service: " + data.toString());
                }
            }
        ).execute();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ref = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, ref.getInt("report_interval", 0), 0, locationListenerGps);
        } catch (SecurityException e){
            e.printStackTrace();
        }
//        try{
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, ref.getInt("report_interval", 0), 0, locationListenerNetwork);
//        } catch (SecurityException e){
//            e.printStackTrace();
//        }
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
//        locationManager.removeUpdates(locationListenerNetwork);
        isRunning = false;
    }

}