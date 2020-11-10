package hero.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskDAO extends SQLiteOpenHelper {

    static TaskDAO instance;

    public TaskDAO(Context context) {
        super(context,  "smartwatch", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE Task(id TEXT NOT NULL PRIMARY KEY, name TEXT, type TEXT, detail TEXT," +
                "from_time INTEGER, to_time INTEGER, status TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS Task");
//        onCreate(db);
    }

    public static synchronized TaskDAO getInstance(Context context) {
        if (instance == null) instance = new TaskDAO(context.getApplicationContext());
        return instance;
    }

    public void saveList(List<TaskDTO> taskList){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Task");
        onCreate(db);
        for (TaskDTO dto : taskList){
            ContentValues values = new ContentValues();
            values.put("id", dto.getId());
            values.put("name", dto.getName());
            values.put("type", dto.getType());
            values.put("detail", dto.getDetail());
            values.put("from_time", dto.getFromTime().getTimeInMillis());
            values.put("to_time", dto.getToTime().getTimeInMillis());
            values.put("status", dto.getStatus());
            db.insert("Task", null, values);
        }
        db.close();
    }

    public List getActiveTask() {
        List<TaskDTO> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT id, name, type, detail, from_time, to_time, status FROM Task ORDER BY from_time";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String name = cursor.getString(1);
            String type = cursor.getString(2);
            String detail = cursor.getString(3);
            Calendar fromTime = Calendar.getInstance();
            fromTime.setTimeInMillis(cursor.getLong(4));
            Calendar toTime = Calendar.getInstance();
            toTime.setTimeInMillis(cursor.getLong(5));
            String status = cursor.getString(6);

            if (Calendar.getInstance().getTimeInMillis() > fromTime.getTimeInMillis() &&
                    Calendar.getInstance().getTimeInMillis() < toTime.getTimeInMillis()) {
                result.add(new TaskDTO(id, name, type, detail, fromTime, toTime, status));
                break;
            }
        }
        cursor.close();
        db.close();
        return result;
    }

    public List getListLate() {
        List<TaskDTO> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT id, name, type, detail, from_time, to_time, status FROM Task";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String name = cursor.getString(1);
            String type = cursor.getString(2);
            String detail = cursor.getString(3);
            Calendar fromTime = Calendar.getInstance();
            fromTime.setTimeInMillis(cursor.getLong(4));
            Calendar toTime = Calendar.getInstance();
            toTime.setTimeInMillis(cursor.getLong(5));
            String status = cursor.getString(6);

            if (Calendar.getInstance().getTimeInMillis() > toTime.getTimeInMillis())
                result.add(new TaskDTO(id, name, type, detail, fromTime, toTime, status));
        }
        cursor.close();
        db.close();
        return result;
    }

    public void delete(Context context, long id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Status", 0);
        db.delete("Task", "id = " + id, null);
        db.close();
    }



//
//    public BookDTO getDataDetail(Context context, String id) {
//        BookDTO result = null;
//        SQLiteDatabase db = this.getReadableDatabase();
//        String sql = "SELECT BookName, Description, TimeOfCreate, Status, Price FROM Book WHERE BookId = " + id;
//        Cursor cursor = db.rawQuery(sql, null);
//        if (cursor.moveToNext()) {
//            String name = cursor.getString(0);
//            String description = cursor.getString(1);
//            String timeOfCreate = cursor.getString(2);
//            boolean status = cursor.getInt(3) == 1;
//            float price = cursor.getFloat(4);
//            result = new BookDTO(id, name, description, timeOfCreate, status, price);
//        }
//        cursor.close();
//        db.close();
//        return result;
//    }
//
//    public void disable(Context context, String id){
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("Status", 0);
//        db.update("Book", values, "BookId = " + id, null);
//        db.close();
//    }
//
//    public boolean add(Context context, BookDTO dto){
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("BookId", dto.getId());
//        values.put("BookName", dto.getName());
//        values.put("Description", dto.getDescription());
//        values.put("TimeOfCreate", dto.getTimeOfCreate());
//        values.put("Status", dto.isStatus() ? 1 : 0);
//        values.put("Price", dto.getPrice());
//        int success = (int) db.insert("Book", null, values);
//        db.close();
//        return success != -1;
//    }

}
