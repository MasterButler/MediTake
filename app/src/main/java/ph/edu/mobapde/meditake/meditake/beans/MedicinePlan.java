package ph.edu.mobapde.meditake.meditake.beans;

/**
 * Created by Winfred Villaluna on 3/30/2017.
 */

public class MedicinePlan {

    public static final String TABLE = "medcicinePlan";
    public static final String COLUMN_ID = "medicinePlanId";
    public static final String COLUMN_DOSAGE = "dosage";

    private int medicineId;
    private int dosage;

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
}
