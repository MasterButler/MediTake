package ph.edu.mobapde.meditake.meditake.beans;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Winfred Villaluna on 2/13/2017.
 */

public class MedicineList extends ArrayList<Medicine>{

    public Medicine getUsingBrandName(String brandName){
        for(Medicine inList : this){
            if(inList.getBrandName().equals(brandName)){
                return inList;
            }
        }
        return null;
    }

    public Medicine getUsingNosqlId(long sqlId){
        for(Medicine inList : this){
            if(inList.getSqlId() == sqlId){
                return inList;
            }
        }
        return null;
    }

    public void setMedicineList(MedicineList medicines) {
        super.clear();
        super.addAll(medicines);
    }
}
