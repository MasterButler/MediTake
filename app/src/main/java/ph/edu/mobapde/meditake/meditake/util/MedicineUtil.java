package ph.edu.mobapde.meditake.meditake.util;

import android.content.Context;

import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.db.SQLiteConnection;

/**
 * Created by Winfred Villaluna on 3/12/2017.
 */

public class MedicineUtil {
    SQLiteConnection databaseConnection;

    public void initializeDBConnection(Context context){
        databaseConnection = new SQLiteConnection(context);
    }

    public int addMedicine(Context context, Medicine medicine){
        return databaseConnection.createMedicine(medicine);
    }

    public static void setMedicineInfo(Medicine medicine, String brandName, String genericName, String medicineFor, double amount){
        medicine.setBrandName(brandName);
        medicine.setGenericName(genericName);
        medicine.setMedicineFor(medicineFor);
        medicine.setAmount(amount);
    }
}
