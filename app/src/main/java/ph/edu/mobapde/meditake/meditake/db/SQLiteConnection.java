package ph.edu.mobapde.meditake.meditake.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ph.edu.mobapde.meditake.meditake.beans.Capsule;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.util.MedicineInstantiatorUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;

/**
 * Created by Winfred Villaluna on 3/6/2017.
 */

public class SQLiteConnection extends SQLiteOpenHelper{
    public static final String SCHEMA = "MediTake";
    public static final int VERSION = 12;

    private Context contextHolder;
    private MedicineUtil medicineUtil;

    public SQLiteConnection(Context context) {
        super(context, SCHEMA, null, VERSION);
        this.contextHolder = context;
    }

    /**
     * Creates the db if it is not yet exisiting in the device's local storage.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlMedicine;
        String sqlSchedule;
        /*
         * CREATE TABLE medicine
         * _id INTEGER PRIMARY KEY AUTOINCREMENT
         * brandName TEXT
         * genericName TEXT NOT NULL
         *
         * medicineFor TEXT
         * amount REAL NOT NULL
         * dosage REAL NOT NULL
         * medicineType TEXT NOT NULL
         * );
         *
         * CREATE TABLE schedule
         * _id INTEGER PRIMARY KEY AUTOINCREMENT
         * medicineToDrinkId INTEGER NOT NULL
         * dosagePerDrinkingInterval INTEGER NOT NULL
         * drinkingIntervals INTEGER NOT NULL
         * isActivated NUMERIC NOT NULL
         */


        sqlMedicine = "CREATE TABLE " + Medicine.TABLE + " ( "
            + Medicine.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Medicine.COLUMN_BRAND_NAME + " TEXT, "
            + Medicine.COLUMN_GENERIC_NAME + " TEXT NOT NULL, "
            + Medicine.COLUMN_MEDICINE_FOR + " TEXT, "
            + Medicine.COLUMN_AMOUNT + " REAL NOT NULL, "
            + Medicine.COLUMN_DOSAGE + " REAL NOT NULL, "
            + Medicine.COLUMN_MEDICINE_TYPE + " TEXT NOT NULL);";

        sqlSchedule = "CREATE TABLE " + Schedule.TABLE + " ( "
            + Schedule.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Schedule.COLUMN_MEDICINE_TO_DRINK + " INTEGER, "
            + Schedule.COLUMN_DOSAGE_PER_DRINKING_INTERVAL + " REAL, "
            + Schedule.COLUMN_DRINKING_INTERVAL + " REAL, "
            + Schedule.COLUMN_LAST_TIME_TAKEN + " REAL, "
            + Schedule.COLUMN_IS_ACTIVATED + " NUMERIC NOT NULL, "
            + Schedule.COLUMN_CUSTOM_NEXT_DRINKING_TIME + " REAL);";

        db.execSQL(sqlMedicine);
        db.execSQL(sqlSchedule);
    }

    /**
     * Called whenever the newVersion is not equal to the oldVersion, updates the tables in
     * the device.
     * @param db
     * @param oldVersion old version of the db
     * @param newVersion new version of the db
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlMedicine = "DROP TABLE IF EXISTS " + Medicine.TABLE;
        String sqlSchedule = "DROP TABLE IF EXISTS " + Schedule.TABLE;
        db.execSQL(sqlMedicine);
        db.execSQL(sqlSchedule);
        onCreate(db);
    }

    /****************
     * MEDICINE CRUD
     ****************/

    /**
     * Creates medicine by passing another medicine object
     * @param medicine
     * @return id of the created object
     */
    public long createMedicine(Medicine medicine){

        Log.wtf("DB_ADD", "instance of " + medicine.getClass().getSimpleName() + " to be inserted");

        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(Medicine.TABLE, null, MedicineInstantiatorUtil.createCVMapFromBean(medicine));

        db.close();
        return id;
    }

    /**
     * Retrieves all medicine in the database.
     * @return cursor containing all the medicines in the database
     */
    public Cursor getAllMedicine(){
        /* SELECT * FROM medicine
         *null == '*'
         */
        SQLiteDatabase db = getReadableDatabase();
        return db.query(Medicine.TABLE, null, null, null, null, null, null);
    }

    /**
     * Retrieves the medicine through the id.
     * @param id value that points to the object.
     * @return medicine object referenced by the id.
     */
    public Medicine getMedicine(int id){
        //SELECT * FROM medicine WHERE _id = ?
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
            medicine = MedicineInstantiatorUtil.createMedicineFromCursor(cursor);
        }

        cursor.close();
        db.close();
        return medicine;
    }

    /**
     * Retrieves the medicine through the given conditions
     * @param conditions array of strings that dictate the values being searched.
     * @return medicines referenced by the id.
     */
    public Cursor getMedicine(String[] conditions) {
        //SELECT * FROM medicine WHERE _id = ?
        Medicine medicine = null;
        SQLiteDatabase db = getReadableDatabase();

        String brandNameLike = "";
        String genericNameLike = "";
        String medicineForLike = "";
        String medicineTypeLike = "";
        String[] newConditions = new String[conditions.length*4];

        for(int i = 0; i < conditions.length; i++){
            brandNameLike += "lower(" + Medicine.COLUMN_BRAND_NAME + ") LIKE ? ";
            genericNameLike += "lower(" + Medicine.COLUMN_GENERIC_NAME + ") LIKE ? ";
            medicineForLike += "lower(" + Medicine.COLUMN_MEDICINE_FOR + ") LIKE ? ";
            medicineTypeLike += "lower(" + Medicine.COLUMN_MEDICINE_TYPE + ") LIKE ? ";

            if(i != conditions.length-1){
                brandNameLike += "OR ";
                genericNameLike += "OR ";
                medicineForLike += "OR ";
                medicineTypeLike += "OR ";
            }
        }


        for(int i = 0; i < newConditions.length; i++){
            Log.wtf("AT ARRAY", " AT INDEX " + i + ": " + conditions[i%conditions.length]);
            newConditions[i] = "%" + conditions[i%conditions.length] + "%";
        }

        Cursor cursor = db.query(Medicine.TABLE,
                null,
                brandNameLike + " OR " +
                genericNameLike + " OR " +
                medicineForLike + " OR " +
                medicineTypeLike,
                newConditions,
                null,
                null,
                null);
        return cursor;
    }

    /**
     * Updates the value of the medicine
     * @param medicine object that contains the new value and as well as the id of the object to be updated
     * @return int containing the number of rows affected
     */
    public int updateMedicine(Medicine medicine){
        SQLiteDatabase db = getWritableDatabase();
        /* UPDATE INTO medicine SET ..... WHERE id = ?

         */
        ContentValues cv = MedicineInstantiatorUtil.createCVMapFromBean(medicine);

        int rows = db.update(Medicine.TABLE,
                    cv,
                    Medicine.COLUMN_ID + " = ? ",
                    new String[]{medicine.getSqlId()+""});

        db.close();
        return rows;
    }

    public int updateMedicineId(int prevId, int newId){
        SQLiteDatabase db = getWritableDatabase();
        /* UPDATE INTO medicine SET ..... WHERE id = ?

         */
        ContentValues cv = new ContentValues();
        cv.put(Medicine.COLUMN_ID, prevId);

        int rows = db.update(Medicine.TABLE,
                cv,
                Medicine.COLUMN_ID + " = ? ",
                new String[]{newId+""});

        db.close();
        return rows;
    }

    /**
     * Deletes the medicine in the through the use of its id.
     * @param id value that points to the object
     * @return int containing the number of rows deleted
     */
    public int deleteMedicine(int id){
        SQLiteDatabase db = getWritableDatabase();
        // DELETE FROM medicine WHERE _id = ?
        int rows = db.delete(Medicine.TABLE,
                Medicine.COLUMN_ID + " = ? ",
                new String[]{id+""});
        db.close();
        return rows;
    }

    /****************
     * SCHEDULE CRUD
     ****************/
    /**
     * Creates schedule by passing another scheudle object
     * @param schedule
     * @return id of the created object
     */
    public long createSchedule(Schedule schedule){
        ContentValues cv = new ContentValues();

        if(schedule.getMedicineToDrink() != null){
            cv.put(Schedule.COLUMN_MEDICINE_TO_DRINK, schedule.getMedicineToDrink().getSqlId());
        }
        cv.put(Schedule.COLUMN_DOSAGE_PER_DRINKING_INTERVAL, schedule.getDosagePerDrinkingInterval());
        cv.put(Schedule.COLUMN_DRINKING_INTERVAL, schedule.getDrinkingInterval());
        cv.put(Schedule.COLUMN_LAST_TIME_TAKEN, schedule.getLastTimeTaken());
        cv.put(Schedule.COLUMN_IS_ACTIVATED, schedule.isActivated());
        cv.put(Schedule.COLUMN_CUSTOM_NEXT_DRINKING_TIME, schedule.getCustomNextDrinkingTime());

        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(Schedule.TABLE, null, cv);

        Log.wtf("DB_ADD", "instance of schedule with id " + id + " inserted");

        db.close();
        return id;
    }

    /**
     * Retrieves all schedules in the database.
     * @return cursor containing all the schedule in the database
     */
    public Cursor getAllSchedule(){
        /* SELECT * FROM schedule
         *null == '*'
         */
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + Schedule.TABLE + ", " + Medicine.TABLE +
                " WHERE " + Schedule.TABLE + "." + Schedule.COLUMN_MEDICINE_TO_DRINK +
                " = " + Medicine.TABLE + "." + Medicine.COLUMN_ID;
        return db.rawQuery(sql, null);
    }

    public Schedule getSchedule(int id){
        //SELECT * FROM schedule WHERE _id = ?
        Schedule schedule = null;
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + Schedule.TABLE + ", " + Medicine.TABLE +
                " WHERE " + Schedule.TABLE + "." + Schedule.COLUMN_MEDICINE_TO_DRINK +
                " = " + Medicine.TABLE + "." + Medicine.COLUMN_ID
                + " AND " + Schedule.TABLE + "." + Schedule.COLUMN_ID + " = " + id;
        Cursor cursor = db.query(Schedule.TABLE,
                null,
                Schedule.TABLE + "." + Schedule.COLUMN_ID + " = ?",
                new String[]{id+""},
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            schedule = new Schedule();

            int medicineToDrinkId = cursor.getInt(cursor.getColumnIndex(Schedule.COLUMN_MEDICINE_TO_DRINK));
            double dosagePerDrinkingInterval = cursor.getDouble(cursor.getColumnIndex(Schedule.COLUMN_DOSAGE_PER_DRINKING_INTERVAL));
            double drinkingInterval = cursor.getDouble(cursor.getColumnIndex(Schedule.COLUMN_DRINKING_INTERVAL));
            long lastTimeTaken = cursor.getLong(cursor.getColumnIndex(Schedule.COLUMN_LAST_TIME_TAKEN));
            boolean isActivated = cursor.getInt(cursor.getColumnIndex(Schedule.COLUMN_IS_ACTIVATED)) == 1 ? true : false;
            long customNextDrinkingTime = cursor.getLong(cursor.getColumnIndex(Schedule.COLUMN_CUSTOM_NEXT_DRINKING_TIME));

            String brandName  = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_BRAND_NAME));
            String genericName  = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_GENERIC_NAME));
            String medicineFor =  cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_FOR));
            double amount = cursor.getDouble(cursor.getColumnIndex(Medicine.COLUMN_AMOUNT));
            String medicineType = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_TYPE));

            Medicine med = MedicineInstantiatorUtil.createMedicineInstanceFromString(medicineType);
            med.setSqlId(medicineToDrinkId);
            med.setBrandName(brandName);
            med.setGenericName(genericName);
            med.setMedicineFor(medicineFor);
            med.setAmount(amount);

            Log.wtf("IN SQLITE CONNECTION", "FOUND " + id);
            Log.wtf("check", "FOUND SCHEDULE WITH INTERVAL OF " + drinkingInterval);

            schedule.setSqlId(id);
            schedule.setMedicineToDrink(med);
            schedule.setDosagePerDrinkingInterval(dosagePerDrinkingInterval);
            schedule.setDrinkingInterval(drinkingInterval);
            schedule.setLastTimeTaken(lastTimeTaken);
            schedule.setActivated(isActivated);
            schedule.setCustomNextDrinkingTime(customNextDrinkingTime);
        }

        cursor.close();
        db.close();
        return schedule;
    }

    /**
     * Retrieves the schedule through the id.
     * @param id value that points to the object.
     * @return schedule object referenced by the id.
     * @deprecated use getScheduleInstead(), this will only break the code.
     */
    @Deprecated
    public Schedule getScheduleWithoutMedicineInfo(int id){
        //SELECT * FROM schedule WHERE _id = ?
        Schedule schedule = null;
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + Schedule.TABLE + ", " + Medicine.TABLE +
                " WHERE " + Schedule.TABLE + "." + Schedule.COLUMN_MEDICINE_TO_DRINK +
                " = " + Medicine.TABLE + "." + Medicine.COLUMN_ID
                + " AND " + Schedule.TABLE + "." + Schedule.COLUMN_ID + " = " + id;
        Cursor cursor = db.query(Schedule.TABLE,
                null,
                Schedule.COLUMN_ID + " = ?",
                new String[]{id+""},
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            schedule = new Schedule();
            long medicineToDrinkId = cursor.getLong(cursor.getColumnIndex(Schedule.COLUMN_MEDICINE_TO_DRINK));
            double dosagePerDrinkingInterval = cursor.getDouble(cursor.getColumnIndex(Schedule.COLUMN_DOSAGE_PER_DRINKING_INTERVAL));
            double drinkingInterval = cursor.getDouble(cursor.getColumnIndex(Schedule.COLUMN_DRINKING_INTERVAL));
            long lastTimeTaken = cursor.getLong(cursor.getColumnIndex(Schedule.COLUMN_LAST_TIME_TAKEN));
            boolean isActivated = cursor.getInt(cursor.getColumnIndex(Schedule.COLUMN_IS_ACTIVATED)) == 1 ? true : false;

            Medicine medTempHolder = new Capsule();
            medTempHolder.setSqlId((int) medicineToDrinkId);

            Log.wtf("IN SQLITE CONNECTION", "FOUND " + id);

            schedule.setSqlId(id);
            schedule.setMedicineToDrink(medTempHolder);
            schedule.setDosagePerDrinkingInterval(dosagePerDrinkingInterval);
            schedule.setDosagePerDrinkingInterval(drinkingInterval);
            schedule.setLastTimeTaken(lastTimeTaken);
            schedule.setActivated(isActivated);
        }

        cursor.close();
        db.close();
        return schedule;
    }

    /**
     * Updates the value of the medicine
     * @param schedule object that contains the new value and as well as the id of the object to be updated
     * @return int containing the number of rows affected
     */
    public int  updateSchedule(Schedule schedule){
//        SQLiteDatabase db = getWritableDatabase();
//        /* UPDATE INTO medicine SET ..... WHERE id = ?
//
//         */
//        ContentValues cv = new ContentValues();
//        cv.put(Medicine.COLUMN_BRAND_NAME, medicine.getBrandName());
//        cv.put(Medicine.COLUMN_GENERIC_NAME, medicine.getGenericName());
//        cv.put(Medicine.COLUMN_MEDICINE_FOR, medicine.getMedicineFor());
//        cv.put(Medicine.COLUMN_AMOUNT, medicine.getAmount());
//        cv.put(Medicine.COLUMN_MEDICINE_TYPE, medicine.getClass().getSimpleName());
//
//        int rows = db.update(Medicine.TABLE,
//                cv,
//                Medicine.COLUMN_ID + " = ? ",
//                new String[]{medicine.getSqlId()+""});
//
//        db.close();
//        return rows;
        SQLiteDatabase db = getWritableDatabase();
        /* UPDATE INTO schedule SET ..... WHERE id = ?

         */
        ContentValues cv = new ContentValues();
        cv.put(Schedule.COLUMN_MEDICINE_TO_DRINK, schedule.getMedicineToDrink().getSqlId());
        cv.put(Schedule.COLUMN_DOSAGE_PER_DRINKING_INTERVAL, schedule.getDosagePerDrinkingInterval());
        cv.put(Schedule.COLUMN_DRINKING_INTERVAL, schedule.getDrinkingInterval());
        cv.put(Schedule.COLUMN_LAST_TIME_TAKEN, schedule.getLastTimeTaken());
        cv.put(Schedule.COLUMN_IS_ACTIVATED, schedule.isActivated());
        cv.put(Schedule.COLUMN_CUSTOM_NEXT_DRINKING_TIME, schedule.getCustomNextDrinkingTime());

        Log.wtf("check", "UPDATING MEDICINE ID OF " + schedule.getSqlId() + " TO " + schedule.getMedicineToDrink().getSqlId());
        Log.wtf("check", "UPDATING DOSAGE OF " + schedule.getSqlId() + " TO " + schedule.getDosagePerDrinkingInterval());
        Log.wtf("check", "UPDATING INTERVAL OF " + schedule.getSqlId() + " TO " + schedule.getDrinkingInterval());
        Log.wtf("check", "UPDATING LAST TIME OF " + schedule.getLastTimeTaken() + " TO " + schedule.getLastTimeTaken());
        Log.wtf("check", "UPDATING ISACTIVATED OF " + schedule.getSqlId() + " TO " + schedule.isActivated());
        Log.wtf("check", "UPDATING CUSTOM NEXT DRINK TIME OF " + schedule.getSqlId() + " TO " + schedule.getCustomNextDrinkingTime());
        int rows = db.update(Schedule.TABLE,
                cv,
                Schedule.COLUMN_ID + " = ? ",
                new String[]{schedule.getSqlId()+""});
        Log.d("ROWS AFFECTED", rows+"");
        db.close();
        return rows;
    }

    /**
     * Deletes the schedule in the db through the use of its id.
     * @param id value that points to the object
     * @return int containing the number of rows deleted
     */
    public int deleteSchedule(int id){
        SQLiteDatabase db = getWritableDatabase();
        // DELETE FROM medicine WHERE _id = ?
        int rows = db.delete(Schedule.TABLE,
                Schedule.COLUMN_ID + " = ? ",
                new String[]{id+""});
        db.close();
        Log.wtf("DELETE", "deleted item with id " + id);
        return rows;
    }

    public Medicine getFirstMedicineRow() {
        //SELECT * FROM schedule WHERE _id = ?
        Medicine medicine = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Medicine.TABLE, null, null, null, null, null, null, "1");

        if(cursor.moveToFirst()){
            medicine = MedicineInstantiatorUtil.createMedicineInstanceFromString(cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_TYPE)));
            long id = cursor.getLong(cursor.getColumnIndex(Medicine.COLUMN_ID));
            String brandName = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_BRAND_NAME));
            String genericName = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_GENERIC_NAME));
            String medicineFor = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_FOR));
            double amount = cursor.getDouble(cursor.getColumnIndex(Medicine.COLUMN_AMOUNT));

            Log.wtf("IN SQLITE CONNECTION", "FOUND " + id);
            Log.wtf("IN SQLITE CONNECTION", "FOUND " + brandName);

            medicine.setSqlId((int) id);
            medicine.setBrandName(brandName);
            medicine.setGenericName(genericName);
            medicine.setMedicineFor(medicineFor);
            medicine.setAmount(amount);
            Log.wtf("FULL INFO", medicine.getSqlId() + ": " + medicine.getBrandName() + ", " + medicine.getGenericName() + ", " + medicine.getMedicineFor());
        }


        db.close();
        return medicine;
    }
}


