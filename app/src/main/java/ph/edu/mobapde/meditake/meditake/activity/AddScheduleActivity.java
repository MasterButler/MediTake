package ph.edu.mobapde.meditake.meditake.activity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.page.AddSchedulePagerAdapter;
import ph.edu.mobapde.meditake.meditake.adapter.page.AddScheduleStepAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.dialog.BasicRepeatingTimePickerDialogFragment;
import ph.edu.mobapde.meditake.meditake.fragment.schedule.add.AddScheduleDetailsFragment;
import ph.edu.mobapde.meditake.meditake.fragment.schedule.add.AddScheduleMedicineFragment;
import ph.edu.mobapde.meditake.meditake.listener.CustomOnTimeSetListener;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class AddScheduleActivity extends AppCompatActivity
        implements  AddScheduleDetailsFragment.OnAddScheduleDetailsFragmentInteractionListener,
                    AddScheduleMedicineFragment.OnAddScheduleMedicineFragmentInteractionListener,
                    StepperLayout.StepperListener{


    AddScheduleStepAdapter mAddScheduleStepAdapter;
    ScheduleUtil scheduleUtil;

    @BindView(R.id.sl_add_schedule)
    StepperLayout slAddSchedule;
//    @BindView(R.id.toolbar)
//    Toolbar add_schedule_toolbar;
//    @BindView(R.id.container)
//    ViewPager mViewPager;
//    @BindView(R.id.tabs)
//    TabLayout tabLayout;
//    @BindView(R.id.ci_steps)
//    CircleIndicator ciSteps;


//    public void setUpActionBar(){
//        setSupportActionBar(add_schedule_toolbar);
//        getSupportActionBar().setTitle("Add Schedule");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_add_schedule);

        ButterKnife.bind(this);
//        setUpActionBar();

        scheduleUtil = new ScheduleUtil(getBaseContext());

        initializePager();
        initializeContents();

    }

    public void initializePager(){
//        mAddSchedulePagerAdapter = new AddSchedulePagerAdapter(getSupportFragmentManager());
//        mAddSchedulePagerAdapter.add(AddScheduleDetailsFragment.newInstance(1));
//        mAddSchedulePagerAdapter.add(AddScheduleMedicineFragment.newInstance(2));

//        mViewPager.setAdapter(mAddSchedulePagerAdapter);
        int[] attrs = {android.R.attr.colorBackground};
        TypedArray typedArray = obtainStyledAttributes(attrs);

        //mViewPager.setBackgroundColor(typedArray.getColor(0, Color.WHITE));
        //slAddSchedule.setAdapter();
        //ciSteps.setViewPager(mViewPager);
        //tabLayout.setupWithViewPager(mViewPager);
        mAddScheduleStepAdapter = new AddScheduleStepAdapter(getSupportFragmentManager(), getBaseContext());
        mAddScheduleStepAdapter.add(AddScheduleDetailsFragment.newInstance(1));
        mAddScheduleStepAdapter.add(AddScheduleMedicineFragment.newInstance(2));
        slAddSchedule.setAdapter(mAddScheduleStepAdapter, 0);
        slAddSchedule.setBackgroundColor(typedArray.getColor(0, Color.WHITE));
        slAddSchedule.setListener(this);
    }

    public void initializeContents(){
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.toolbar_add_medicine, menu);
        //......
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                finish();
                break;
        }

        return false;
    }

    /*********************
     * FRAGMENT LISTENERS
     *********************/
    @Override
    public void onAddScheduleDetailsFragmentTimeClick(TextView tvTime, TextView tvTimePeriod) {
        editTime(tvTime, tvTimePeriod);
    }

    @Override
    public void onAddScheduleDetailsFragmentCancel(){
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onAddScheduleDetailsFragmentRepeatClick(TextView tvRepeat) {
        editRepeatingTime(tvRepeat);
    }

    @Override
    public void onAddScheduleMedicineFragmentSave(Schedule schedule) {
        int id = (int) scheduleUtil.addNewSchedule(schedule);
        schedule.setSqlId(id);

        Intent data = new Intent();
        data.putExtra(Schedule.TABLE, schedule);
        setResult(RESULT_OK, data);
        finish();
    }

    /***********************
     * TIME PICKER FRAGMENT
     ***********************/

    public void editRepeatingTime(final TextView tvRepeat){
        BasicRepeatingTimePickerDialogFragment repeatingTimePickerDialogFragment
                = new BasicRepeatingTimePickerDialogFragment("Drinking Interval", "CHANGE", "CANCEL", String.valueOf((Object)tvRepeat.getText().toString()));

        repeatingTimePickerDialogFragment.setOnClickListener(new BasicRepeatingTimePickerDialogFragment.OnDialogClickListener() {
            @Override
            public void onPositiveSelected(int hourValue, int minuteValue) {
                tvRepeat.setText(DateUtil.parseToTimePickerDisplay(hourValue, minuteValue));
            }

            @Override
            public void onNegativeSelected() {

            }
        });

        repeatingTimePickerDialogFragment.show(getFragmentManager(), BasicRepeatingTimePickerDialogFragment.class.getSimpleName());
    }

    public void editTime(TextView tvTime, TextView tvTimePeriod){
        if(tvTime != null){
            int selectedHour = Integer.valueOf(tvTime.getText().toString().split(":")[0]);
            int selectedMinute = Integer.valueOf(tvTime.getText().toString().split(":")[1]);

            if(tvTimePeriod.getText().toString().toLowerCase().trim().equals("pm")){
                if(selectedHour != 12){
                    selectedHour+=12;
                }
            }else if(tvTimePeriod.getText().toString().toLowerCase().trim().equals("am")){
                if(selectedHour == 12){
                    selectedHour-=12;
                }
            }

            CustomOnTimeSetListener timeSet = new CustomOnTimeSetListener(getBaseContext(), tvTime, tvTimePeriod, false);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(AddScheduleActivity.this, timeSet, selectedHour, selectedMinute, false);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
    }

    /********************
     * STEPPERS LISTENER
     ********************/

    @Override
    public void onCompleted(View completeButton) {
    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {
        finish();
    }
}
