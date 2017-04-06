package ph.edu.mobapde.meditake.meditake.activity;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.frakbot.glowpadbackport.GlowPadView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.RecylerView.AlarmMedicineAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.MedicinePlan;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.util.AlarmUtil;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicinePlanUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;
import ph.edu.mobapde.meditake.meditake.util.instantiator.MedicinePlanInstantiatorUtil;

public class DrinkMedicineActivity extends AppCompatActivity {

    public static final int TARGET_SNOOZE = 0;
    public static final int TARGET_DRINK = 2;

    @BindView(R.id.rv_medicine_alarm)
    RecyclerView rvMedicineAlert;

    @BindView(R.id.gpv_alarm_medicine)
    GlowPadView glowPad;

    @BindView(R.id.tv_alarm_display_time)
    TextView tvAlarmTime;
    @BindView(R.id.tv_alarm_display_time_period)
    TextView tvAlarmTimePeriod;


    AlarmMedicineAdapter alarmMedicineAdapter;

    MedicinePlanUtil medicinePlanUtil;
    MedicineUtil medicineUtil;

    private PowerManager.WakeLock wl;
    private Schedule schedule;

    private Ringtone r;
    private Vibrator v;

    Handler handler;
    Runnable autoCloser;

    boolean isClicked;
    boolean isMilitary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_drink_medicine);

        ButterKnife.bind(this);

        medicineUtil = new MedicineUtil(getBaseContext());
        medicinePlanUtil = new MedicinePlanUtil(getBaseContext());

        isMilitary = false;

        checkIntent();
        initializeAdapter();
        initializeContent();
        initializeContinuousPing();
        initializeTimedPresence();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

    }

    public void checkIntent(){
        Bundle data = getIntent().getExtras();
        schedule = data.getParcelable(Schedule.TABLE);
    }

    public void initializeAdapter(){

        ArrayList<MedicinePlan> medicinePlanList = medicinePlanUtil.getMedicinePlanListWithScheduleId(schedule.getSqlId());
        if(medicinePlanList != null) {
            int[] medicineId = new int[medicinePlanList.size()];
            for (int i = 0; i < medicinePlanList.size(); i++) {
                medicineId[i] = medicinePlanList.get(i).getMedicineId();
                Log.wtf("DRINK MEDICINE", "MEDICINE WITH ID OF " + medicineId[i]);
            }
            alarmMedicineAdapter = new AlarmMedicineAdapter(getBaseContext(), medicineUtil.getMedicine(medicineId), Medicine.COLUMN_ID);
        }else {
            alarmMedicineAdapter = new AlarmMedicineAdapter(getBaseContext(), null, Medicine.COLUMN_ID);
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        rvMedicineAlert.setAdapter(alarmMedicineAdapter);
        rvMedicineAlert.setLayoutManager(mLayoutManager);
    }

    public void initializeContent(){
        playRingtone();
        playVibration();

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        String displayTime = DateUtil.format(hour, minute, isMilitary);
        this.tvAlarmTime.setText(displayTime.split("\\s+")[0]);
        this.tvAlarmTimePeriod.setText(displayTime.split("\\s+")[1]);

        //final int[] mults = {1, 2, 3, 4, 6, 8, 10, 12, 16, 20, 32};
        glowPad.setPointsMultiplier(12);
        glowPad.setOnTriggerListener(new GlowPadView.OnTriggerListener() {
            @Override
            public void onGrabbed(View v, int handle) {
                setClicked(true);
            }

            @Override
            public void onReleased(View v, int handle) {
                setClicked(false);
            }

            @Override
            public void onTrigger(View v, int target) {
                switch(target){
                    case TARGET_DRINK:  drink();
                        break;
                    case TARGET_SNOOZE: snooze();
                        break;
                }
                Toast.makeText(getBaseContext(), "Target triggered! ID=" + target, Toast.LENGTH_SHORT).show();
                glowPad.reset(true);
            }

            @Override
            public void onGrabbedStateChange(View v, int handle) {
                // Do nothing
            }

            @Override
            public void onFinishFinalAnimation() {
                // Do nothing
            }
        });
    }

    public void initializeContinuousPing(){
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while(true){
                        if(!isInterrupted() && !isClicked) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    glowPad.ping();
                                }
                            });
                            Thread.sleep(1500);
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    public void initializeTimedPresence(){
        handler = new Handler();
        autoCloser = new Runnable(){
            @Override
            public void run() {
                snooze();
            }
        };
        handler.postDelayed(autoCloser, 45000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.wtf("STOP", "STOPPING DRINK MED ACT");
    }

    public void playRingtone(){
        r = AlarmUtil.convertStringToRingtone(getBaseContext(), schedule.getRingtone());
        r.play();
    }

    public void playVibration(){
        v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
        if(schedule.isVibrate()) {
            v.vibrate(new long[]{0l, 200l, 100l, 100l}, 0);
        }
    }

    public boolean isClicked(){
        return isClicked;
    }
    public void setClicked(boolean bool){
        isClicked = bool;
    }

    public void drink(){
        Intent i = new Intent(getBaseContext(), ScheduleListActivity.class);
        i.putExtra(Schedule.TABLE, schedule);
        i.putExtra(getString(R.string.schedule_snooze), 0);
        startActivity(i);
    }

    public void snooze(){
        if(r.isPlaying()){
            r.stop();
        }
        if(v.hasVibrator()){
            v.cancel();
        }

        Intent i = new Intent(getBaseContext(), ScheduleListActivity.class);
        i.putExtra(Schedule.TABLE, schedule);
        i.putExtra(getString(R.string.schedule_snooze), 5);
        startActivity(i);
    }

}
