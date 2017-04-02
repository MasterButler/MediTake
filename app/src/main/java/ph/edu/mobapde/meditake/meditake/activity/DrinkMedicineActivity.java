package ph.edu.mobapde.meditake.meditake.activity;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.util.AlarmUtil;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class DrinkMedicineActivity extends AppCompatActivity {

    @BindView(R.id.drink_medicine)
    Button buttonMedicineDrink;

    private PowerManager.WakeLock wl;
    private Schedule schedule;

    private Ringtone r;
    private Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_drink_medicine);

        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        schedule = data.getParcelable(Schedule.TABLE);

        playRingtone();
        enableVibration();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(r.isPlaying()){
                    r.stop();
                }
                if(v.hasVibrator()){
                    v.cancel();
                }
                finish();
            }
        }, 45*DateUtil.MILLIS_TO_SECONDS);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void playRingtone(){
        r = AlarmUtil.convertStringToRingtone(getBaseContext(), schedule.getRingtone());
        r.play();
    }

    public void enableVibration(){
        v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(new long[]{0l,200l,100l,100l}, 0);
    }

    @OnClick(R.id.drink_medicine)
    public void drinkMedicine(){
        Intent i = new Intent(getBaseContext(), ScheduleListActivity.class);
        i.putExtra(Schedule.TABLE, schedule);
        startActivity(i);
    }
}
