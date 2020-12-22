package hero.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

import hero.main.MainScreenActivity;
import hero.main.R;

public class Noti {

    public static void createNoti(Context context, Class<?> destination, int fragmentIndex, String message){

        Intent intent = new Intent(context, destination);
        intent.putExtra("fragment", fragmentIndex);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Anh hùng nhỏ")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) Calendar.getInstance().getTimeInMillis(), builder.build());

    }

    public static void createReminderNoti(Context context, String message){

        Intent intent = new Intent(context, MainScreenActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Anh hùng nhỏ")
                .setContentText(message)
                .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000})
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) Calendar.getInstance().getTimeInMillis(), builder.build());

    }

    public static void createReminder(Context context, Calendar time, String message, long id){
        Intent notifyIntent = new Intent(context, AlarmReceiver.class);
        notifyIntent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) id, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
    }

    public static void cancelReminder(Context context, String message, long id){
        Intent notifyIntent = new Intent(context, AlarmReceiver.class);
        notifyIntent.putExtra("message", message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) id, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}
