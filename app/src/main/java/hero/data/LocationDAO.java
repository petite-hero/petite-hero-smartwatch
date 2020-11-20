package hero.data;

import java.util.List;

import hero.util.Location;

public class LocationDAO {

    static LocationDAO instance;

    public LocationDAO(){

    }

    public static synchronized LocationDAO getInstance() {
        if (instance == null) instance = new LocationDAO();
        return instance;
    }

    public void add(){
    }

    public void saveList(List<LocationDTO> locList){
        Location.locList = locList;
    }

    public void delete(long id){
    }

}
