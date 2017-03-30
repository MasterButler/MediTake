package ph.edu.mobapde.meditake.meditake.util;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Winfred Villaluna on 3/29/2017.
 */

public class AlarmUtil {

    public static final int REQUEST_RINGTONE = 1;

    public static void chooseRingtone(Fragment f){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone:");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALARM);
        f.startActivityForResult(intent, REQUEST_RINGTONE);
    }

    public static Uri getRingtoneUri(Context context, Intent data){
        Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        Ringtone ringtone = null;
        if (uri == null) {
            uri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE);
        }
        return uri;
    }

    public static Ringtone convertStringToRingtone(Context context, String string){


        Uri ringtoneUri = Uri.parse(string);
        Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
        if(ringtone == null){
            ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE));
        }
        return ringtone;
    }
}
