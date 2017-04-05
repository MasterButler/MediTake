package ph.edu.mobapde.meditake.meditake.util;

import android.content.Context;

import java.util.ArrayList;

import ph.edu.mobapde.meditake.meditake.beans.SchedulePlan;
import ph.edu.mobapde.meditake.meditake.db.SQLiteConnection;

/**
 * Created by Winfred Villaluna on 4/5/2017.
 */

public class SchedulePlanUtil {
    SQLiteConnection databaseConnection;
    Context context;

    public SchedulePlanUtil(Context context){
        this.context = context;
    }

    public void initializeDBConnection(Context context){
        databaseConnection = new SQLiteConnection(context);
    }

    public long addSchedulePlan(long scheduleId, long medicinePlanId){
        initializeDBConnection(context);
        return databaseConnection.createSchedulePlan(scheduleId, medicinePlanId);
    }

    public int deleteSchedulePlan(int id){
        initializeDBConnection(context);
        return databaseConnection.deleteSchedulePlan(id);
    }
}
