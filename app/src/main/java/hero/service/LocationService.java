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
import hero.api.UriBuilder;

public class LocationService extends Service implements DataCallback {

    public static boolean isRunning = false;
    public static boolean isEmergency = false;

    protected LocationManager locationManager;
    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (!isRunning) stopSelf();
            JSONObject locationJsonObj = new JSONObject();
            try {
//                locationJsonObj.put("provider", "gps")
//                        .put("latitude", Double.toString(location.getLatitude()))
//                        .put("longitude", Double.toString(location.getLongitude()))
//                        .put("time", new java.util.Date().getTime());
                locationJsonObj.put("child", 3)  // test
                        .put("latitude", location.getLatitude())
                        .put("longitude", location.getLongitude())
                        .put("status", true)
                        .put("time", new java.util.Date().getTime());
                Log.d("hulk", "gps updated");
            } catch (JSONException e){
                e.printStackTrace();
            }
            new POSTRequestSender(UriBuilder.getUri()+"/location/current-location/"+!isEmergency, locationJsonObj.toString(), null).execute();
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListenerGps);
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
//        Log.d("hulk", data.toString());
    }
}