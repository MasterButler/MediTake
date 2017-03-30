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

    public static long getTime(String s) {
        String[] time = s.split("\\s+");
        long offset = 0;
        if(time.length == 2){
            offset = time[1].trim().toLowerCase().equals("pm") ? 12 * MILLIS_TO_HOURS : 0;
        }
        long actualTime = Long.valueOf(time[0].split(":")[0]) * MILLIS_TO_HOURS + Long.valueOf(time[0].split(":")[1]) * MILLIS_TO_MINUTES;
        return actualTime + offset;
    }

    public static String convertToReadableFormat(long nextDrinkingTime) {
        DateFormat df = new SimpleDateFormat(DEFAULT_TIME_FORMAT_PERIOD_12);
        return df.format(new Date(nextDrinkingTime));
    }

    public static String convertToNotificationFormat(long DrinkingTime){
        return "";
    }
}
