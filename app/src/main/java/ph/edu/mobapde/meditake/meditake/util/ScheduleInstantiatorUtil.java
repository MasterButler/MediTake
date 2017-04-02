package ph.edu.mobapde.meditake.meditake.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;

import ph.edu.mobapde.meditake.meditake.beans.Schedule;

/**
 * Created by Winfred Villaluna on 3/30/2017.
 */

public class ScheduleInstantiatorUtil {
    /*
    * CREATE TABLE schedule
    * _scheduleId PRIMARY KEY AUTOINCREMENT
    * time INTEGER NOT NULL
    * label TEXT NOT NULL
    * ringtone TEXT NOT NULL
    * drinkingIntervals INTEGER NOT NULL
    * vibrate NUMERIC NOT NULL
    * isActivated NUMERIC NOT NULL
    */
    public static ContentValues createCVMapFromBean(Schedule schedule) {
        ContentValues cv = new ContentValues();

        cv.put(Schedule.COLUMN_NEXT_DRINKING_TIME, schedule.getNextDrinkingTime());
        cv.put(Schedule.COLUMN_LABEL, schedule.getLabel());
        cv.put(Schedule.COUMNN_RINGTONE, schedule.getRingtone());
        cv.put(Schedule.COLUMN_DRINKING_INTERVAL, schedule.getDrinkingInterval());
        cv.put(Schedule.COLUMN_IS_VIBRATE, schedule.isVibrate());
        cv.put(Schedule.COLUMN_IS_ACTIVATED, schedule.isActivated());

        return cv;
    }

    public static Schedule createBeanFromCursor(Cursor cursor){
        Schedule schedule = new Schedule();

        int id = cursor.getInt(cursor.getColumnIndex(Schedule.COLUMN_ID));
        long startingTime = cursor.getLong(cursor.getColumnIndex(Schedule.COLUMN_NEXT_DRINKING_TIME));
        String label = cursor.getString(cursor.getColumnIndex(Schedule.COLUMN_LABEL));
        String ringtone = cursor.getString(cursor.getColumnIndex(Schedule.COUMNN_RINGTONE));
        long drinkingInterval = cursor.getLong(cursor.getColumnIndex(Schedule.COLUMN_DRINKING_INTERVAL));
        boolean isVibrate = cursor.getInt(cursor.getColumnIndex(Schedule.COLUMN_IS_VIBRATE)) == 1 ? true : false;
        boolean isActivated = cursor.getInt(cursor.getColumnIndex(Schedule.COLUMN_IS_ACTIVATED)) == 1 ? true : false;

        schedule.setSqlId(id);
        schedule.setNextDrinkingTime(startingTime);
        schedule.setLabel(label);
        schedule.setRingtone(ringtone);
        schedule.setDrinkingInterval(drinkingInterval);
        schedule.setVibrate(isVibrate);
        schedule.setActivated(isActivated);

        return schedule;
    }
}
