package ph.edu.mobapde.meditake.meditake.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import ph.edu.mobapde.meditake.meditake.activity.DrinkMedicineActivity;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;

public class AlarmService extends IntentService {
    private static final String ACTION_FOO = "ph.edu.mobapde.meditake.meditake.service.action.FOO";
    private static final String ACTION_BAZ = "ph.edu.mobapde.meditake.meditake.service.action.BAZ";

    private static final String EXTRA_PARAM1 = "ph.edu.mobapde.meditake.meditake.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "ph.edu.mobapde.meditake.meditake.service.extra.PARAM2";

    private PowerManager.WakeLock wakeLock;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle data = intent.getExtras();
        Schedule schedule = data.getParcelable(Schedule.TABLE);

        Intent in = new Intent(getBaseContext(), DrinkMedicineActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        in.putExtra(Schedule.TABLE, schedule);
        startActivity(in);

        Log.i("AlarmReceiver", "Completed service @ " + SystemClock.elapsedRealtime());
        AlarmReceiver.completeWakefulIntent(intent);

        wakeLock.release();
    }
}
