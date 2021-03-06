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
        String createTable = "CREATE TABLE Task(id INTEGER NOT NULL PRIMARY KEY, name TEXT, type TEXT, detail TEXT," +
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

    public void add(TaskDTO dto, SQLiteDatabase db){
        boolean isSaveList = false;
        if (db == null) db = getWritableDatabase();
        else isSaveList = true;
        ContentValues values = new ContentValues();
        values.put("id", dto.getId());
        values.put("name", dto.getName());
        values.put("type", dto.getType());
        values.put("detail", dto.getDetail());
        values.put("from_time", dto.getFromTime().getTimeInMillis());
        values.put("to_time", dto.getToTime().getTimeInMillis());
        values.put("status", dto.getStatus());
        db.insert("Task", null, values);
        if (!isSaveList) db.close();
    }

    public void saveList(List<TaskDTO> taskList){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Task");
        onCreate(db);
        for (TaskDTO dto : taskList)
            add(dto, db);
        db.close();
    }

    public void delete(long id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Task", "id = " + id, null);
        db.close();
    }

    public List getActiveTask() {
        List<TaskDTO> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT id, name, type, detail, from_time, to_time, status FROM Task ORDER BY from_time";
        Cursor cursor = db.rawQuery(sql, null);
        Calendar now = Calendar.getInstance();
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
            if (now.getTimeInMillis() > fromTime.getTimeInMillis() && now.getTimeInMillis() < toTime.getTimeInMillis()) {
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
        Calendar now = Calendar.getInstance();
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
            if (now.getTimeInMillis() > toTime.getTimeInMillis())
                result.add(new TaskDTO(id, name, type, detail, fromTime, toTime, status));
        }
        cursor.close();
        db.close();
        return result;
    }

}
