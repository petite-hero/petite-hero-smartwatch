package hero.data;

public class RealLocationDTO {

    public long id;
    public double latitude;
    public double longitude;
    public String provider;
    public boolean status;
    public long time;

    public RealLocationDTO(){

    }

    public RealLocationDTO(long id, double latitude, double longitude, String provider, boolean status, long time) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.provider = provider;
        this.status = status;
        this.time = time;
    }

    public RealLocationDTO(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
