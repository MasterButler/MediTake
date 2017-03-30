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
        // an Intent broadcast.
        //WakeLocker.acquire(context);

        //notify user
        //MedicineUtil medicineUtil = new MedicineUtil(context);
        //Medicine toDrink = medicineUtil.getMedicine(intent.getIntExtra(context.getString(R.string.MEDICINE_ID), -1));


        //ScheduleUtil scheduleUtil = new ScheduleUtil(context);
        //Schedule schedule = scheduleUtil.getSchedule(intent.getIntExtra(context.getString(R.string.SCHEDULE_ID), -1));

        //NotificationManager notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        //Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.clock_colored);

//        //PendingIntent pendingAdd = PendingIntent.getActivity(context, 2, new Intent(context, NextActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Notification.Builder notifBuilder = new Notification.Builder(context)
//                .setContentTitle("Drink your medicine!")
//                //.setContentText("Drink " + schedule.getDosagePerDrinkingInterval() + " " + toDrink.getModifier() + " of " + toDrink.getName() + " now!")
//                .setSmallIcon(R.drawable.alarm_clock_colored)
//                .setLargeIcon(bitmap)
//                .setAutoCancel(true)
//                .setContentIntent(pendingNext);
//
//        notificationManager.notify(NOTIFICATION_ANNOUNCEMENT, notifBuilder.build());
//
//        //throw new UnsupportedOperationException("Not yet implemented");
//
//        Intent in=new Intent().setClass(context,DrinkMedicineActivity.class);
//        startActivity(in);
        Bundle data = intent.getExtras();
        Schedule schedule = data.getParcelable(Schedule.TABLE);

        startAlarmService(context, schedule);
    }

    public void startAlarmService(Context context, Schedule schedule){
        Log.wtf("RECEIVER", "RECEIVED THE ACTION");

        Intent intentNext = new Intent(context, AlarmService.class);
        intentNext.putExtra(Schedule.TABLE, schedule);
        startWakefulService(context, intentNext);
    }
}
