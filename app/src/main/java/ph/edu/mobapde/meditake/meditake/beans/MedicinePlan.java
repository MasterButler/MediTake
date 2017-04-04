package ph.edu.mobapde.meditake.meditake.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Winfred Villaluna on 3/30/2017.
 */

public class MedicinePlan implements Parcelable {

    public static final String TABLE = "medcicinePlan";
    public static final String COLUMN_ID = "_medicinePlanId";
    public static final String COLUMN_MEDICINE_ID = "medicineId";
    public static final String COLUMN_DOSAGE = "dosage";

    private int sqlId;
    private int medicineId;
    private int dosage;

    public MedicinePlan(int sqlId, int medicineId, int dosage) {
        this.sqlId = sqlId;
        this.medicineId = medicineId;
        this.dosage = dosage;
    }

    public MedicinePlan(int medicineId, int dosage) {
        this.medicineId = medicineId;
        this.dosage = dosage;
    }

    public MedicinePlan(){

    }

    public int getSqlId() {
        return sqlId;
    }

    public void setSqlId(int sqlId) {
        this.sqlId = sqlId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    protected MedicinePlan(Parcel in) {
        sqlId = in.readInt();
        medicineId = in.readInt();
        dosage = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sqlId);
        dest.writeInt(medicineId);
        dest.writeInt(dosage);
    }

    public static final Parcelable.Creator<MedicinePlan> CREATOR = new Parcelable.Creator<MedicinePlan>() {
        @Override
        public MedicinePlan createFromParcel(Parcel in) {
            return new MedicinePlan(in);
        }

        @Override
        public MedicinePlan[] newArray(int size) {
            return new MedicinePlan[size];
        }
    };
}