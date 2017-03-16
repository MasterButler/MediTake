package ph.edu.mobapde.meditake.meditake.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.util.MedicineInstantiatorUtil;

/**
 * Created by Winfred Villaluna on 3/6/2017.
 */

public class SQLiteConnection extends SQLiteOpenHelper{
    public static final String SCHEMA = "MediTake";
    public static final int VERSION = 1;

    public SQLiteConnection(Context context) {
        super(context, SCHEMA, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // if the db exists, it will NOT call onCreate
        // if the db !exists, it will call onCreate
        //create tables here

        String sql;
        /*
         * CREATE TABLE medicine
         * _id INTEGER PRIMARY KEY AUTOINCREMENT
         * brandName TEXT
         * genericName TEXT NOT NULL
         * medicineFor TEXT
         * amount REAL NOT NULL
         * //icon INTEGER NOT NULL
         * //modifier TEXT NOT NULL
         * medicineType TEXT NOT NULL
         * );
         */
        sql = "CREATE TABLE " + Medicine.TABLE + " ( "
            + Medicine.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Medicine.COLUMN_BRAND_NAME + " TEXT, "
            + Medicine.COLUMN_GENERIC_NAME + " TEXT NOT NULL, "
            + Medicine.COLUMN_MEDICINE_FOR + " TEXT, "
            + Medicine.COLUMN_AMOUNT + " REAL NOT NULL, "
            //+ Medicine.COLUMN_ICON + " INTEGER NOT NULL, "
            //+ Medicine.COLUMN_MODIFIER + " TEXT NOT NULL, "
            + Medicine.COLUMN_MEDICINE_TYPE + " TEXT NOT NULL);";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //will be called when device's version < new apk's version
        //drop tables
        //call oncreate again
        String sql = "DROP TABLE IF EXISTS " + Medicine.TABLE;
        db.execSQL(sql);
        onCreate(db);

    }

    // CRUD OPERATIONS

    // create Medicine
    public long createMedicine(Medicine medicine){

        ContentValues cv = new ContentValues();

        Log.wtf("DB_ADD", "instance of " + medicine.getClass().getSimpleName() + " to be inserted");

        cv.put(Medicine.COLUMN_BRAND_NAME, medicine.getBrandName());
        cv.put(Medicine.COLUMN_GENERIC_NAME, medicine.getGenericName());
        cv.put(Medicine.COLUMN_MEDICINE_FOR, medicine.getMedicineFor());
        cv.put(Medicine.COLUMN_AMOUNT, medicine.getAmount());
        cv.put(Medicine.COLUMN_MEDICINE_TYPE, medicine.getClass().getSimpleName());

        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(Medicine.TABLE, null, cv);

        db.close();
        return id;
    }

    // retrieve all food
    public Cursor getAllMedicine(){
        /* SELECT * FROM medicine
         *null == '*'
         */
        SQLiteDatabase db = getReadableDatabase();
        return db.query(Medicine.TABLE, null, null, null, null, null, null);
    }

    // retrieve single food
    public Medicine getMedicine(int id){
        //SELECT * FROM food WHERE _id = ?
        Medicine medicine = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Medicine.TABLE,
                null,
                Medicine.COLUMN_ID + " = ?",
                new String[]{id+""},
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            medicine = MedicineInstantiatorUtil.createMedicineInstanceFromString(cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_TYPE)));
            String brandName = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_BRAND_NAME));
            String genericName = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_GENERIC_NAME));
            String medicineFor = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_FOR));
            double amount = cursor.getDouble(cursor.getColumnIndex(Medicine.COLUMN_AMOUNT));

            Log.wtf("IN SQLITE CONNECTION", "FOUND " + id);
            Log.wtf("IN SQLITE CONNECTION", "FOUND " + brandName);

            medicine.setSqlId(id);
            medicine.setBrandName(brandName);
            medicine.setGenericName(genericName);
            medicine.setMedicineFor(medicineFor);
            medicine.setAmount(amount);
        }

        cursor.close();
        db.close();
        Log.wtf("FULL INFO", medicine.getSqlId() + ": " + medicine.getBrandName() + ", " + medicine.getGenericName() + ", " + medicine.getMedicineFor());
        return medicine;
    }

    // update food
    public int updateMedicine(Medicine medicine){
        SQLiteDatabase db = getWritableDatabase();
        /* UPDATE INTO medicine SET ..... WHERE id = ?

         */
        ContentValues cv = new ContentValues();
        cv.put(Medicine.COLUMN_BRAND_NAME, medicine.getBrandName());
        cv.put(Medicine.COLUMN_GENERIC_NAME, medicine.getGenericName());
        cv.put(Medicine.COLUMN_MEDICINE_FOR, medicine.getMedicineFor());
        cv.put(Medicine.COLUMN_AMOUNT, medicine.getAmount());
        cv.put(Medicine.COLUMN_MEDICINE_TYPE, medicine.getClass().getSimpleName());

        int rows = db.update(Medicine.TABLE,
                    cv,
                    Medicine.COLUMN_ID + " = ? ",
                    new String[]{medicine.getSqlId()+""});

        db.close();
        return rows;
    }


    // delete food
    public int deleteMedicine(int id){
        SQLiteDatabase db = getWritableDatabase();
        // DELETE FROM medicine WHERE _id = ?
        int rows = db.delete(Medicine.TABLE,
                Medicine.COLUMN_ID + " = ? ",
                new String[]{id+""});
        db.close();
        return rows;
    }
}
