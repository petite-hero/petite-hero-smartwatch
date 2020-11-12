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

    public void set(String key, String value){
        refEditor.putString(key, value);
        refEditor.apply();
    }

    public String get(String key){
        return ref.getString(key, null);
    }

}
