package ph.edu.mobapde.meditake.meditake.beans;

import android.media.Ringtone;

/**
 * Main class for maintaining the schedule of the person's dosage of the medicines.
 * @author Winfred Villaluna
 */
public class Schedule {
    public static final String TABLE = "schedule";
    public static final String COLUMN_ID = "_scheduleId";
    //public static final String COLUMN_SCHEDULE_ID = "_schedule_id";
    public static final String COLUMN_DOSAGE_PER_DRINKING_INTERVAL = "dosagePerDrinkingInterval";
    public static final String COLUMN_MEDICINE_TO_DRINK = "medicineToDrink";
    public static final String COLUMN_DRINKING_INTERVAL = "drinkingInterval";
    public static final String COLUMN_LAST_TIME_TAKEN = "lastTimeTaken";
    public static final String COLUMN_IS_ACTIVATED = "isActivated";
    public static final String COLUMN_CUSTOM_NEXT_DRINKING_TIME = "customNextDrinkingTime";

    public static final String COLUMN_NEXT_DRINKING_TIME = "nextDrinkingTime";
    public static final String COUMNN_RINGTONE = "ringtone";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_IS_VIBRATE = "vibrate";

    public static final int TIMES_A_DAY = 0;
    public static final int EVERY_NTH_HOUR = 1;
    public static final int EVERY_DAY = 2;
    public static final int EVERY_OTHER_DAY = 3;

    //the medicine to drink
    //private Medicine medicineToDrink;
    //dosage needed to drink
    private double dosagePerDrinkingInterval;

    private int sqlId;
    private long nextDrinkingTime;
    private String label;
    private Ringtone ringtone;
    private double drinkingInterval;
    private boolean isVibrate;
    private boolean isActivated;

    public Schedule(){    }

    public Schedule(long nextDrinkingTime, String label, Ringtone ringtone, double drinkingInterval, boolean isVibrate, boolean isActivated){
        this.nextDrinkingTime = nextDrinkingTime;
        this.label = label;
        this.ringtone = ringtone;
        this.drinkingInterval = drinkingInterval;
        this.isVibrate = isVibrate;
        this.isActivated = isActivated;
    }

    public Schedule(int sqlId, long nextDrinkingTime, String label, Ringtone ringtone, double drinkingInterval, boolean isVibrate, boolean isActivated){
        this(nextDrinkingTime, label, ringtone, drinkingInterval, isVibrate, isActivated);
        this.sqlId = sqlId;
    }

    /**
     * Operation that controls if theperson has successfuly drank the medicineToDrink.
     * Returns a false when there are values that are missing within the parameters.
     * @return
     */
    public boolean drinkMedicine(){
//        if(medicineToDrink.drink(dosagePerDrinkingInterval) > Medicine.NOT_ENOUGH) {
//            setLastTimeTaken(new Date(System.currentTimeMillis()));
            return true;
//        }
//        return false;
    }

    public int getSqlId() {
        return sqlId;
    }

    public void setSqlId(int sqlId) {
        this.sqlId = sqlId;
    }

    public long getNextDrinkingTime() {
        return nextDrinkingTime;
    }

    public void setNextDrinkingTime(long nextDrinkingTime) {
        this.nextDrinkingTime = nextDrinkingTime;
    }

    public double getDrinkingInterval() {
        return drinkingInterval;
    }

    public void setDrinkingInterval(double drinkingInterval) {
        this.drinkingInterval = drinkingInterval;
    }

    public Ringtone getRingtone() {
        return ringtone;
    }

    public void setRingtone(Ringtone ringtone) {
        this.ringtone = ringtone;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public void setVibrate(boolean vibrate) {
        isVibrate = vibrate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }
}
