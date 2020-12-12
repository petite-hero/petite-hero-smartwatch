package hero.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hero.data.LocationDTO;
import hero.data.QuadDTO;

public class Location {

    public static List<LocationDTO> locList = new ArrayList<>();
    public static int outerRadius = 3000;
    public static final int GLOBE_RADIUS = 6371000;

    public Location(){

    }

    private static double[] lngLatToXY(double lng, double lat, double midLad){
        double x = lng*Math.cos(midLad*Math.PI/180);
        double y = lat;
        return new double[]{x, y};
    }

    private static final boolean isInQuad(double lat, double lng, QuadDTO quad){

        // long lat coordinates to xy
        double[] reportedLoc = lngLatToXY(lng, lat, lat);
        double x = reportedLoc[0];
        double y = reportedLoc[1];
        double[] quadX = new double[4];
        double[] quadY = new double[4];
        for (int i = 0; i < 4; i++){
            double[] vertex = lngLatToXY(quad.getVertex(i)[1], quad.getVertex(i)[0], lat);
            quadX[i] = vertex[0];
            quadY[i] = vertex[1];
        }

        // check
        for (int i = 0; i < 4; i++){
            double x1 = quadX[i];
            double y1 = quadY[i];
            double x2 = quadX[(i+1)%4];
            double y2 = quadY[(i+1)%4];
            boolean exp = (x2-x)*(y1-y)-(x1-x)*(y2-y) > 0;
            if (!exp) return false;
        }

        return true;
    }

    // calculate distance in meter between 2 points, given latitude and longitude
    // based on haversine formula http://www.movable-type.co.uk/scripts/latlong.html
    private static double distance(double lat1, double lon1, double lat2, double lon2) {

        double dLat = (lat2 - lat1)*Math.PI/180;
        double dLon = (lon2 - lon1)*Math.PI/180;
        lat1 = lat1*Math.PI/180;
        lat2 = lat2*Math.PI/180;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return GLOBE_RADIUS * c;

    }

    // check if the current location is safe
    public static boolean isSafe(double lat, double lng, Calendar time){

        // special case: empty list
        if (locList.size() == 0) return false;

        // check if child has to be in a location | quadrilateral
        for (LocationDTO loc : locList){
            if (loc.getFromTime().before(time) && loc.getToTime().after(time)){
                return isInQuad(lat, lng, loc.getQuad());
            }
        }

        // if the child does not have to be not in any location | outer radius
        for (LocationDTO loc : locList){
            if (distance(lat, lng, loc.getLatitude(), loc.getLongitude()) < outerRadius) return true;
        }

        return false;

    }

}
