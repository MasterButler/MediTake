package ph.edu.mobapde.meditake.meditake.util;

import android.content.Context;

import java.util.ArrayList;

import ph.edu.mobapde.meditake.meditake.beans.MedicinePlan;
import ph.edu.mobapde.meditake.meditake.db.SQLiteConnection;

/**
 * Created by Winfred Villaluna on 4/4/2017.
 */

public class MedicinePlanUtil {

    SQLiteConnection databaseConnection;
    Context context;

    public MedicinePlanUtil(Context context){
        this.context = context;
    }

    public void initializeDBConnection(Context context){
        databaseConnection = new SQLiteConnection(context);
    }

    public long addMedicinePlan(MedicinePlan medicinePlan){
        initializeDBConnection(context);
        return databaseConnection.createMedicinePlan(medicinePlan);
    }

    public ArrayList<MedicinePlan> getMedicinePlanListWithScheduleId(int id){
        initializeDBConnection(context);
        return databaseConnection.getMedicinePlanListWithScheduleId(id);
    }

    public int deleteMedicinePlan(int id){
        initializeDBConnection(context);
        return databaseConnection.deleteMedicinePlan(id);
    }
}
