package ph.edu.mobapde.meditake.meditake.beans;

import java.util.Date;

/**
 * Main class for maintaining the schedule of the person's dosage of the medicines.
 * @author Winfred Villaluna
 */
public class Schedule {
    public static final String TABLE = "schedule";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DOSAGE_PER_DRINKING_INTERVAL = "dosagePerDrinkingInterval";
    public static final String COLUMN_MEDICINE_TO_DRINK = "medicineToDrink";
    public static final String COLUMN_DRINKING_INTERVAL = "drinkingInterval";
    public static final String COLUMN_LAST_TIME_TAKEN = "lastTimeTaken";

    public static final int TIMES_A_DAY = 0;
    public static final int EVERY_NTH_HOUR = 1;
    public static final int EVERY_DAY = 2;
    public static final int EVERY_OTHER_DAY = 3;

    public static long MILLIS_TO_SECONDS = 1000;
    public static long MILLIS_TO_MINUTES = 60 * 1000;
    public static long MILLIS_TO_HOURS = 60 * 60 * 1000;
    public static long MILLIS_TO_DAYS = 24 * 60 * 60 * 1000;

    //id of the medicine in the server for easier retrieval/storage when it is already present in the cache
    private int sqlId;
    //the medicine to drink
    private Medicine medicineToDrink;
    //dosage needed to drink
    private double dosagePerDrinkingInterval;
    //intervals in between drinking the medicineToDrink. stored in terms of HOURS
    private double drinkingInterval;

    private long lastTimeTaken;

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

    /**
     * returns next drinking time relative to last time taken and the interval between drinks.
     * @param
     */
    public Date getNextDrinkingTime(){
        return new Date((long) (this.lastTimeTaken + drinkingInterval * MILLIS_TO_HOURS));
    }

    public void setMedicineToDrink(Medicine medicineToDrink){
        this.medicineToDrink = medicineToDrink;
    }

    public Medicine getMedicineToDrink(){
        return this.medicineToDrink;
    }

    public double getDosagePerDrinkingInterval(){
        return this.dosagePerDrinkingInterval;
    }

    public void setDosagePerDrinkingInterval(double dosagePerDrinkingInterval){
        this.dosagePerDrinkingInterval = dosagePerDrinkingInterval;
    }

    public double getDrinkingInterval(){
        return this.drinkingInterval;
    }

    public void setDrinkingIntervals(int value, int mode){
        switch(mode){
            case TIMES_A_DAY: drinkingInterval = 24 / value;
                break;
            case EVERY_DAY: drinkingInterval = 24;
                break;
            case EVERY_OTHER_DAY: drinkingInterval = 48;
                break;
            case EVERY_NTH_HOUR: drinkingInterval = value;
                break;
            default: drinkingInterval = value;
                break;
        }
    }

    public int getSqlId() {
        return sqlId;
    }

    public void setSqlId(int sqlId) {
        this.sqlId = sqlId;
    }

    public void setDrinkingInterval(double drinkingInterval) {
        this.drinkingInterval = drinkingInterval;
    }

    public long getLastTimeTaken(){
        return this.lastTimeTaken;
    }

    public void setLastTimeTaken(long lastTimeTaken){
        this.lastTimeTaken = lastTimeTaken - (lastTimeTaken%MILLIS_TO_SECONDS);
    }
}
