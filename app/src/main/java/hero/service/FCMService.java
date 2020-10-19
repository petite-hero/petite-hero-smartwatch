package hero.service;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //Write the token to DB every time the token is refreshed
        System.out.println("====> Here is the token: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // implement what needs to do when message arrived

        if (remoteMessage.getNotification() == null) {
            // insert code to change emergency state here
            System.out.println("====> Here is title from Data: " + remoteMessage.getData().get("title"));
            System.out.println("====> Here is body from Data: " + remoteMessage.getData().get("body"));
        } else {
            // ignore it for now
            System.out.println("====> Here is title from Notification: " + remoteMessage.getNotification().getTitle());
            System.out.println("====> Here is body from Notification: " + remoteMessage.getNotification().getBody());
        }
    }


}
