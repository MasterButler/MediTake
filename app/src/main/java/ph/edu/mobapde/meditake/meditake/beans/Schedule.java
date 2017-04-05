package ph.edu.mobapde.meditake.meditake.beans;

import android.media.Ringtone;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Main class Schedule  maintaining the schedule of the person's dosage of the medicines.
 * @author Winfred Villaluna
 */
public class Schedule implements Parcelable {
    public static final String TABLE = "schedule";
    public static final String COLUMN_ID = "_scheduleId";
    public static final String COLUMN_DRINKING_INTERVAL = "drinkingInterval";
    public static final String COLUMN_IS_ACTIVATED = "isActivated";

    public static final String COLUMN_NEXT_DRINKING_TIME = "nextDrinkingTime";
    public static final String COUMNN_RINGTONE = "ringtone";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_IS_VIBRATE = "vibrate";

    private double dosagePerDrinkingInterval;

    private int sqlId;
    private long nextDrinkingTime;
    private String label;
    private String ringtone;
    private long drinkingInterval;
    private boolean isVibrate;
    private boolean isActivated;
    private ArrayList<MedicinePlan> medicinePlanList;

    public Schedule(){    }

    public Schedule(long nextDrinkingTime, String label, String ringtone, long drinkingInterval, boolean isVibrate, boolean isActivated){
        this.nextDrinkingTime = nextDrinkingTime;
        this.label = label;
        this.ringtone = ringtone;
        this.drinkingInterval = drinkingInterval;
        this.isVibrate = isVibrate;
        this.isActivated = isActivated;
    }

    public Schedule(int sqlId, long nextDrinkingTime, String label, String ringtone, long drinkingInterval, boolean isVibrate, boolean isActivated){
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

    public long getDrinkingInterval() {
        return drinkingInterval;
    }

    public void setDrinkingInterval(long drinkingInterval) {
        this.drinkingInterval = drinkingInterval;
    }

    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
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

    public ArrayList<MedicinePlan> getMedicinePlanList() {
        return medicinePlanList;
    }

    public void setMedicinePlanList(ArrayList<MedicinePlan> medicinePlanList) {
        this.medicinePlanList = medicinePlanList;
    }



    protected Schedule(Parcel in) {
        dosagePerDrinkingInterval = in.readDouble();
        sqlId = in.readInt();
        nextDrinkingTime = in.readLong();
        label = in.readString();
        ringtone = in.readString();
        drinkingInterval = in.readLong();
        isVibrate = in.readByte() != 0x00;
        isActivated = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            medicinePlanList = new ArrayList<MedicinePlan>();
            in.readList(medicinePlanList, MedicinePlan.class.getClassLoader());
        } else {
            medicinePlanList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(dosagePerDrinkingInterval);
        dest.writeInt(sqlId);
        dest.writeLong(nextDrinkingTime);
        dest.writeString(label);
        dest.writeString(ringtone);
        dest.writeLong(drinkingInterval);
        dest.writeByte((byte) (isVibrate ? 0x01 : 0x00));
        dest.writeByte((byte) (isActivated ? 0x01 : 0x00));
        if (medicinePlanList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(medicinePlanList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };
}