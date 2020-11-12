package hero.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

import hero.main.R;

public class Noti {

    public static void createNoti(Context context, Class<?> destination, int fragmentIndex, String message){

        Intent intent = new Intent(context, destination);
        intent.putExtra("fragment", fragmentIndex);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "")
                .setSmallIcon(R.drawable.icon_face)
                .setContentTitle("Anh hùng nhỏ")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) Calendar.getInstance().getTimeInMillis(), builder.build());

    }

}
