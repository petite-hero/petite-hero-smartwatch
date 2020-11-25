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

    public void add(LocationDTO loc){
        Location.locList.add(loc);
    }

    public void saveList(List<LocationDTO> locList){
        Location.locList = locList;
    }

    public void delete(long id){
        for (LocationDTO loc : Location.locList){
            if (loc.getId() == id){
                Location.locList.remove(loc);
                break;
            }
        }
    }

    public void update(LocationDTO location){
        int count = 0;
        for (LocationDTO loc : Location.locList){
            if (loc.getId() == location.getId()){
                Location.locList.set(count, location);
                break;
            }
            count++;
        }
    }

}
