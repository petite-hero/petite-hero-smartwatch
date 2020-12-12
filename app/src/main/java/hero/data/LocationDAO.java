package hero.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hero.util.Location;

public class LocationDAO extends SQLiteOpenHelper {

    static LocationDAO instance;

    public LocationDAO(Context context) {
        super(context,  "smartwatch", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE Location(id INTEGER NOT NULL PRIMARY KEY, latitude REAL, longitude REAL," +
                " from_time INTEGER, to_time INTEGER, latA REAL, lngA REAL, latB REAL, lngB REAL, latC REAL, lngC REAL, latD REAL, lngD REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static synchronized LocationDAO getInstance(Context context) {
        if (instance == null) instance = new LocationDAO(context.getApplicationContext());
        return instance;
    }

    public void add(LocationDTO dto, SQLiteDatabase db){
        Location.locList.add(dto);
        boolean isSaveList = false;
        if (db == null) db = getWritableDatabase();
        else isSaveList = true;
        ContentValues values = new ContentValues();
        values.put("id", dto.getId());
        values.put("latitude", dto.getLatitude());
        values.put("longitude", dto.getLongitude());
        values.put("from_time", dto.getFromTime().getTimeInMillis());
        values.put("to_time", dto.getToTime().getTimeInMillis());
        values.put("latA", dto.getQuad().getVertex(0)[0]);
        values.put("lngA", dto.getQuad().getVertex(0)[1]);
        values.put("latB", dto.getQuad().getVertex(1)[0]);
        values.put("lngB", dto.getQuad().getVertex(1)[1]);
        values.put("latC", dto.getQuad().getVertex(2)[0]);
        values.put("lngC", dto.getQuad().getVertex(2)[1]);
        values.put("latD", dto.getQuad().getVertex(3)[0]);
        values.put("lngD", dto.getQuad().getVertex(3)[1]);
        db.insert("Location", null, values);
        if (!isSaveList) db.close();
    }

    public void saveList(List<LocationDTO> locList){
        Location.locList = locList;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Location");
        onCreate(db);
        for (LocationDTO dto : locList) add(dto, db);
        db.close();
    }

    public void delete(long id){
        for (LocationDTO loc : Location.locList){
            if (loc.getId() == id){
                Location.locList.remove(loc);
                break;
            }
        }
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Location", "id = " + id, null);
        db.close();
    }

    public void update(LocationDTO dto){
        int count = 0;
        for (LocationDTO loc : Location.locList){
            if (loc.getId() == dto.getId()){
                Location.locList.set(count, dto);
                break;
            }
            count++;
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("latitude", dto.getLatitude());
        values.put("longitude", dto.getLongitude());
        values.put("from_time", dto.getFromTime().getTimeInMillis());
        values.put("to_time", dto.getToTime().getTimeInMillis());
        values.put("latA", dto.getQuad().getVertex(0)[0]);
        values.put("lngA", dto.getQuad().getVertex(0)[1]);
        values.put("latB", dto.getQuad().getVertex(1)[0]);
        values.put("lngB", dto.getQuad().getVertex(1)[1]);
        values.put("latC", dto.getQuad().getVertex(2)[0]);
        values.put("lngC", dto.getQuad().getVertex(2)[1]);
        values.put("latD", dto.getQuad().getVertex(3)[0]);
        values.put("lngD", dto.getQuad().getVertex(3)[1]);
        db.update("Location", values, "id = " + dto.getId(), null);
        db.close();
    }

    public List getList() {
        List<LocationDTO> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT id, latitude, longitude, from_time, to_time, latA, lngA, latB, lngB, latC, lngC, latD, lngD FROM Location";
        Cursor cursor = db.rawQuery(sql, null);
        Calendar now = Calendar.getInstance();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            double latitude = cursor.getDouble(1);
            double longitude = cursor.getDouble(2);
            Calendar fromTime = Calendar.getInstance();
            fromTime.setTimeInMillis(cursor.getLong(3));
            Calendar toTime = Calendar.getInstance();
            toTime.setTimeInMillis(cursor.getLong(4));
            double latA = cursor.getDouble(5);
            double lngA = cursor.getDouble(6);
            double latB = cursor.getDouble(7);
            double lngB = cursor.getDouble(8);
            double latC = cursor.getDouble(9);
            double lngC = cursor.getDouble(10);
            double latD = cursor.getDouble(11);
            double lngD = cursor.getDouble(12);
            QuadDTO quad = new QuadDTO(latA, lngA, latB, lngB, latC, lngC, latD, lngD);
            result.add(new LocationDTO(id, "", latitude, longitude, -1, fromTime, toTime, "", quad));
        }
        cursor.close();
        db.close();
        return result;
    }

}
