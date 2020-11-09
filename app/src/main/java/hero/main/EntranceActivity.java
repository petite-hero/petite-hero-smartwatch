package hero.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hero.api.DataCallback;
import hero.api.GETRequestSender;
import hero.data.TaskDAO;
import hero.data.TaskDTO;
import hero.util.Util;

public class EntranceActivity extends Activity {

    private static final String CHILD_ID = "1";
    private static final String IP_PORT = "http://10.1.129.208:8080";
    private static final int INTERVAL = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SharedPreferences ref = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent;
        if (ref.getString("child_id", null) == null) intent = new Intent(this, WelcomeActivity.class);
        else intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);

        setConfig();
        fetchTaskList();

    }

    private void setConfig(){
        SharedPreferences ref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor refEditor = ref.edit();
//        refEditor.putString("child_id", CHILD_ID);
        refEditor.putString("ip_port", IP_PORT);
        refEditor.putInt("report_interval", INTERVAL);
        refEditor.apply();
    }

    public void fetchTaskList(){
        SharedPreferences ref = PreferenceManager.getDefaultSharedPreferences(this);
        new GETRequestSender(ref.getString("ip_port", null)+"/task/list/"+ref.getString("child_id", null)+"?date="+ Util.getLongHour0()+"&provider=sw",
           new DataCallback() {
                @Override
                public void onDataReceiving(JSONObject data) throws Exception {
                    Log.d("test", "Request response: " + data.toString());
                    JSONArray rowJsonArr = data.getJSONArray("data");
                    List<TaskDTO> taskList = new ArrayList<>();
                    for (int i = 0; i < rowJsonArr.length(); i++) {
                        JSONObject rowJsonObj = rowJsonArr.getJSONObject(i);
                        long id = rowJsonObj.getLong("taskId");
                        String name = rowJsonObj.getString("name");
                        String status = rowJsonObj.getString("status");
                        Calendar fromTime = Util.timeStrToCalendar(rowJsonObj.getString("fromTime"));
                        Calendar toTime = Util.timeStrToCalendar(rowJsonObj.getString("toTime"));
                        String type = rowJsonObj.getString("type");
                        String description = rowJsonObj.getString("description");
                        taskList.add(new TaskDTO(id, name, type, description, fromTime, toTime, status));
                    }
                    Log.d("test", "1234");
                    TaskDAO.getInstance(EntranceActivity.this).saveList(taskList);
                }
            }
        ).execute();
    }

}
