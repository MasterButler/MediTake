package ph.edu.mobapde.meditake.meditake.service;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.activity.DrinkMedicineActivity;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    public static final int NOTIFICATION_ANNOUNCEMENT = 0;
    public static final int PENDING_NEXTACTIVITY = 0;
    public static final int PENDING_ALARMRECEIVER = 1;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // TODO: This method is called when the BroadcastReceiver is receiving
        startAlarmService(context, new ScheduleUtil(context).getSchedule(intent.getIntExtra(Schedule.COLUMN_ID, -1)));

    }

    public void startAlarmService(Context context, Schedule schedule){
        Log.wtf("RECEIVER", "RECEIVED THE ACTION");

        Intent intentNext = new Intent(context, AlarmService.class);
        intentNext.putExtra(Schedule.TABLE, schedule);
        startWakefulService(context, intentNext);
    }
}
