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
        final AlertDialog.Builder alert = new AlertDialog.Builder(AddScheduleActivity.this);
        View repeatingTimeSelection = this.getLayoutInflater().inflate(R.layout.fragment_repeating_time_picker, null);

        ButterKnife.bind(repeatingTimeSelection, this);

        Log.wtf("ACTION", "OPENING THE SELCTOR");

        final NumberPicker npHour = (NumberPicker) repeatingTimeSelection.findViewById(R.id.number_picker_hours);
        final NumberPicker npMinutes = (NumberPicker) repeatingTimeSelection.findViewById(R.id.number_picker_minutes);


        npHour.setMinValue(0);
        npHour.setMaxValue(168);
        npMinutes.setMinValue(0);
        npMinutes.setMaxValue(59);

        if(tvRepeat != null){
            int hourValue = 0;
            int minuteValue = 1;
            int[] timeValues;
            if(!tvRepeat.getText().toString().equals(DateUtil.REPEATING_TIME_NOT_SET)){
                timeValues = DateUtil.parseFromTimePicker(tvRepeat.getText().toString());

                Log.wtf("GOT VALUES", timeValues[hourValue] + " hour(s) and " + timeValues[minuteValue] + " minutes");
                npHour.setValue(npHour.getMinValue() <= timeValues[hourValue] && npHour.getMaxValue() >= timeValues[hourValue] ? timeValues[hourValue] : 0);
                npMinutes.setValue(npMinutes.getMinValue() <= timeValues[minuteValue] && npMinutes.getMaxValue() >= timeValues[minuteValue] ? timeValues[minuteValue] : 0);
            }else{
                timeValues = new int[]{0, 0};
            }
        }

        npMinutes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(oldVal == npMinutes.getMinValue() && newVal == npMinutes.getMaxValue()){
                    npHour.setValue(npHour.getValue() != npHour.getMinValue() ? npHour.getValue()-1 : npHour.getValue());
                }else if(oldVal == npMinutes.getMaxValue() && newVal == npMinutes.getMinValue()){
                    npHour.setValue(npHour.getValue() != npHour.getMaxValue() ? npHour.getValue()+1 : npHour.getValue());
                }
            }
        });

        alert.setView(repeatingTimeSelection);
        alert.setTitle("Drinking Interval");
        alert.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                tvRepeat.setText(DateUtil.parseToTimePickerDisplay(npHour.getValue(), npMinutes.getValue()));
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = alert.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                int[] attrs = {android.R.attr.colorAccent};
                TypedArray typedArray = AddScheduleActivity.this.obtainStyledAttributes(attrs);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(typedArray.getColor(0, Color.BLACK));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(typedArray.getColor(0, Color.BLACK));
            }
        });
        dialog.show();
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
