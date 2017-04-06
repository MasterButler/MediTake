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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;

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

    @BindView(R.id.Snooze)
    Button buttonMedicineDrink;

    @BindView(R.id.rv_medicine_alarm)
    RecyclerView rvMedicineAlert;

    AlarmMedicineAdapter alarmMedicineAdapter;

    MedicinePlanUtil medicinePlanUtil;
    MedicineUtil medicineUtil;

    private PowerManager.WakeLock wl;
    private Schedule schedule;

    private Ringtone r;
    private Vibrator v;

    Handler handler;
    Runnable autoCloser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_drink_medicine);

        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        schedule = data.getParcelable(Schedule.TABLE);

        medicineUtil = new MedicineUtil(getBaseContext());
        medicinePlanUtil = new MedicinePlanUtil(getBaseContext());

        initializeAdapter();

        playRingtone();
        playVibration();

        handler = new Handler();
        autoCloser = new Runnable(){
            @Override
            public void run() {
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
        };
        handler.postDelayed(autoCloser, 45000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.wtf("STOP", "STOPPING DRINK MED ACT");
        handler.removeCallbacks(autoCloser);
    }

    @OnClick(R.id.Snooze)
    public void drinkMedicine(){
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


}
