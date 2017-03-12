package ph.edu.mobapde.meditake.meditake.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ph.edu.mobapde.meditake.meditake.beans.Medicine;

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
        /* CREATE TABLE food
            (idfood INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            price REAL NOT NULL);
            sql = "CREATE TABLE " + Food.TABLE + " ( "
                + Food.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Food.COLUMN_NAME + " TEXT NOT NULL, "
                + Food.COLUMN_PRICE + " REAL NOT NULL);";
        */

        /*
         * CREATE TABLE medicine
         * _id INTEGER PRIMARY KEY AUTOINCREMENT
         * brandName TEXT
         * genericName TEXT NOT NULL
         * medicineFor TEXT NOT NULL
         * amount REAL NOT NULL
         * icon INTEGER NOT NULL
         * modifier TEXT NOT NULL
         * );
         */
        sql = "CREATE TABLE " + Medicine.TABLE + " ( "
            + Medicine.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Medicine.COLUMN_BRAND_NAME + " TEXT "
            + Medicine.COLUMN_GENERIC_NAME + " TEXT NOT NULL "
            + Medicine.COLUMN_MEDICINE_FOR + " TEXT NOT NULL "
            + Medicine.COLUMN_AMOUNT + " REAL NOT NULL "
            + Medicine.COLUMN_ICON + " INTEGER NOT NULL "
            + Medicine.COLUMN_MODIFIER + " TEXT NOT NULL)";

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

//    // create Food
//    public long createFood(Food food){
//
//        ContentValues cv = new ContentValues();
//
//        cv.put(Food.COLUMN_NAME, food.getName());
//        cv.put(Food.COLUMN_PRICE, food.getPrice());
//
//        SQLiteDatabase db = getWritableDatabase();
//        // INSERT INTO food ('name', 'price')
//        //             VALUES (?, ?)
//        long id = db.insert(Food.TABLE,
//                null, //only place a null / non empty string here when you want to enter an empty row
//                cv);
//
//        db.close();
//        return id;
//    }
//
//    // retrieve all food
//    public Cursor getAllFoods(){
//        /*
//        SELECT * FROM food
//        null == '*'
//        */
//        SQLiteDatabase db = getReadableDatabase();
//        return db.query(Food.TABLE, null, null, null, null, null, null);
//    }
//
//    // retrieve single food
//    public Food getFood(int id){
//        //SELECT * FROM food WHERE _id = ?
//        Food food = null;
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.query(Food.TABLE,
//                null,
//                Food.COLUMN_ID + " = ?",
//                new String[]{id+""},
//                null,
//                null,
//                null);
//
//        if(cursor.moveToFirst()){
//            food = new Food();
//            String name = cursor.getString(cursor.getColumnIndex(Food.COLUMN_NAME));
//            double price = cursor.getDouble(cursor.getColumnIndex(Food.COLUMN_PRICE));
//
//            food.setName(name);
//            food.setPrice(price);
//            food.setId(id);
//        }
//
//        cursor.close();
//        db.close();
//        return food;
//    }
//
//    // update food
//    public int updateFood(Food food){
//        SQLiteDatabase db = getWritableDatabase();
//        /* UPDATE INTO food
//                SET name = ? and price = ?
//                WHERE id = ?
//
//         */
//        ContentValues cv = new ContentValues();
//        cv.put(Food.COLUMN_NAME, food.getName());
//        cv.put(Food.COLUMN_PRICE, food.getPrice());
//        int rows = db.update(Food.TABLE,
//                    cv,
//                    Food.COLUMN_ID + " = ? ",
//                    new String[]{food.getId()+""});
//
//        db.close();
//        return rows;
//    }
//
//
//    // delete food
//    public int deleteFood(int id){
//        SQLiteDatabase db = getWritableDatabase();
//        // DELETE FROM food WHERE _id = ?
//        int rows = db.delete(Food.TABLE,
//                Food.COLUMN_ID + " = ? ",
//                new String[]{id+""});
//        db.close();
//        return rows;
//    }
}
