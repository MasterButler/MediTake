package ph.edu.mobapde.meditake.meditake.util;

import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.MedicineList;
import ph.edu.mobapde.meditake.meditake.beans.MedicinePlan;
import ph.edu.mobapde.meditake.meditake.beans.MedicinePlanList;

/**
 * Created by Winfred Villaluna on 4/2/2017.
 */

public class MedicinePlanInstantiatorUtil {

    public static MedicinePlanList convertMedicineToMedicinePlan(MedicineList medicineList) {
        MedicinePlanList planList = new MedicinePlanList();
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

}
