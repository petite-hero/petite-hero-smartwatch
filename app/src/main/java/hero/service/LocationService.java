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

import hero.api.DataCallback;
import hero.api.POSTRequestSender;

public class LocationService extends Service implements DataCallback {

    public static boolean isRunning = false;
    public static boolean isEmergency = false;
    public static String childId;
    public static int interval;
    public static String ipPort;

    private static boolean isSafe(Location location){
        if (location.getLatitude() >= 10.8474 || location.getLongitude() <= 106.8026) return false;
        return true;
    }

    protected LocationManager locationManager;
    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (!isRunning) stopSelf();
            JSONObject locationJsonObj = new JSONObject();
            try {
                locationJsonObj.put("child", childId)  // test
                    .put("latitude", location.getLatitude())
                    .put("longitude", location.getLongitude())
                    // .put("provider", "gps")
                    .put("status", isSafe(location))
                    .put("time", new java.util.Date().getTime());
                Log.d("test", "gps updated");
            } catch (JSONException e){
                e.printStackTrace();
            }
            new POSTRequestSender(ipPort+"/location/current-location/"+isEmergency, locationJsonObj.toString(), LocationService.this).execute();
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };
//    LocationListener locationListenerNetwork = new LocationListener() {
//        public void onLocationChanged(Location location) {
//            if (!isRunning) stopSelf();
//            JSONObject locationJsonObj = new JSONObject();
//            try {
////                locationJsonObj.put("provider", "network")
////                        .put("latitude", Double.toString(location.getLatitude()))
////                        .put("longitude", Double.toString(location.getLongitude()));
//                locationJsonObj.put("child", 7)  // test
//                        .put("latitude", location.getLatitude())
//                        .put("longitude", location.getLongitude())
//                        .put("status", "1")
//                        .put("time", new java.util.Date().getTime());
//                Log.d("hulk", "network updated");
////                Log.d("hulk", locationJsonObj.toString());
//            } catch (JSONException e){
//                e.printStackTrace();
//            }
//            new POSTRequestSender(UriBuilder.getLocationUri()+"/addNewLocation", locationJsonObj.toString(), LocationService.this).execute();
//        }
//        public void onProviderDisabled(String provider) {}
//        public void onProviderEnabled(String provider) {}
//        public void onStatusChanged(String provider, int status, Bundle extras) {}
//    };

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval, 0, locationListenerGps);
        } catch (SecurityException e){
            e.printStackTrace();
        }
//        try{
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 0, locationListenerNetwork);
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

    @Override
    public void onDataReceiving(JSONObject data) throws Exception {
        Log.d("test", "Request response | Location service: " + data.toString());
    }
}