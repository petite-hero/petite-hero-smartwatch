package hero.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SPSupport {

    SharedPreferences ref;
    SharedPreferences.Editor refEditor;

    public SPSupport(Context context){
        ref = PreferenceManager.getDefaultSharedPreferences(context);
        refEditor = ref.edit();
    }

    public String get(String key){
        return ref.getString(key, null);
    }

    public void set(String key, String value){
        refEditor.putString(key, value);
        refEditor.apply();
    }

    public int getInt(String key){
        return ref.getInt(key, -1);
    }

    public void setInt(String key, int value){
        refEditor.putInt(key, value);
        refEditor.apply();
    }

    public long getLong(String key){
        return ref.getLong(key, -1);
    }

    public void setLong(String key, long value){
        refEditor.putLong(key, value);
        refEditor.apply();
    }

    public boolean getBool(String key){
        return ref.getBoolean(key, false);
    }

    public void setBool(String key, boolean value){
        refEditor.putBoolean(key, value);
        refEditor.apply();
    }

}
