package hero.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import hero.data.HttpDAO;
import hero.util.SPSupport;

public class EntranceActivity extends Activity {

    private static final String IP_PORT = "http://10.1.142.236:8080";
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

        HttpDAO.getInstance(this, IP_PORT).getLocList(spSupport.get("child_id"));

    }

    private void setConfig(){
        SharedPreferences ref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor refEditor = ref.edit();
        refEditor.putString("ip_port", IP_PORT);
        refEditor.putInt("report_interval", INTERVAL);
        refEditor.apply();
    }

}
