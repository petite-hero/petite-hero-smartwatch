package hero.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class QuestDAO extends SQLiteOpenHelper {

    static QuestDAO instance;

    public QuestDAO(Context context) {
        super(context,  "smartwatch", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE Quest(id INTEGER NOT NULL PRIMARY KEY, name TEXT, detail TEXT, badge INTEGER, title TEXT, status TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS Quest");
//        onCreate(db);
    }

    public static synchronized QuestDAO getInstance(Context context) {
        if (instance == null) instance = new QuestDAO(context.getApplicationContext());
        return instance;
    }

    public void add(QuestDTO dto, SQLiteDatabase db){
        boolean isSaveList = false;
        if (db == null) db = getWritableDatabase();
        else isSaveList = true;
        ContentValues values = new ContentValues();
        values.put("id", dto.getId());
        values.put("name", dto.getName());
        values.put("detail", dto.getDetail());
        values.put("badge", dto.getBadge());
        values.put("title", dto.getTitle());
        values.put("status", dto.getStatus());
        db.insert("Quest", null, values);
        if (!isSaveList) db.close();
    }

    public void saveList(List<QuestDTO> questList){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Quest");
        onCreate(db);
        for (QuestDTO dto : questList){
            add(dto, db);
        }
        db.close();
    }

    public void delete(long id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Quest", "id = " + id, null);
        db.close();
    }

    public void finish(long id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", "done");
        db.update("Quest", values, "id = " + id, null);
        db.close();
    }

    public List getList(String status) {
        List<QuestDTO> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT id, name, detail, badge, title FROM Quest WHERE status = '" + status + "' ORDER BY id DESC";
        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String name = cursor.getString(1);
            String detail = cursor.getString(2);
            int badge = cursor.getInt(3);
            String title = cursor.getString(4);
            result.add(new QuestDTO(id, name, badge, detail, title, status));
            count++;
            if (status.equals("done") && count == 6) break;
        }
        cursor.close();
        db.close();
        return result;
    }

    public QuestDTO get(long id) {
        QuestDTO result = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT name, detail, badge, title FROM Quest WHERE id = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String detail = cursor.getString(1);
            int badge = cursor.getInt(2);
            String title = cursor.getString(3);
            result = new QuestDTO(id, name, badge, detail, title, "");
        }
        cursor.close();
        db.close();
        return result;
    }

}
