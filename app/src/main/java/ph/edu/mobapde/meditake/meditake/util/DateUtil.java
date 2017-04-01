package ph.edu.mobapde.meditake.meditake.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    private static boolean currTimeIsAfter(long time){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        int second = mcurrentTime.get(Calendar.SECOND);
        long currTime = second * MILLIS_TO_SECONDS + minute * MILLIS_TO_MINUTES + hour * MILLIS_TO_HOURS;

        return currTime > time;
    }

    public static String format(int hourOfDay, int minute, boolean isMilitary){
        Log.wtf("TIME", "RECEIVED " + hourOfDay + ":" + minute);
        if(isMilitary){
            return hourOfDay + ":" + minute;
        }
        String period = "";
        if(hourOfDay < 12){
            period = "AM";
            hourOfDay = hourOfDay == 0 ? 12 : hourOfDay;
        }else{
            period = "PM";
            hourOfDay = hourOfDay == 12 ? 12 : hourOfDay - 12;
        }
        return String.format("%02d",hourOfDay) + ":" + String.format("%02d",minute) + " " + period;
    }

    public static long getTime(String s) {

        String[] time = s.split("\\s+");
        Log.wtf("TIME", "GOT " + s);
        long offset = 0;
        if(time.length == 2){
            offset = time[1].trim().toLowerCase().equals("pm") ? 12 * MILLIS_TO_HOURS : 0;
        }
        long actualTime = Long.valueOf(time[0].split(":")[0]) * MILLIS_TO_HOURS + Long.valueOf(time[0].split(":")[1]) * MILLIS_TO_MINUTES;
        Log.wtf("TIME PARSED", "RETURNING " + actualTime/MILLIS_TO_HOURS + ":" + actualTime%MILLIS_TO_HOURS/MILLIS_TO_MINUTES);
        return actualTime + offset;
    }

    public static long getDelay(long timeToAlarm){
        Log.wtf("CURRENT TIME", "ORIGTIME IS " + timeToAlarm + " OR " + convertToReadableFormat(timeToAlarm, false) );
        if(DateUtil.currTimeIsAfter(timeToAlarm)){
            timeToAlarm += 24*MILLIS_TO_HOURS;
        }
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        int second = mcurrentTime.get(Calendar.SECOND);
        long currTime = second * MILLIS_TO_SECONDS + minute * MILLIS_TO_MINUTES + hour * MILLIS_TO_HOURS;

        Log.wtf("CURRENT TIME", "ALRMTIME IS " + timeToAlarm + " OR " + convertToReadableFormat(timeToAlarm, false) );
        Log.wtf("CURRENT TIME", "CURRTIME IS " + currTime + " OR " + convertToReadableFormat(currTime, false) );
        return timeToAlarm-currTime;
    }

    public static String convertToReadableFormat(long nextDrinkingTime, boolean isMilitary) {
        int hours = (int) (nextDrinkingTime/MILLIS_TO_HOURS);
        int minutes = (int) (nextDrinkingTime%MILLIS_TO_HOURS/MILLIS_TO_MINUTES);
        return format(hours, minutes, isMilitary);
    }

    public static String convertToNotificationFormat(long drinkingTime){
        String message = "";
        long delay = getDelay(drinkingTime);
        Log.wtf("TIME DELAY", "DELAY IS " + delay);
        if(delay < MILLIS_TO_MINUTES){
            message = "less than a minute";
        }else if(drinkingTime < MILLIS_TO_HOURS){
            message = delay/MILLIS_TO_MINUTES + " minutes";
        }else if(drinkingTime < MILLIS_TO_DAYS){
            message = delay/MILLIS_TO_HOURS + " hours";
            message += delay/MILLIS_TO_MINUTES > 0 ? " and " + delay%MILLIS_TO_HOURS/MILLIS_TO_MINUTES + " minutes" : "";
        }
        return message;
    }


    public static int[] parseFromTimePicker(String in) {
        String[] contents = in.split("\\s+");
        int hourValue = 0;
        int minuteValue = 0;

        if(contents.length == 3){
            if(contents[2].equals("hour(s)")){
                hourValue = Integer.valueOf(contents[1]);
            }else if(contents[2].equals("minutes")){
                minuteValue = Integer.valueOf(contents[1]);
            }
        }else if(contents.length == 6){
            hourValue = Integer.valueOf(contents[1]);
            minuteValue = Integer.valueOf(contents[4]);
        }
        return new int[]{hourValue, minuteValue};
    }
}
