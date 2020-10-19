package hero.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {

    public static String token = "";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //Write the token to DB every time the token is refreshed
        Log.d("token", "====> Here is the token: " + token);
        this.token = token;
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // implement what needs to do when message arrived

        if (remoteMessage.getNotification() == null) {
            if (remoteMessage.getData().get("title").equals("emergency")) LocationService.isEmergency = true;
            if (remoteMessage.getData().get("title").equals("stop_emergency")) LocationService.isEmergency = false;
            Log.d("token", "====> Here is title from Data: " + remoteMessage.getData().get("title"));
            Log.d("token", "====> Here is body from Data: " + remoteMessage.getData().get("body"));
        } else {
            // ignore it for now
            Log.d("token", "====> Here is title from Notification: " + remoteMessage.getNotification().getTitle());
            Log.d("token", "====> Here is body from Notification: " + remoteMessage.getNotification().getBody());
        }
    }


}
