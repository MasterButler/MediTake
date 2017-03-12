package ph.edu.mobapde.meditake.meditake.util;

import android.widget.ImageView;

import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Capsule;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.Syrup;
import ph.edu.mobapde.meditake.meditake.beans.Tablet;

/**
 * Created by Winfred Villaluna on 3/12/2017.
 */

public class MedicineInstantiatorUtil {
    public static Medicine createMedicineFromImageView(ImageView imageView){
        if(imageView.getId() == R.id.selection_capsule){
            return new Capsule();
        }else if(imageView.getId() == R.id.selection_lozenge){
            return new Tablet();
        }else if(imageView.getId() == R.id.selection_syrup){
            return new Syrup();
        }
        return null;
    }

    public static Medicine createMedicineFromString(String medicineType){
        medicineType = medicineType.toLowerCase();
        if(medicineType.equals("capsule")){
            return new Capsule();
        }else if(medicineType.equals("lozenge")){
            return new Tablet();
        }else if(medicineType.equals("syrup")){
            return new Syrup();
        }
        return null;
    }

    public static Medicine createMedicineFromClass(Class clazz){
        if(clazz.equals(Capsule.class)){
            return new Capsule();
        }else if(clazz.equals(Tablet.class)){
            return new Tablet();
        }else if(clazz.equals(Syrup.class)){
            return new Syrup();
        }
        return null;
    }
}
