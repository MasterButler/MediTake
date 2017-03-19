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

    private static final String DEFAULT_DATETIME_FORMAT_PERIOD_12 = "MM/dd hh:mm a";
    private static final String DEFAULT_DATETIME_FORMAT_PERIOD_24 = "MM/dd HH:mm";

    public static String parseFromLong(long time, String format){
        DateFormat df = new SimpleDateFormat(format);
        return df.format(new Date(time));
    }

    public static long parseToLong(String time, String format){
        DateFormat df =  new SimpleDateFormat(format);
        try{
            return df.parse(time).getTime();
        }catch (ParseException e) {
            return 0;
        }
    }

    public static String getTime(long time, boolean isMilitaryTime){
        return isMilitaryTime ? parseFromLong(time, DEFAULT_TIME_FORMAT_PERIOD_24) : parseFromLong(time, DEFAULT_TIME_FORMAT_PERIOD_12);
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
        if(remainingTime == 0){
            return "0";
        }else if(remainingTime % 60 == 0){
            return remainingTime/60 + "hours ";
        }else{
            return (remainingTime / 60) + " hours and " + (remainingTime % 60) + " minutes";
        }
    }
}
