package ph.edu.mobapde.meditake.meditake.util;

import android.content.Context;
import android.database.Cursor;

import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.db.SQLiteConnection;

/**
 * Created by Winfred Villaluna on 3/18/2017.
 */

public class ScheduleUtil {
    SQLiteConnection databaseConnection;
    Context context;

    public ScheduleUtil(Context context){
        this.context = context;
    }

    public void initializeDBConnection(Context context){
        databaseConnection = new SQLiteConnection(context);
    }

    public long addNewSchedule(Schedule schedule){
        initializeDBConnection(context);
        return databaseConnection.createSchedule(schedule);
    }

    public Cursor getAllSchedule(){
        initializeDBConnection(context);
        return databaseConnection.getAllSchedule();
    }

    public Schedule getSchedule(int id){
        initializeDBConnection(context);
        return databaseConnection.getSchedule(id);
    }
    public int deleteSchedule(int id){
        initializeDBConnection(context);
        return databaseConnection.deleteSchedule(id);
    }

    public int deleteAllSchedule(){
        initializeDBConnection(context);
        return databaseConnection.deleteAllSchedule();
    }

    public int updateSchedule(Schedule schedule){
        initializeDBConnection(context);
        return databaseConnection.updateSchedule(schedule);
    }
}
