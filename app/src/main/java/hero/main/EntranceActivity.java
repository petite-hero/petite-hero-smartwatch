package hero.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;

import hero.data.HttpDAO;
import hero.util.Location;
import hero.util.SPSupport;
import hero.util.Util;

public class EntranceActivity extends Activity {

    private static final String IP_PORT = "http://192.168.43.166:8080";
    private static final int INTERVAL = 5000;
    private static final int OUTER_RADIUS = 3000;

    SPSupport spSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        spSupport = new SPSupport(this);
        Intent intent;
        if (spSupport.get("child_id") == null) intent = new Intent(this, WelcomeActivity.class);
        else intent = new Intent(this, MainScreenActivity.class);
        finish();
        startActivity(intent);

        setConfig();
        loadData();

    }

    private void setConfig() {
        if (!spSupport.getBool("initialized") || true) {  // test
            spSupport.set("ip_port", IP_PORT);
            spSupport.setInt("report_interval", INTERVAL);
            spSupport.setInt("outer_radius", OUTER_RADIUS);
            spSupport.setBool("initialized", true);
        }
        Location.outerRadius = spSupport.getInt("outer_radius");
    }

    private void loadData(){

        HttpDAO httpDAO = HttpDAO.getInstance(this, spSupport.get("ip_port"));

        long lastSafeZoneUpdate = spSupport.getLong("last_safezone_update");
        if (lastSafeZoneUpdate == -1 || !Util.isToday(lastSafeZoneUpdate)){
            httpDAO.getLocList(spSupport.get("child_id"));
            spSupport.setLong("last_safezone_update", Calendar.getInstance().getTimeInMillis());
        }

        long lastTaskUpdate = spSupport.getLong("last_task_update");
        if (lastTaskUpdate == -1 || !Util.isToday(lastTaskUpdate)){
            httpDAO.getTaskList(spSupport.get("child_id"));
            spSupport.setLong("last_task_update", Calendar.getInstance().getTimeInMillis());
        }

    }

}
