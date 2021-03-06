package ph.edu.mobapde.meditake.meditake.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.db.SQLiteConnection;

/**
 * Created by Winfred Villaluna on 3/12/2017.
 */

public class MedicineUtil {
    SQLiteConnection databaseConnection;
    Context context;

    public static final String ORDER_ASCENDING = "DESC";
    public static final String ORDER_DESENDING = "ASC";

    public MedicineUtil(Context context){
        this.context = context;
    }

    public void initializeDBConnection(Context context){
        databaseConnection = new SQLiteConnection(context);
    }

    public long addMedicine(Medicine medicine){
        initializeDBConnection(this.context);
        return databaseConnection.createMedicine(medicine);
    }

    public void setMedicineInfo(Medicine medicine, String brandName, String genericName, String medicineFor, long amount, int dosage){
        initializeDBConnection(this.context);
        medicine.setBrandName(brandName);
        medicine.setGenericName(genericName);
        medicine.setMedicineFor(medicineFor);
        medicine.setAmount(amount);
        medicine.setDosage(dosage);
    }

    public int deleteMedicine(int id){
        initializeDBConnection(this.context);
        return databaseConnection.deleteMedicine(id);
    }

    public int updateMedicine(Medicine medicine){
        initializeDBConnection(this.context);
        return databaseConnection.updateMedicine(medicine);
    }

    public Cursor getAllMedicine(){
        initializeDBConnection(this.context);
        return databaseConnection.getAllMedicine();
    }

    public Cursor getAllMedicine(String column, String order){
        initializeDBConnection(this.context);
        String orderBy = column + " " + order;
        if(column.equals(Medicine.COLUMN_MEDICINE_TYPE)){
            orderBy += ", " + Medicine.COLUMN_GENERIC_NAME + " " + ORDER_ASCENDING;
        }
        return databaseConnection.getAllMedicine(orderBy);
    }

    public Cursor getMedicine(int[] id){
        initializeDBConnection(this.context);
        return databaseConnection.getMedicine(id);
    }

    public Medicine getMedicine(int id){
        initializeDBConnection(this.context);
        return databaseConnection.getMedicine(id);
    }

    public Cursor search(String[] conditions) {
        initializeDBConnection(this.context);
        return databaseConnection.getMedicine(conditions);
    }

    public int updateMedicineId(int prevId, int newId){
        initializeDBConnection(this.context);
        return databaseConnection.updateMedicineId(prevId, newId);
    }

}
