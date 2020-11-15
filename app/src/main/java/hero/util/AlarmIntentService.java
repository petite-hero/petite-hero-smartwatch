package hero.util;

import android.app.IntentService;
import android.content.Intent;

public class AlarmIntentService extends IntentService {

    public AlarmIntentService() {
        super("AlarmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Noti.createReminderNoti(this, intent.getStringExtra("message"));
    }
}