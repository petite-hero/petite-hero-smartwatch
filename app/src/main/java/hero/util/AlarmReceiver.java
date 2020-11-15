package hero.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentTmp = new Intent(context, AlarmIntentService.class);
        intentTmp.putExtra("message", intent.getStringExtra("message"));
        context.startService(intentTmp);
    }
}