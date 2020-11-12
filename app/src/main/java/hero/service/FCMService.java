package hero.service;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import hero.data.HttpDAO;
import hero.main.MainScreenActivity;
import hero.util.Noti;
import hero.util.SPSupport;

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

        super.onMessageReceived(remoteMessage);

        Map<String, String> noti = remoteMessage.getData();
        SPSupport spSupport = new SPSupport(this);
        HttpDAO httpDao = HttpDAO.getInstance(this, spSupport.get("ip_port"));

        Log.d("test", "noti received. type:" + noti.get("title"));
        Log.d("test", noti.get("body"));
        // Log.d("test", "====> Here is title from Data: " + remoteMessage.getData().get("title"));
        // Log.d("test", "====> Here is body from Data: " + remoteMessage.getData().get("body"));

        // ========== SILENT NOTI LISTENER ==========
        if (noti.get("title").equals("silent-noti")) {

            // get new SAFE ZONE LIST from cron-job
            if (noti.get("body").equals("new-safezones")){
            }

            // get UPDATED SAFE ZONE in the day
            if (noti.get("body").equals("updated-safezones")){
            }

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

            // get new TASK LIST from cron-job
            if (noti.get("body").equals("new-tasks")){

            }

            // get UPDATED TASK in the day
            if (noti.get("body").equals("updated-tasks")){
                Log.d("test", "Silent noti: Update task");
                httpDao.getTaskList(spSupport.get("child_id"));
            }

        // ========== NORMAL NOTI LISTENER ==========
        } else {

            // Log.d("test", "====> Here is title from Notification: " + remoteMessage.getNotification().getTitle());
            // Log.d("test", "====> Here is body from Notification: " + remoteMessage.getNotification().getBody());

            // get UPDATED QUEST
            if (noti.get("body").contains("nhiệm vụ mới")){
                Noti.createNoti(this, MainScreenActivity.class, 1, "Bạn có nhiệm vụ mới");
                httpDao.getQuestList(spSupport.get("child_id"));
            }

            // get SUCCEEDED QUEST
            if (noti.get("body").contains("đã thành công")){
                Noti.createNoti(this, MainScreenActivity.class, 2, "Bạn đã hoàn thành nhiệm vụ");
                httpDao.getQuestList(spSupport.get("child_id"));
                httpDao.getBadgeList(spSupport.get("child_id"));
            }

        }

    }

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
