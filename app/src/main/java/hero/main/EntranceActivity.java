package hero.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.lang.reflect.Field;

import hero.util.SPSupport;

public class EntranceActivity extends Activity {

    private static final String CHILD_ID = "1";
    private static final String IP_PORT = "http://192.168.43.29:8080";
    private static final int INTERVAL = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SPSupport spSupport = new SPSupport(this);
        Intent intent;
        if (spSupport.get("child_id") == null) intent = new Intent(this, WelcomeActivity.class);
        else intent = new Intent(this, MainScreenActivity.class);
        finish();
        startActivity(intent);

        setConfig();
        setDefaultFont();

    }

    private void setConfig(){
        SharedPreferences ref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor refEditor = ref.edit();
//        refEditor.putString("child_id", CHILD_ID);
        refEditor.putString("ip_port", IP_PORT);
        refEditor.putInt("report_interval", INTERVAL);
        refEditor.apply();
    }

    private void setDefaultFont() {
        try {
            String fontPath = "test.ttf";
            final Typeface bold = Typeface.createFromAsset(getAssets(), fontPath);
            final Typeface italic = Typeface.createFromAsset(getAssets(), fontPath);
            final Typeface boldItalic = Typeface.createFromAsset(getAssets(), fontPath);
            final Typeface regular = Typeface.createFromAsset(getAssets(), fontPath);

            Field DEFAULT = Typeface.class.getDeclaredField("DEFAULT");
            DEFAULT.setAccessible(true);
            DEFAULT.set(null, regular);

            Field DEFAULT_BOLD = Typeface.class.getDeclaredField("DEFAULT_BOLD");
            DEFAULT_BOLD.setAccessible(true);
            DEFAULT_BOLD.set(null, bold);

            Field sDefaults = Typeface.class.getDeclaredField("sDefaults");
            sDefaults.setAccessible(true);
            sDefaults.set(null, new Typeface[]{
                    regular, bold, italic, boldItalic
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
