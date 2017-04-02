package ph.edu.mobapde.meditake.meditake.util.instantiator;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
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
    public static Medicine createMedicineInstanceFromImageView(ImageView imageView){
        if(imageView.getId() == R.id.selection_capsule){
            return new Capsule();
        }else if(imageView.getId() == R.id.selection_lozenge){
            return new Tablet();
        }else if(imageView.getId() == R.id.selection_syrup){
            return new Syrup();
        }
        return null;
    }

    public static Medicine createMedicineInstanceFromString(String medicineType){
        medicineType = medicineType.toLowerCase();
        if(medicineType.equals(Medicine.CAPSULE)){
            return new Capsule();
        }else if(medicineType.equals(Medicine.TABLET)){
            return new Tablet();
        }else if(medicineType.equals(Medicine.SYRUP)){
            return new Syrup();
        }
        return null;
    }

    public static Medicine createMedicineInstanceFromClass(Class clazz){
        if(clazz.equals(Capsule.class)){
            return new Capsule();
        }else if(clazz.equals(Tablet.class)){
            return new Tablet();
        }else if(clazz.equals(Syrup.class)){
            return new Syrup();
        }
        return null;
    }

    public static ContentValues createCVMapFromBean(Medicine medicine){
        ContentValues cv = new ContentValues();

        cv.put(Medicine.COLUMN_BRAND_NAME, medicine.getBrandName());
        cv.put(Medicine.COLUMN_GENERIC_NAME, medicine.getGenericName());
        cv.put(Medicine.COLUMN_MEDICINE_FOR, medicine.getMedicineFor());
        cv.put(Medicine.COLUMN_AMOUNT, medicine.getAmount());
        cv.put(Medicine.COLUMN_DOSAGE, medicine.getDosage());
        cv.put(Medicine.COLUMN_MEDICINE_TYPE, medicine.getClass().getSimpleName());

        return cv;
    }

    public static Medicine createBeanFromCursor(Cursor cursor){
        Medicine medicine = MedicineInstantiatorUtil.createMedicineInstanceFromString(cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_TYPE)));
        int id = cursor.getInt(cursor.getColumnIndex(Medicine.COLUMN_ID));
        String brandName = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_BRAND_NAME));
        String genericName = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_GENERIC_NAME));
        String medicineFor = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_FOR));
        double amount = cursor.getDouble(cursor.getColumnIndex(Medicine.COLUMN_AMOUNT));
        int dosage = cursor.getInt(cursor.getColumnIndex(Medicine.COLUMN_DOSAGE));

        Log.wtf("IN SQLITE CONNECTION", "FOUND " + id);
        Log.wtf("IN SQLITE CONNECTION", "FOUND " + brandName);

        medicine.setSqlId(id);
        medicine.setBrandName(brandName);
        medicine.setGenericName(genericName);
        medicine.setMedicineFor(medicineFor);
        medicine.setAmount(amount);
        medicine.setDosage(dosage);
        Log.wtf("FULL INFO", medicine.getSqlId() + ": " + medicine.getBrandName() + ", " + medicine.getGenericName() + ", " + medicine.getMedicineFor());

        return medicine;
    }
}
