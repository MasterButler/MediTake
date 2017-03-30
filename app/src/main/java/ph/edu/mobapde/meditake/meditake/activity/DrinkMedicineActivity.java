package ph.edu.mobapde.meditake.meditake.activity;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.util.AlarmUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class DrinkMedicineActivity extends AppCompatActivity {

    private PowerManager.WakeLock wl;
    private Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_drink_medicine);

        Bundle data = getIntent().getExtras();
        schedule = data.getParcelable(Schedule.TABLE);

        playRingtone();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void playRingtone(){
        Log.wtf("ACTION", "PLAY " + schedule.getRingtone());
        Ringtone r = AlarmUtil.convertStringToRingtone(getBaseContext(), schedule.getRingtone());
        r.play();
    }
}
