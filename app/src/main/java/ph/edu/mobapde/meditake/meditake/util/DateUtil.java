package ph.edu.mobapde.meditake.meditake.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String parseFromTime(long time, String format){
        DateFormat df = new SimpleDateFormat(format);
        return df.format(new Date(time));
    }

    public static long parseToTime(String time, String format){
        DateFormat df =  new SimpleDateFormat(format);
        try{
            return df.parse(time).getTime();
        }catch (ParseException e) {
            return 0;
        }
    }

    public static String getTime(long time, boolean isMilitaryTime){
        return isMilitaryTime ? parseFromTime(time, DEFAULT_TIME_FORMAT_PERIOD_24) : parseFromTime(time, DEFAULT_TIME_FORMAT_PERIOD_12);
    }

    public static int compareTime(String timeA, String timeB){
        if(timeA.toLowerCase().contains("am") || timeA.toLowerCase().contains("pm")){
            return compareTime(timeA, timeB, DEFAULT_TIME_FORMAT_PERIOD_12);
        }
        return compareTime(timeA, timeB, DEFAULT_TIME_FORMAT_PERIOD_24);
    }

    public static int compareTime(String timeA, String timeB, String format){
        long timeValueA = parseToTime(timeA, format);
        long timeValueB = parseToTime(timeB, format);
        return timeValueA < timeValueB ? TIME_EARLIER : timeValueA == timeValueB ? TIME_EQUAL : TIME_LATER;
    }

    public static long addHours(long lastTimeTaken, double hours) {
        return (long) (lastTimeTaken + hours*MILLIS_TO_HOURS);
    }
}
