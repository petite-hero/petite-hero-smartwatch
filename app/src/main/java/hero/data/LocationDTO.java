package hero.data;

import java.util.Calendar;

public class LocationDTO {

    long id;
    String name;
    double latitude;
    double longitude;
    int radius;
    Calendar fromTime;
    Calendar toTime;
    String type;
    QuadDTO quad;

    public LocationDTO(){

    }

    public LocationDTO(long id, String name, double latitude, double longitude, int radius, Calendar fromTime, Calendar toTime, String type, QuadDTO quad) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.type = type;
        this.quad = quad;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Calendar getFromTime() {
        return fromTime;
    }

    public void setFromTime(Calendar fromTime) {
        this.fromTime = fromTime;
    }

    public Calendar getToTime() {
        return toTime;
    }

    public void setToTime(Calendar toTime) {
        this.toTime = toTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public QuadDTO getQuad() {
        return quad;
    }

    public void setQuad(QuadDTO quad) {
        this.quad = quad;
    }
}
