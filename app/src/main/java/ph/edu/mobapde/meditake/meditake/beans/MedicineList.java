package ph.edu.mobapde.meditake.meditake.beans;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Winfred Villaluna on 2/13/2017.
 */

public class MedicineList extends ArrayList<Medicine>{

    public MedicineList(){
        super();
    }

    public boolean add(Medicine medicine){

        if(super.add(medicine)){
            //<#DEBUG_AREA>
            System.out.println("AddingMedicineInfo: Medicine " + medicine.getName());
            //</#DEBUG_AREA>
            return true;
        }
        return false;
    }

    public Medicine getUsingBrandName(String brandName){
        for(Medicine inList : this){
            if(inList.getBrandName().equals(brandName)){
                return inList;
            }
        }
        return null;
    }

    public Medicine getUsingNosqlId(long nosqlId){
        for(Medicine inList : this){
            if(inList.getNosqlid() == nosqlId){
                return inList;
            }
        }
        return null;
    }

    public Medicine get(int index){
        return super.get(index);
    }

    public MedicineList getMedicines() {
        return this;
    }

    public void setMedicineList(MedicineList medicines) {
        super.clear();
        super.addAll(medicines);
    }
}
