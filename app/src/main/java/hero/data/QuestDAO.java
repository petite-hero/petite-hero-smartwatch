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
        String createTable = "CREATE TABLE Quest(id INTEGER NOT NULL PRIMARY KEY, name TEXT, detail TEXT, badge INTEGER)";
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

    public void saveList(List<QuestDTO> questList){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Quest");
        onCreate(db);
        int count = 0;
        for (QuestDTO dto : questList){
            ContentValues values = new ContentValues();
            values.put("id", count);
            values.put("name", dto.getName());
            values.put("detail", dto.getDetail());
            values.put("badge", dto.getBadge());
            db.insert("Quest", null, values);
            count++;
        }
        db.close();
    }

    public List getList() {
        List<QuestDTO> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT name, detail, badge FROM Quest ORDER BY id DESC";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String detail = cursor.getString(1);
            int badge = cursor.getInt(2);
            result.add(new QuestDTO(name, badge, detail));
        }
        cursor.close();
        db.close();
        return result;
    }

    public void saveListBadge(List<Integer> badgeList){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Badge");
        String createTable = "CREATE TABLE Badge(id INTEGER NOT NULL PRIMARY KEY, badge INTEGER)";
        db.execSQL(createTable);
        int count = 0;
        for (int badge : badgeList){
            ContentValues values = new ContentValues();
            values.put("id", count);
            values.put("badge", badge);
            db.insert("Badge", null, values);
            count++;
        }
        db.close();
    }

    public List getListBadge() {
        List<Integer> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT badge FROM Badge ORDER BY id DESC";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int badge = cursor.getInt(0);
            result.add(badge);
        }
        cursor.close();
        db.close();
        return result;
    }

}
