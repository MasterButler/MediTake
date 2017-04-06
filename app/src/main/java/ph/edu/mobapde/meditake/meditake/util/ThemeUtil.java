package ph.edu.mobapde.meditake.meditake.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import ph.edu.mobapde.meditake.meditake.R;

/**
 * Created by Winfred Villaluna on 2/27/2017.
 */

public class ThemeUtil {

    public static final String SELECTED_THEME = "theme";
    public final static int THEME_DEFAULT = 3;
    public final static int THEME_BLUE = 4;
    public final static int THEME_PURPLE = 1;
    public final static int THEME_INDIGO = 2;
    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        Log.wtf("THEME_INFO", "THEME PASSED IS: " + theme);
        setSelectedTheme(activity.getBaseContext(), theme);
        Log.wtf("THEME_INFO", "THEME STORED IS: " + getSelectedTheme(activity.getBaseContext()));
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void reloadWithTheme(Activity activity){
        changeToTheme(activity, getSelectedTheme(activity.getBaseContext()));
    }

    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        Log.wtf("THEME_INFO", "THEME SELECTED IS: " + getSelectedTheme(activity.getBaseContext()));
        switch (getSelectedTheme(activity.getBaseContext()))
        {
            default:
            case THEME_DEFAULT:
                activity.setTheme(R.style.AppTheme_NoActionBar_DefaultColorScheme);
                break;
            case THEME_BLUE:
                activity.setTheme(R.style.AppTheme_NoActionBar_BlueColorScheme);
                break;
            case THEME_PURPLE:
                activity.setTheme(R.style.AppTheme_NoActionBar_PurpleColorScheme);
                break;
            case THEME_INDIGO:
                activity.setTheme(R.style.AppTheme_NoActionBar_IndigoColorScheme);
                break;
        }
    }

    public static int getSelectedTheme(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(SELECTED_THEME, THEME_DEFAULT);
    }

    public static void setSelectedTheme(Context context, int selectedTheme){
        SharedPreferences sp =
                PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(SELECTED_THEME, selectedTheme);
        editor.commit();
    }
}
