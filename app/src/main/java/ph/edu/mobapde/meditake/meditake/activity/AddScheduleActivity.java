package ph.edu.mobapde.meditake.meditake.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.Page.AddSchedulePagerAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.fragment.AddSchedule.AddScheduleDetailsFragment;
import ph.edu.mobapde.meditake.meditake.fragment.AddSchedule.AddScheduleMedicineFragment;
import ph.edu.mobapde.meditake.meditake.fragment.RepeatingTimePickerFragment;
import ph.edu.mobapde.meditake.meditake.listener.CustomOnTimeSetListener;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class AddScheduleActivity extends AppCompatActivity
        implements  RepeatingTimePickerFragment.OnRepeatingTimePickerFragmentInteractionListener,
                    AddScheduleDetailsFragment.OnAddScheduleDetailsFragmentInteractionListener,
                    AddScheduleMedicineFragment.OnAddScheduleMedicineFragmentInteractionListener{

    private AddSchedulePagerAdapter mAddSchedulePagerAdapter;

    @BindView(R.id.toolbar)
    Toolbar add_schedule_toolbar;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    ScheduleUtil scheduleUtil;

    FragmentTransaction ft;
    RepeatingTimePickerFragment repeatingTimePickerFragment;

    public void setUpActionBar(){
        setSupportActionBar(add_schedule_toolbar);
        getSupportActionBar().setTitle("Add Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_add_schedule);

        ButterKnife.bind(this);
        setUpActionBar();

        scheduleUtil = new ScheduleUtil(getBaseContext());

        initializePager();
        initializeContents();

    }

    public void initializePager(){
        mViewPager.setAdapter(mAddSchedulePagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        mAddSchedulePagerAdapter = new AddSchedulePagerAdapter(getSupportFragmentManager());
        mAddSchedulePagerAdapter.add(AddScheduleDetailsFragment.newInstance(1));
        mAddSchedulePagerAdapter.add(AddScheduleMedicineFragment.newInstance(2));

        mViewPager.setAdapter(mAddSchedulePagerAdapter);
    }

    public void initializeContents(){
        repeatingTimePickerFragment = new RepeatingTimePickerFragment();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add_medicine, menu);
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
    public void onRepeatingTimePickerFragmentCancel() {
        closeRepeatingTimePickerFragment();
    }

    @Override
    public void onRepeatingTimePickerFragmentSave() {
        closeRepeatingTimePickerFragment();
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

    public void editRepeatingTime(TextView tvRepeat){
        repeatingTimePickerFragment.attachTextView(tvRepeat);
        showRepeatingTimePickerFragment();
    }

    private void showRepeatingTimePickerFragment() {
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        ft.add(R.id.fragment_schedule_options_selection, repeatingTimePickerFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void closeRepeatingTimePickerFragment() {
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        ft.remove(repeatingTimePickerFragment);
        ft.addToBackStack(null);
        ft.commit();
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

}
