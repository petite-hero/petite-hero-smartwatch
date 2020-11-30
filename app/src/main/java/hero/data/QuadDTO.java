package hero.data;

public class QuadDTO {

    private double[] lats;
    private double[] lngs;

    public QuadDTO() {
        this.lats = new double[4];
        this.lngs = new double[4];
    }

    public QuadDTO(double latA, double lngA, double latB, double lngB, double latC, double lngC, double latD, double lngD) {
        this.lats = new double[]{latA, latB, latC, latD};
        this.lngs = new double[]{lngA, lngB, lngC, lngD};
    }

    public double[] getVertex(int index){
        return new double[]{this.lats[index], this.lngs[index]};
    }

    public void setVertex(double lat, double lng, int index){
        this.lats[index] = lat;
        this.lngs[index] = lng;
    }

}
