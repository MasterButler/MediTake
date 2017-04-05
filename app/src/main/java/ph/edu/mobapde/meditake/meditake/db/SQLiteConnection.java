package ph.edu.mobapde.meditake.meditake.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.MedicinePlan;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.beans.SchedulePlan;
import ph.edu.mobapde.meditake.meditake.util.instantiator.MedicineInstantiatorUtil;
import ph.edu.mobapde.meditake.meditake.util.instantiator.MedicinePlanInstantiatorUtil;
import ph.edu.mobapde.meditake.meditake.util.instantiator.ScheduleInstantiatorUtil;

/**
 * Created by Winfred Villaluna on 3/6/2017.
 */

public class SQLiteConnection extends SQLiteOpenHelper{
    public static final String SCHEMA = "MediTake";
    public static final int VERSION = 17;
    private Context contextHolder;

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
        String sqlMedicinePlan;
        String sqlSchedulePlan;
        /*
         * CREATE TABLE medicine(
         * _id INTEGER PRIMARY KEY AUTOINCREMENT
         * brandName TEXT
         * genericName TEXT NOT NULL
         * medicineFor TEXT
         * amount REAL NOT NULL
         * dosage REAL NOT NULL
         * medicineType TEXT NOT NULL
         * );
         *
         * CREATE TABLE schedule
         * _scheduleId PRIMARY KEY AUTOINCREMENT
         * time INTEGER NOT NULL
         * label TEXT NOT NULL
         * ringtone TEXT NOT NULL
         * drinkingIntervals INTEGER NOT NULL
         * vibrate NUMERIC NOT NULL
         * isActivated NUMERIC NOT NULL
         * );
         *
         * CREATE TABLE medicinePlan
         * _medicinePlanId PRIMARY KEY AUTOINCREMENT
         * medicineId INTEGER NOT NULL
         * dosage REAL NOT NULL
         * );
         *
         * CREATE TABLE schedulePlan
         * scheduleId INTEGER NOT NULL
         * medicineId INTEGER NOT NULL
         * PRIMARY KEY (scheduleId, medicineId
         * );
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
                + Schedule.COLUMN_NEXT_DRINKING_TIME + " REAL NOT NULL, "
                + Schedule.COLUMN_LABEL + " TEXT, "
                + Schedule.COUMNN_RINGTONE + " TEXT NOT NULL, "
                + Schedule.COLUMN_DRINKING_INTERVAL + " INTEGER NOT NULL,"
                + Schedule.COLUMN_IS_VIBRATE + " NUMERIC NOT NULL, "
                + Schedule.COLUMN_IS_ACTIVATED + " NUMERIC NOT NULL);";

        sqlMedicinePlan = "CREATE TABLE " + MedicinePlan.TABLE + " ( "
                + MedicinePlan.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MedicinePlan.COLUMN_MEDICINE_ID + " INTEGER NOT NULL, "
                + MedicinePlan.COLUMN_DOSAGE + " REAL NOT NULL);";

        sqlSchedulePlan = "CREATE TABLE " + SchedulePlan.TABLE + " ( "
                + SchedulePlan.COLUMN_SCHEDULE_ID + " INTEGER NOT NULL, "
                + SchedulePlan.COLUMN_MEDICINE_PLAN_ID + " INTEGER NOT NULL, "
                + "PRIMARY KEY (" + SchedulePlan.COLUMN_SCHEDULE_ID + ", " + SchedulePlan.COLUMN_MEDICINE_PLAN_ID + "));";

        db.execSQL(sqlMedicine);
        db.execSQL(sqlSchedule);
        db.execSQL(sqlMedicinePlan);
        db.execSQL(sqlSchedulePlan);
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
            medicine = MedicineInstantiatorUtil.createBeanFromCursor(cursor);
        }

        cursor.close();
        db.close();
        return medicine;
    }

    /**
     * Retrieves the medicine through the id.
     * @param id value that points to the object.
     * @return medicine object referenced by the id.
     */
    public Cursor getMedicine(int[] id){
        //SELECT * FROM medicine WHERE _id = ?
        Medicine medicine = null;
        SQLiteDatabase db = getReadableDatabase();

        String stringIdPattern = "";
        String[] stringId = new String[id.length];
        for(int i = 0; i < id.length; i++) {
            stringId[i] = id[i] + "";
            stringIdPattern += "?";
            if(i != id.length-1){
                stringIdPattern += ",";
            }
        }

        for(int i = 0; i < id.length; i++){
            Log.wtf("IDS TO CHECK", stringId[i]);
        }

        return db.query(Medicine.TABLE,
                null,
                Medicine.COLUMN_ID + " IN(" + stringIdPattern + ")",
                stringId,
                null,
                null,
                null);
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

    /**
     * Updates the id of the medicine to insert the object into
     * a specific row within the arragement of the row. Especially useful
     * in making the undo function smooth to look at in the recycler view.
     * @param prevId id where the object was inserted when added
     * @param newId id where the object will be placed
     * @return
     */
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
        ContentValues cv = ScheduleInstantiatorUtil.createCVMapFromBean(schedule);

        SQLiteDatabase db;
        db = getWritableDatabase();
        long id = db.insert(Schedule.TABLE, null, cv);
        Log.wtf("DB_ADD", "instance of schedule with id " + id + " inserted");

        for(MedicinePlan medicinePlan : schedule.getMedicinePlanList()){
            long medicinePlanId = createMedicinePlan(medicinePlan);
            medicinePlan.setSqlId((int) medicinePlanId);
            createSchedulePlan(id, medicinePlanId);
        }


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
//        SQLiteDatabase db = getReadableDatabase();
//        String sql = "SELECT * FROM " + Schedule.TABLE + ", " + Medicine.TABLE;
//                + " WHERE " + Schedule.TABLE + "." + Schedule.COLUMN_MEDICINE_TO_DRINK
//                + " = " + Medicine.TABLE + "." + Medicine.COLUMN_ID;
        SQLiteDatabase db = getReadableDatabase();
        return db.query(Schedule.TABLE, null, null, null, null, null, null);
        //return db.rawQuery(sql, null);
    }

    public Schedule getSchedule(int id){
        //SELECT * FROM schedule WHERE _id = ?
        Schedule schedule = null;
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + Schedule.TABLE + ", " + Medicine.TABLE +
                " WHERE " +
//                Schedule.TABLE + "." + Schedule.COLUMN_MEDICINE_TO_DRINK +
//                " = " + Medicine.TABLE + "." + Medicine.COLUMN_ID + " AND "
                Schedule.TABLE + "." + Schedule.COLUMN_ID + " = " + id;
        Cursor cursor = db.query(Schedule.TABLE,
                null,
                Schedule.TABLE + "." + Schedule.COLUMN_ID + " = ?",
                new String[]{id+""},
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            schedule = ScheduleInstantiatorUtil.createBeanFromCursor(cursor);
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
        SQLiteDatabase db = getWritableDatabase();
        /* UPDATE INTO schedule SET ..... WHERE id = ? */
        ContentValues cv = ScheduleInstantiatorUtil.createCVMapFromBean(schedule);
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
        /* DELETE FROM medicine WHERE _id = ? */
        int rows = db.delete(Schedule.TABLE,
                Schedule.COLUMN_ID + " = ? ",
                new String[]{id+""});
        db.close();
        Log.wtf("DELETE", "Deleted Schedule with id " + id);
        return rows;
    }

    /**
     * Deletes all schedule in the db.
     */
    public int deleteAllSchedule(){
        SQLiteDatabase db = getWritableDatabase();
        /* DELETE FROM medicine WHERE _id = ? */
        int rows = db.delete(Schedule.TABLE, null, null);
        db.close();
        return rows;
    }


    /*********************
     * MEDICINE PLAN CRUD
     *********************/

    public long createMedicinePlan(MedicinePlan medicinePlan){
        Log.wtf("DB_ADD", "ADDING MedicinePlan with id of " + medicinePlan);

        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(MedicinePlan.TABLE, null, MedicinePlanInstantiatorUtil.createCVMapFromBean(medicinePlan));

        db.close();
        return id;
    }

    public MedicinePlan getMedicinePlan(int id){
        MedicinePlan medicinePlan = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(MedicinePlan.TABLE,
                null,
                MedicinePlan.TABLE + "." + MedicinePlan.COLUMN_ID + " = ?",
                new String[]{id+""},
                null,
                null,
                null);
        if(cursor.moveToFirst()){
            medicinePlan = MedicinePlanInstantiatorUtil.createBeanFromCursor(cursor);
        }
        cursor.close();
        db.close();
        return medicinePlan;
    }

    public int updateMedicinePlan(MedicinePlan medicinePlan){
        SQLiteDatabase db = getWritableDatabase();
        /* UPDATE INTO schedule SET ..... WHERE id = ? */
        ContentValues cv = MedicinePlanInstantiatorUtil.createCVMapFromBean(medicinePlan);
        int rows = db.update(MedicinePlan.TABLE,
                cv,
                MedicinePlan.COLUMN_ID + " = ? ",
                new String[]{medicinePlan.getSqlId()+""});
        Log.d("ROWS AFFECTED", rows+"");
        db.close();
        return rows;

    }

    public ArrayList<MedicinePlan> getMedicinePlanListWithScheduleId(int scheduleId){
        ArrayList<MedicinePlan> medicinePlanList = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(SchedulePlan.TABLE,
                null,
                SchedulePlan.TABLE + "." + SchedulePlan.COLUMN_SCHEDULE_ID+ " = ? ",
                new String[]{scheduleId+""},
                null,
                null,
                null);


        if(cursor.moveToFirst()){
            medicinePlanList = new ArrayList<>();
            while(!cursor.isAfterLast()) {
                int medicinePlanId = cursor.getInt(cursor.getColumnIndex(SchedulePlan.COLUMN_MEDICINE_PLAN_ID));
                Log.wtf("ITERATE", "GETTING MEDICINE PLAN WITH ID " + medicinePlanId);
                Log.wtf("ITERATE", "GETTING SCHEDULE ID " + cursor.getInt(cursor.getColumnIndex(SchedulePlan.COLUMN_SCHEDULE_ID)));
                medicinePlanList.add(getMedicinePlan(medicinePlanId));

                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();

        return medicinePlanList;
    }

    public int deleteMedicinePlan(int id){
        SQLiteDatabase db = getWritableDatabase();
        /* DELETE FROM medicine WHERE _id = ? */
        int rows = db.delete(MedicinePlan.TABLE,
                MedicinePlan.COLUMN_ID + " = ? ",
                new String[]{id+""});
        db.close();
        Log.wtf("DELETE", "Deleted Medicine plan with ID " + id);
        return rows;
    }

    /*********************
     * SCHEDULE PLAN CRUD
     *********************/

    public long createSchedulePlan(long scheduleId, long medicinePlanId){
        Log.wtf("DB_ADD", "ADDING SchedulePlan with id of " + scheduleId + " AND " + medicinePlanId);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SchedulePlan.COLUMN_MEDICINE_PLAN_ID, medicinePlanId);
        cv.put(SchedulePlan.COLUMN_SCHEDULE_ID, scheduleId);

        long id = db.insert(SchedulePlan.TABLE, null, cv);

        db.close();
        return id;
    }

    public int deleteSchedulePlan(int scheudleId){
        SQLiteDatabase db = getWritableDatabase();
        /* DELETE FROM medicine WHERE _id = ? */
        int rows = db.delete(SchedulePlan.TABLE,
                SchedulePlan.TABLE + "." + SchedulePlan.COLUMN_SCHEDULE_ID + " = ? ",
                new String[]{scheudleId+""});
        db.close();
        Log.wtf("DELETE", "Deleted Schedule plan with ID " + scheudleId);
        return rows;
    }
}


