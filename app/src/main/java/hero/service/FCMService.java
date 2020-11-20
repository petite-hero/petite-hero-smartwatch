package hero.service;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import hero.data.LocationDAO;
import hero.data.LocationDTO;
import hero.data.QuestDAO;
import hero.data.QuestDTO;
import hero.data.TaskDAO;
import hero.data.TaskDTO;
import hero.main.MainScreenActivity;
import hero.util.Noti;
import hero.util.Util;

public class FCMService extends FirebaseMessagingService {

    public static String token = "";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //Write the token to DB every time the token is refreshed
        this.token = token;
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        // TODO: AUTOMATIC UPDATE AFTER DAY PASSED

        super.onMessageReceived(remoteMessage);

        Map<String, String> noti = remoteMessage.getData();
        TaskDAO taskDao = TaskDAO.getInstance(this);
        QuestDAO questDao = QuestDAO.getInstance(this);

        Log.d("test", "Noti received | Type:" + noti.get("title") + " | Body: " + noti.get("body") + " | Data: " + noti.get("data"));

        // =============== SILENT NOTI LISTENER ===============
        if (noti.get("title").equals("silent-noti")) {

            // ===== CRONJOB HANDLER =====

            // get new SAFE ZONE LIST from cron-job
            if (noti.get("body").equals("new-safezones")){
                // [{"date":1605805200000,"parent":2,"latitude":10.8414846,"name":"Đại học FPT TP.HCM","fromTime":"12:57:48 AM","safezoneId":20,"radius":40,"type":"None","toTime":"05:57:49 AM","longitude":106.8100464,"child":3}]
                try {
                    JSONArray jsonArr = new JSONArray(noti.get("data"));
                    List<LocationDTO> locList = new ArrayList<>();
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        long id = jsonObj.getLong("safezoneId");
                        String name = jsonObj.getString("name");
                        double latitude = jsonObj.getDouble("latitude");
                        double longitude = jsonObj.getDouble("longitude");
                        int radius = jsonObj.getInt("radius");
                        Calendar fromTime = Util.timeStrToCalendar(jsonObj.getString("fromTime"));
                        Calendar toTime = Util.timeStrToCalendar(jsonObj.getString("toTime"));
                        String type = jsonObj.getString("type");
                        locList.add(new LocationDTO(id, name, latitude, longitude, radius, fromTime, toTime, type));
                    }
                    LocationDAO.getInstance().saveList(locList);
                } catch (Exception e){
                    Log.e("error", "Error while parsing JsonObject");
                }
            }

            // get new TASK LIST from cron-job
            if (noti.get("body").equals("new-tasks")){
                try {
                    JSONArray jsonArr = new JSONArray(noti.get("data"));
                    List<TaskDTO> taskList = new ArrayList<>();
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        long id = jsonObj.getLong("taskId");
                        String name = jsonObj.getString("name");
                        Calendar fromTime = Util.timeStrToCalendar(jsonObj.getString("fromTime"));
                        Calendar toTime = Util.timeStrToCalendar(jsonObj.getString("toTime"));
                        String type = jsonObj.getString("type");
                        String description = jsonObj.getString("description");
                        taskList.add(new TaskDTO(id, name, type, description, fromTime, toTime, "ASSIGNED"));
                    }
                    taskDao.saveList(taskList);
                } catch (Exception e){
                    Log.e("error", "Error while parsing JsonObject");
                }
            }


            // ===== HOT-UPDATING DATA =====

            // turning on/off LOCATION REPORTING
            if (noti.get("body").equals("active")){
                if (!LocationService.isRunning) {
                    Intent locationIntent = new Intent(this, LocationService.class);
                    startService(locationIntent);
                }
                LocationService.isRunning = true;
            }
            if (noti.get("body").equals("inactive")) LocationService.isRunning = false;

            // turning on/off EMERGENCY MODE
            if (noti.get("body").equals("emergency")) LocationService.isEmergency = true;
            if (noti.get("body").equals("stop-emergency")) LocationService.isEmergency = false;

            // get UPDATED SAFE ZONE in the day
            if (noti.get("body").equals("updated-safezones")){
                // TODO
            }

            // get UPDATED TASK in the day
            if (noti.get("body").equals("updated-tasks")){
                // {"taskId":344,"status":"DELETED"}
                try {
                    JSONObject jsonObj = new JSONObject(noti.get("data"));
                    long id = jsonObj.getLong("taskId");
                    if (jsonObj.getString("status").equals("ASSIGNED")){
                        String name = jsonObj.getString("name");
                        String status = jsonObj.getString("status");
                        Calendar fromTime = Util.timeStrToCalendar(jsonObj.getString("fromTime"));
                        Calendar toTime = Util.timeStrToCalendar(jsonObj.getString("toTime"));
                        String type = jsonObj.getString("type");
                        String description = jsonObj.getString("description");
                        taskDao.add(new TaskDTO(id, name, type, description, fromTime, toTime, status), null);
                        Noti.createReminder(this, fromTime, "Con có công việc mới: " + name, id);
                    } else{
                        taskDao.delete(id);
                        Noti.cancelReminder(this, "", id);
                    }
                } catch (Exception e){
                    Log.e("error", "Error while parsing JsonObject");
                }
            }

            // get updated FAILED QUEST
            if (noti.get("body").equals("failed-quests")){
                try {
                    JSONObject jsonObj = new JSONObject(noti.get("data"));
                    long id = jsonObj.getLong("questId");
                    questDao.delete(id);
                } catch (Exception e){
                    Log.e("error", "Error while parsing JsonObject");
                }
            }

            // get updated SYSTEM CONFIGURATION
            if (noti.get("body").equals("updated-config")) {
                // TODO
            }

        // =============== NORMAL NOTI LISTENER ===============
        } else {

            // get new QUEST LIST from cron-job
            if (noti.get("body").equals("new-quests")){
                // [{"reward":8,"questId":70,"name":"vjvj","description":"vuv","status":"ASSIGNED"}]
                try {
                    JSONArray jsonArr = new JSONArray(noti.get("data"));
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        Log.d("test", jsonObj.toString());
                        String name = jsonObj.getString("name");
                        Noti.createNoti(this, MainScreenActivity.class, 1, "Con có nhiệm vụ: " + name);
                    }
                } catch (Exception e){
                    Log.e("error", "Error while parsing JsonObject");
                }
            }

            // get UPDATED QUEST
            if (noti.get("body").contains("nhiệm vụ mới")){
                try {
                    JSONObject jsonObj = new JSONObject(noti.get("data"));
                    long id = jsonObj.getLong("questId");
                    String name = jsonObj.getString("name");
                    String description = jsonObj.getString("description");
                    int badge = jsonObj.getInt("reward");
                    String title = "Người nhện";
                    questDao.add(new QuestDTO(id, name, badge, description, title, "assigned"), null);
                    Noti.createNoti(this, MainScreenActivity.class, 1, "Con có nhiệm vụ mới: " + name);
                } catch (Exception e){
                    Log.e("error", "Error while parsing JsonObject");
                }
            }

            // get SUCCEEDED QUEST
            if (noti.get("body").contains("đã thành công")){
                // {"questId":70,"status":"DONE"}
                try{
                    JSONObject jsonObj = new JSONObject(noti.get("data"));
                    long id = jsonObj.getLong("questId");
                    String name = questDao.get(id).getName();
                    questDao.finish(id);
                    Noti.createNoti(this, MainScreenActivity.class, 2, "Con đã hoàn thành nhiệm vụ: " + name);
                } catch (Exception e){
                    Log.e("error", "Error while parsing JsonObject");
                }
            }

        }

    }

    // get firebase device token asynchronously
    public void getDeviceToken() {
        try {
            FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.d("test", "Fetching FCM registration token failed", task.getException());
                            return;
                        } else {
                            // Get new FCM registration token
                            token = task.getResult();
                        }
                    }
                });
        } catch (Exception e) {
            Log.d("test", "===> Error at FCMService: " + e.toString());
            e.printStackTrace();
        }
    }
}
