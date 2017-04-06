package ph.edu.mobapde.meditake.meditake.util;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.RequestCodes;
import ph.edu.mobapde.meditake.meditake.activity.MedicineListActivity;
import ph.edu.mobapde.meditake.meditake.activity.ScheduleListActivity;
import ph.edu.mobapde.meditake.meditake.activity.SettingsActivity;

/**
 * Created by Winfred Villaluna on 3/12/2017.
 */

public class DrawerManager {
    public static void execute(Activity activity, MenuItem item){
        // TODO check if i.setFlags() will cause a problem in onActivityResult(). Will remove if problem is found within it.
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i;

        switch(id){
            case R.id.nav_schedule:
                i = new Intent();
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.setClass(activity.getBaseContext(), ScheduleListActivity.class);
                activity.startActivity(i);
                break;
            case R.id.nav_medicine:
                i = new Intent();
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.setClass(activity.getBaseContext(), MedicineListActivity.class);
                activity.startActivity(i);
                break;
            case R.id.nav_settings:
                i = new Intent();
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.setClass(activity.getBaseContext(), SettingsActivity.class);
                activity.startActivityForResult(i, RequestCodes.REQUEST_SETTINGS_UPDATE);
                break;
            default: Toast.makeText(activity.getBaseContext(), "Unexpected error encountered. Please try again", Toast.LENGTH_SHORT);
        }
    }
}
