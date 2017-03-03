package ph.edu.mobapde.meditake.meditake.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import ph.edu.mobapde.meditake.meditake.R;

/**
 * Created by Winfred Villaluna on 2/27/2017.
 */

public class ThemeUtil {
    private static int selectedTheme;
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_BLUE = 1;
    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        selectedTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }
    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        Log.wtf("THEME_INFO", "THEME SELECTED IS: " + selectedTheme);
        switch (selectedTheme)
        {
            default:
            case THEME_DEFAULT:
                activity.setTheme(R.style.AppTheme_NoActionBar_DefaultColorScheme);
                break;

            case THEME_BLUE:
                activity.setTheme(R.style.AppTheme_NoActionBar_BlueColorScheme);
                break;

        }
    }

    public static int getSelectedTheme(){
        if(selectedTheme < 1)
            return 0;
        return selectedTheme;
    }
}
