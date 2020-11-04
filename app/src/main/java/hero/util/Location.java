package hero.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Location {

    public static List<LocationDTO> locList = new ArrayList<>();
    public static int OUTER_RADIUS = 2500;
    public static final int GLOBE_RADIUS = 6371000;

    public Location(){

    }

    private static double distance(double lat1, double lon1, double lat2, double lon2) {

        // haversine formula http://www.movable-type.co.uk/scripts/latlong.html

        double dLat = (lat2 - lat1)*Math.PI/180;
        double dLon = (lon2 - lon1)*Math.PI/180;
        lat1 = lat1*Math.PI/180;
        lat2 = lat2*Math.PI/180;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return GLOBE_RADIUS * c;
    }

    public static boolean isSafe(double lat, double lng, Calendar time){

        if (locList.size() == 0) return true; // should be false

        // the child had to be in a location at the current time, check location radius
        for (LocationDTO loc : locList){
            if (loc.fromTime.before(time) && loc.toTime.after(time)){
                Log.d("test", loc.name);
                return distance(lat, lng, loc.latitude, loc.longitude) < loc.radius;
            }
        }

        // if the child did not have to be in any location at the current time, check outer radius
        for (LocationDTO loc : locList){
            if (distance(lat, lng, loc.latitude, loc.longitude) < OUTER_RADIUS) return true;
        }
        return false;

    }

}
