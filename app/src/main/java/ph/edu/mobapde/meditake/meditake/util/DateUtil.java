package ph.edu.mobapde.meditake.meditake.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ph.edu.mobapde.meditake.meditake.R;

/**
 * Created by Winfred Villaluna on 3/18/2017.
 */

public class DateUtil {

    public static final int TIME_EARLIER = -1;
    public static final int TIME_LATER = 0;
    public static final int TIME_EQUAL = 1;

    public static long MILLIS_TO_SECONDS = 1000;
    public static long MILLIS_TO_MINUTES = 60 * 1000;
    public static long MILLIS_TO_HOURS = 60 * 60 * 1000;
    public static long MILLIS_TO_DAYS = 24 * 60 * 60 * 1000;

    private static final String DEFAULT_TIME_FORMAT_PERIOD_12 = "hh:mm a";
    private static final String DEFAULT_TIME_FORMAT_PERIOD_24 = "HH:mm";

    private static final String DEFAULT_DATETIME_FORMAT_PERIOD_12 = "MM/dd hh:mm a";
    private static final String DEFAULT_DATETIME_FORMAT_PERIOD_24 = "MM/dd HH:mm";

    public static int pickBackground(long time){
        long value = time % MILLIS_TO_DAYS;

        if(value < MILLIS_TO_HOURS * 3){
            return R.drawable.schedule_evening;
        }else if(value < MILLIS_TO_HOURS * 6){
            return R.drawable.schedule_early;
        }else if(value < MILLIS_TO_HOURS * 12){
            return R.drawable.schedule_morning;
        }else if(value < MILLIS_TO_HOURS * 18){
            return R.drawable.schedule_afternoon;
        }else{
            return R.drawable.schedule_evening;
        }
    }

    public static String parseFromLong(long time, String format){
        DateFormat df = new SimpleDateFormat(format);
        return df.format(new Date(time));
    }

    public static long parseToLong(String time, String am_pm){
        DateFormat df =  new SimpleDateFormat(DEFAULT_TIME_FORMAT_PERIOD_12);
        try{
            return df.parse(time + " " + am_pm).getTime();
        }catch (ParseException e) {
            return 0;
        }
    }

    public static long parseToLong(String time){
        DateFormat df =  new SimpleDateFormat(DEFAULT_TIME_FORMAT_PERIOD_24);
        try{
            return df.parse(time).getTime();
        }catch (ParseException e) {
            return 0;
        }
    }

    public static long getCurrentTime(){
        long value = System.currentTimeMillis();
        return value - (value%MILLIS_TO_SECONDS);
    }

    public static String getTime(long time, boolean isMilitaryTime){
        return isMilitaryTime ? parseFromLong(time, DEFAULT_TIME_FORMAT_PERIOD_24) : parseFromLong(time, DEFAULT_TIME_FORMAT_PERIOD_12);
    }

    private static long getDate(long time){
        long val = time/MILLIS_TO_DAYS;
        return val*MILLIS_TO_DAYS;
    }

    public static String getDateTime(long datetime, boolean isMilitaryTime){
        return isMilitaryTime ? parseFromLong(datetime, DEFAULT_DATETIME_FORMAT_PERIOD_24) : parseFromLong(datetime, DEFAULT_DATETIME_FORMAT_PERIOD_12);
    }

    public static long addHours(long referenceTime, long numHours){
        return referenceTime + (numHours*MILLIS_TO_HOURS);
    }

    public static int compareTime(String timeA, String timeB){
        if(timeA.toLowerCase().contains("am") || timeA.toLowerCase().contains("pm")){
            return compareTime(timeA, timeB, DEFAULT_TIME_FORMAT_PERIOD_12);
        }
        return compareTime(timeA, timeB, DEFAULT_TIME_FORMAT_PERIOD_24);
    }

    public static int compareTime(String timeA, String timeB, String format){
        long timeValueA = parseToLong(timeA, format);
        long timeValueB = parseToLong(timeB, format);
        return timeValueA < timeValueB ? TIME_EARLIER : timeValueA == timeValueB ? TIME_EQUAL : TIME_LATER;
    }

    public static long addHours(long lastTimeTaken, double hours) {
        return (long) (lastTimeTaken + hours*MILLIS_TO_HOURS);
    }

    public static String getDifferenceInMinutes(long referenceTime, long nextDrinkingTime) {
        long remainingTime = (nextDrinkingTime - referenceTime) / MILLIS_TO_MINUTES;
        Log.d("time", "currTime is " + DateUtil.getDateTime(referenceTime, false));
        Log.d("time", "futureTime is " + DateUtil.getDateTime(nextDrinkingTime, false));
        Log.d("time", "remaining time is " + remainingTime + " minutes");
        if(remainingTime == 0){
            return "0";
        }else if(remainingTime < 60) {
            return remainingTime + " minutes";
        }else if(remainingTime % 60 == 0){
            return remainingTime/60 + "hours ";
        }else{
            return (remainingTime / 60) + " hours and " + (remainingTime % 60) + " minutes";
        }
    }

    public static long addDate(long time){
        time = time % MILLIS_TO_DAYS;
        time = getDate(System.currentTimeMillis()) + time;
        return time < System.currentTimeMillis() ? time + MILLIS_TO_DAYS : time;
    }
}
