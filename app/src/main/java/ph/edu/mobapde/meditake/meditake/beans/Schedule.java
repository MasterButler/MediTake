package ph.edu.mobapde.meditake.meditake.beans;

import java.util.Date;

/**
 * Main class for maintaining the schedule of the person's dosage of the medicines.
 * @author Winfred Villaluna
 */
public class Schedule {
    public static final int TIMES_A_DAY = 0;
    public static final int EVERY_NTH_HOUR = 1;
    public static final int EVERY_DAY = 2;
    public static final int EVERY_OTHER_DAY = 3;

    public static long MILLIS_TO_SECONDS = 1000;
    public static long MILLIS_TO_MINUTES = 60 * 1000;
    public static long MILLIS_TO_HOURS = 60 * 60 * 1000;
    public static long MILLIS_TO_DAYS = 24 * 60 * 60 * 1000;

    private Medicine medicine;
    private double dosagePerDrinkingInterval;
    private double drinkingIntervals;

    private Date lastTimeTaken;

    /**
     * Operation that controls if theperson has successfuly drank the medicine.
     * Returns a false when there are values that are missing within the parameters.
     * @return
     */
    public boolean drinkMedicine(){
        if(medicine.drink(dosagePerDrinkingInterval) > Medicine.NOT_ENOUGH) {
            setLastTimeTaken(new Date(System.currentTimeMillis()));
            return true;
        }
        return false;
    }

    public void setMedicine(Medicine medicine){
        this.medicine = medicine;
    }

    public Medicine getMedicine(){
        return this.medicine;
    }

    public double getDosagePerDrinkingInterval(){
        return this.dosagePerDrinkingInterval;
    }

    public void setDosagePerDrinkingInterval(double dosagePerDrinkingInterval){
        this.dosagePerDrinkingInterval = dosagePerDrinkingInterval;
    }

    public double getDrinkingIntervals(){
        return this.drinkingIntervals;
    }

    public void setDrinkingIntervals(int value, int mode){
        switch(mode){
            case TIMES_A_DAY: drinkingIntervals = 24 / value;
                break;
            case EVERY_DAY: drinkingIntervals = 24;
                break;
            case EVERY_OTHER_DAY: drinkingIntervals = 48;
                break;
            case EVERY_NTH_HOUR: drinkingIntervals = value;
                break;
            default: drinkingIntervals = value;
                break;
        }
    }

    public Date getLastTimeTaken(){
        return this.lastTimeTaken;
    }

    private void setLastTimeTaken(Date lastTimeTaken){
        this.lastTimeTaken = lastTimeTaken;
    }
}
