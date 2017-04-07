package ph.edu.mobapde.meditake.meditake.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.service.AlarmReceiver;

/**
 * Created by Winfred Villaluna on 3/29/2017.
 */

public class AlarmUtil {

    public static final int REQUEST_RINGTONE = 1;
    public static AlarmManager alarmManager;

    public static void chooseRingtone(Fragment f){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone:");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALARM);
        f.startActivityForResult(intent, REQUEST_RINGTONE);
    }

    public static Uri getRingtoneUri(Context context, Intent data){
        Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        Ringtone ringtone = null;
        if (uri == null) {
            uri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE);
        }
        return uri;
    }

    public static Ringtone convertStringToRingtone(Context context, String string){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        if(volume==0)
            volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

        Uri ringtoneUri = Uri.parse(string);
        Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
        if(ringtone == null){
            ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE));
        }
        return ringtone;
    }

    public static void setAlarmForSchedule(Context context, Schedule schedule) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(Schedule.COLUMN_ID, schedule.getSqlId());
        PendingIntent pendingAlarm = PendingIntent
                .getBroadcast(
                        context,
                        schedule.getSqlId(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + DateUtil.getDelay(schedule.getNextDrinkingTime()), pendingAlarm);
    }

    public static void stopAssociatedAlarmsWithSchedule(Context context, Schedule schedule){
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(Schedule.COLUMN_ID, schedule.getSqlId());
        PendingIntent pendingAlarm = PendingIntent
                .getBroadcast(
                        context,
                        schedule.getSqlId(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        alarmManager.cancel(pendingAlarm);
    }
}
