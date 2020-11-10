package hero.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FCMService extends FirebaseMessagingService {

    public static String token = "";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //Write the token to DB every time the token is refreshed
        Log.d("test", "Token | onNewToken: " + token);
        this.token = token;
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        Map<String, String> noti = remoteMessage.getData();

        // SILENT NOTI LISTENER
        if (noti.get("title").equals("silent-noti")) {

            if (noti.get("body").equals("new-safezones")){
//                Log.d("test", noti.get("data"));
            }

            if (noti.get("body").equals("updated-safezones")){
//                Log.d("test", noti.get("data"));
            }

            if (noti.get("body").equals("active")){
//                Log.d("test", noti.get("data"));
            }

            if (noti.get("body").equals("inactive")){
//                Log.d("test", noti.get("data"));
            }

            if (noti.get("body").equals("emergency")){
//                Log.d("test", noti.get("data"));
            }

            if (noti.get("body").equals("stop-emergency")){
//                Log.d("test", noti.get("data"));
            }

            if (noti.get("body").equals("new-tasks")){
//                Log.d("test", noti.get("data"));
            }

            if (noti.get("body").equals("updated_tasks")){
//                Log.d("test", noti.get("data"));
            }

//            // turning on/off emergency mode
//            if (remoteMessage.getData().get("title").equals("emergency")) LocationService.isEmergency = true;
//            if (remoteMessage.getData().get("title").equals("stop_emergency")) LocationService.isEmergency = false;
//
//            // turning on/off location reporting
//            if (remoteMessage.getData().get("title").equals("active")){
//                if (!LocationService.isRunning) {
//                    Intent locationIntent = new Intent(this, LocationService.class);
//                    startService(locationIntent);
//                }
//                LocationService.isRunning = true;
//            }
//            if (remoteMessage.getData().get("title").equals("inactive")) LocationService.isRunning = false;
//
//            Log.d("test", "====> Here is title from Data: " + remoteMessage.getData().get("title"));
//            Log.d("test", "====> Here is body from Data: " + remoteMessage.getData().get("body"));

        // NORMAL NOTI LISTENER
        } else {
            // ignore it for now
            Log.d("test", "====> Here is title from Notification: " + remoteMessage.getNotification().getTitle());
            Log.d("test", "====> Here is body from Notification: " + remoteMessage.getNotification().getBody());
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
