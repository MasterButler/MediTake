package ph.edu.mobapde.meditake.meditake.util;

import android.content.Context;
import android.database.Cursor;

import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.db.SQLiteConnection;

/**
 * Created by Winfred Villaluna on 3/18/2017.
 */

public class ScheduleUtil {

    private SQLiteConnection databaseConnection;
    private Context context;

    public ScheduleUtil(Context context){
        databaseConnection = new SQLiteConnection(context);
    }

    public long addNewSchedule(Schedule schedule){
        return databaseConnection.createSchedule(schedule);
    }

    public Cursor getAllSchedule(){
        return databaseConnection.getAllSchedule();
    }
}
