package ph.edu.mobapde.meditake.meditake.util.instantiator;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.MedicineList;
import ph.edu.mobapde.meditake.meditake.beans.MedicinePlan;

/**
 * Created by Winfred Villaluna on 4/2/2017.
 */

public class MedicinePlanInstantiatorUtil {

    public static ArrayList<MedicinePlan> convertMedicineToMedicinePlan(MedicineList medicineList) {
        ArrayList<MedicinePlan> planList = new ArrayList<>();
        for(Medicine med : medicineList){
            planList.add(convertMedicineToMedicinePlan(med));
        }
        return planList;
    }

    public static MedicinePlan convertMedicineToMedicinePlan(Medicine medicine){
        MedicinePlan medicinePlan = new MedicinePlan();
        medicinePlan.setMedicineId(medicine.getSqlId());
        medicinePlan.setDosage(medicine.getDosage());
        return medicinePlan;
    }

    public static ContentValues createCVMapFromBean(MedicinePlan medicinePlan) {
        ContentValues cv = new ContentValues();

        cv.put(MedicinePlan.COLUMN_MEDICINE_ID, medicinePlan.getMedicineId());
        cv.put(MedicinePlan.COLUMN_DOSAGE, medicinePlan.getDosage());

        return cv;
    }

    public static MedicinePlan createBeanFromCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(MedicinePlan.COLUMN_ID));
        int medicineId = cursor.getInt(cursor.getColumnIndex(MedicinePlan.COLUMN_MEDICINE_ID));
        int dosage = cursor.getInt(cursor.getColumnIndex(MedicinePlan.COLUMN_DOSAGE));

        MedicinePlan medicinePlan = new MedicinePlan(id, medicineId, dosage);

        Log.wtf("FULL INFO", id + " : " + medicineId + " : " + " dosage");

        return medicinePlan;
    }
}
