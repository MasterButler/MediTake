package ph.edu.mobapde.meditake.meditake.activity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.RequestCodes;
import ph.edu.mobapde.meditake.meditake.adapter.RecylerView.ScheduleAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.fragment.ViewSchedule.ViewScheduleDetailsFragment;
import ph.edu.mobapde.meditake.meditake.fragment.ViewSchedule.ViewScheduleFragment;
import ph.edu.mobapde.meditake.meditake.listener.CustomOnTimeSetListener;
import ph.edu.mobapde.meditake.meditake.listener.OnScheduleClickListener;
import ph.edu.mobapde.meditake.meditake.util.AlarmUtil;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;
import ph.edu.mobapde.meditake.meditake.util.DrawerManager;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class ScheduleListActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener,
        ViewScheduleDetailsFragment.OnViewScheduleDetailsFragmentInteractionListener{


    @BindView(R.id.snackbar_position)
    CoordinatorLayout clSnackbar;

    @BindView(R.id.layout_schedule_list)
    CoordinatorLayout scheduleListLayout;

    @BindView(R.id.fab_add_schedule)
    FloatingActionButton addSchedule;

    @BindView(R.id.toolbar_view_medicine)
    Toolbar view_schedule_toolbar;

    @BindView(R.id.drawer_layout_schedule_list)
    DrawerLayout drawer;

    @BindView(R.id.rv_schedule)
    RecyclerView rvSchedule;

    @BindView(R.id.lin_rv_schedule_empty)
    LinearLayout linRvEmpty;

    ViewScheduleFragment viewScheduleFragment;

    ScheduleAdapter scheduleAdapter;

    ScheduleUtil scheduleUtil;
    MedicineUtil medicineUtil;

    FragmentTransaction ft;

    int CREATING_NEW_ITEM;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RequestCodes.REQUEST_ADD_SCHEDULE){
            if(resultCode == RESULT_OK) {
                Schedule addedScheudle = data.getExtras().getParcelable(Schedule.TABLE);
                updateList();
                if(addedScheudle.isActivated()) {
                    setAlarmForSchedule(addedScheudle);
                }
            }else if(resultCode == RESULT_CANCELED){
                showGenericSnackbar("Medicine creation cancelled", Snackbar.LENGTH_SHORT);
            }
        }else if(requestCode == RequestCodes.REQUEST_SETTINGS_UPDATE){
            ThemeUtil.reloadWithTheme(this);
        }
    }

    public void setUpActionBar(){
        setSupportActionBar(view_schedule_toolbar);
        getSupportActionBar().setTitle("Schedules");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void checkIntent(){
        Bundle data = getIntent().getExtras();

        if(data != null) {
            Schedule schedule = data.getParcelable(Schedule.TABLE);
            if(data.get(getString(R.string.schedule_snooze)) == null){
                setNextDrinking(schedule);
//                view(schedule.getSqlId());
            }else{
                int snoozeTime = data.getInt(getString(R.string.schedule_snooze));
                if(snoozeTime == 0){
                    setNextDrinking(schedule);
//                    view(schedule.getSqlId());
                }else {
                    setSnooze(schedule, snoozeTime);
                }
            }
        }
    }

    public void setSnooze(Schedule schedule, int snoozeTime){
        snoozeTime = snoozeTime == 0 ? 5 : snoozeTime;
        schedule.setNextDrinkingTime(schedule.getNextDrinkingTime() + snoozeTime * DateUtil.MILLIS_TO_MINUTES);

        scheduleUtil.updateSchedule(schedule);
        updateList();
        setAlarmForSchedule(schedule);
    }

    public void setNextDrinking(Schedule schedule){
        Log.wtf("NEW", "PREV DRINKING SCHED IS " + schedule.getNextDrinkingTime());
        Log.wtf("NEW", "DRINKING INTERVAL   IS " + schedule.getDrinkingInterval());
        Log.wtf("NEW", "NEXT TIME AFTER " + schedule.getDrinkingInterval() * DateUtil.MILLIS_TO_MINUTES);
        if (schedule.getDrinkingInterval() == 0) {
            schedule.setActivated(false);
        } else {
            schedule.setNextDrinkingTime(schedule.getNextDrinkingTime() + schedule.getDrinkingInterval() * DateUtil.MILLIS_TO_MINUTES);
        }
        scheduleUtil.updateSchedule(schedule);
        updateList();
        setAlarmForSchedule(schedule);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf("ONCREATE", "SCHEDULELISTACTIVITY");
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_schedule_list);

        ButterKnife.bind(this);
        setUpActionBar();

        scheduleUtil = new ScheduleUtil(getBaseContext());
        medicineUtil = new MedicineUtil(getBaseContext());

        //addHardCodedData();
        initializeDrawer();
        initializeAdapter();
        initializeContent();
        CREATING_NEW_ITEM = -1;

        checkIntent();
    }

    public void initializeDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, view_schedule_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ArrayList<Integer> navHeaders = new ArrayList<>();
        navHeaders.add(R.drawable.drawer_wallpaper_1);
        navHeaders.add(R.drawable.drawer_wallpaper_2);
        navHeaders.add(R.drawable.drawer_wallpaper_3);
        navHeaders.add(R.drawable.drawer_wallpaper_4);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getHeaderView(0).findViewById(R.id.nav_header).setBackgroundResource(navHeaders.get(ThemeUtil.getSelectedTheme(getBaseContext())-1));
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

    }

    public void initializeContent(){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        rvSchedule.setAdapter(scheduleAdapter);
        rvSchedule.setLayoutManager(mLayoutManager);

        int rvVisibility = scheduleAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE;
        int linVisibility = scheduleAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE;

        rvSchedule.setVisibility(rvVisibility);
        linRvEmpty.setVisibility(linVisibility);
    }

    public void initializeAdapter(){
        scheduleAdapter = new ScheduleAdapter(getBaseContext(), scheduleUtil.getAllSchedule(), Schedule.COLUMN_ID);
        scheduleAdapter.setHasStableIds(true);
        scheduleAdapter.setOnScheduleClickListener(new OnScheduleClickListener() {
            @Override
            public void onItemClick(int id) {
//                expand(id);
                view(id);
            }

            @Override
            public void onItemDeleteClick(int id) {
                delete(id);
            }

            @Override
            public void onItemEditClick(int id) {
                edit(id);
            }

            @Override
            public void onItemSaveClick(Schedule schedule) {
                save(schedule);
            }

            @Override
            public void onItemCancelClick(int id) {
                cancel(id);
            }

            @Override
            public void onSwitchClick(Schedule schedule) {
                toggleSwitch(schedule);
            }
        });
    }

    private void returnToView(int id) {
        boolean isEditing = scheduleAdapter.isEditing(id);
        scheduleAdapter.setEditingPositionId(isEditing ? -1 : id);
        scheduleAdapter.notifyDataSetChanged();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(viewScheduleFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void addNewSchedule(){
        if(CREATING_NEW_ITEM == -1) {
            //showAddScheduleFragment();
            Intent i = new Intent(getBaseContext(), AddScheduleActivity.class);
            startActivityForResult(i, RequestCodes.REQUEST_ADD_SCHEDULE);
        } else {
            Toast.makeText(getBaseContext(), "No medicines in list. Adding Schedule without any medicine not yet supported.", Toast.LENGTH_LONG).show();
        }
    }

    public void cancel(int id){
        if(CREATING_NEW_ITEM == -1){
            returnToView(id);
        }else{
            delete(CREATING_NEW_ITEM);
            CREATING_NEW_ITEM = -1;
        }
    }

    public void delete(int id){
        AlarmUtil.stopAssociatedAlarmsWithSchedule(getBaseContext(), scheduleUtil.getSchedule(id));
        scheduleUtil.deleteSchedule(id);
        scheduleAdapter.notifyDataSetChanged();
        updateList();
        Toast.makeText(getBaseContext(), R.string.notf_schedule_delete, Toast.LENGTH_SHORT).show();
    }

    public void edit(int id){
        boolean isEditing = scheduleAdapter.isEditing(id);
        Log.wtf("action", "IS EDITING (AT ID" + id + ")? " + isEditing);
        scheduleAdapter.setEditingPositionId(isEditing ? -1 : id);
        scheduleAdapter.notifyDataSetChanged();
    }

    public void expand(int id){
        if(CREATING_NEW_ITEM == -1){
            boolean isExpanded = scheduleAdapter.isExpanded(id);
            scheduleAdapter.setExpandedPositionId(isExpanded ? -1 : id);
            TransitionManager.beginDelayedTransition(rvSchedule);
            scheduleAdapter.notifyDataSetChanged();
        }else{
            delete((int)CREATING_NEW_ITEM);
            CREATING_NEW_ITEM = -1;
        }
    }

    public void save(Schedule schedule){
        Log.wtf("action", "TO UPDATE SCHEDULE WITH ID " + schedule.getSqlId());
        scheduleUtil.updateSchedule(schedule);
        scheduleAdapter.notifyDataSetChanged();

        updateList();
        if(scheduleAdapter.isEditing(schedule.getSqlId()))
            returnToView(schedule.getSqlId());

        if(CREATING_NEW_ITEM != -1)
            CREATING_NEW_ITEM = -1;
    }

    public void toggleSwitch(Schedule schedule){
        schedule.setActivated(!schedule.isActivated());
        scheduleUtil.updateSchedule(schedule);
        scheduleAdapter.notifyDataSetChanged();
        updateList();

        if(schedule.isActivated()){
            setAlarmForSchedule(schedule);
        }
    }

    private void setAlarmForSchedule(Schedule schedule) {
        if(schedule.isActivated()) {
            AlarmUtil.setAlarmForSchedule(getBaseContext(), schedule);
            showGenericSnackbar("Alarm set for " + DateUtil.convertToNotificationFormat(schedule.getNextDrinkingTime()) + " from now.", Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public void updateList(){
        Cursor c = scheduleUtil.getAllSchedule();
        Log.d("count", "FOUND " + c.getCount() + " ROWS IN SCHEDULE");
        scheduleAdapter.changeCursor(c);

        int rvVisibility = scheduleAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE;
        int linVisibility = scheduleAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE;

        rvSchedule.setVisibility(rvVisibility);
        linRvEmpty.setVisibility(linVisibility);
    }


    public void view(int id){
        Log.wtf("SCHEDLIST", "SCHEDULE IS " + scheduleUtil.getSchedule(id));
        viewScheduleFragment = ViewScheduleFragment.newInstance(scheduleUtil.getSchedule(id));
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        ft.replace(R.id.fragment_medicine_list_placeholder, viewScheduleFragment);
        ft.commit();
    }

    @OnClick(R.id.fab_add_schedule)
    public void addSchedule(){
        Log.d("action", "ADDING NEW SCHEDULE");
        addNewSchedule();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(viewScheduleFragment != null && viewScheduleFragment.isVisible()){
            closeViewScheduleFragment();
        }else{
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_schedule_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerManager.execute(this, item);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showGenericSnackbar(String message, int length){
        int[] attrs = {android.R.attr.color};
        Snackbar snackbarNotify = Snackbar.make(clSnackbar, message, length);

        TypedArray typedArray = obtainStyledAttributes(attrs);

        TextView snackBarNotifyTextView = (TextView) snackbarNotify.getView().findViewById(android.support.design.R.id.snackbar_text);
        snackBarNotifyTextView.setTextColor(typedArray.getColor(0, Color.WHITE));

        snackbarNotify.show();
    }

    public void closeViewScheduleFragment(){
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        ft.remove(viewScheduleFragment);
        ft.commit();
        getFragmentManager().popBackStack();
    }

    @Override
    public void onViewScheduleDetailsFragmentRepeatClick(TextView tvRepeat) {
        editRepeatingTime(tvRepeat);
    }

    @Override
    public void onViewScheduleDetailsFragmentTimeClick(TextView tvTime, TextView tvTimePeriod) {
        editTime(tvTime, tvTimePeriod);
    }

    @Override
    public void onViewScheduleDetailsFragmentClose(Schedule schedule) {
        save(schedule);
        updateList();
        closeViewScheduleFragment();
        setAlarmForSchedule(schedule);
    }

    @Override
    public void onViewScheduleMedicineFragmentDelete(int id) {
        delete(id);
        updateList();
        closeViewScheduleFragment();
    }


    /***********************
     * TIME PICKER FRAGMENT
     ***********************/

    public void editRepeatingTime(final TextView tvRepeat){
        final AlertDialog.Builder alert = new AlertDialog.Builder(ScheduleListActivity.this);
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
                int[] attrs = {android.R.attr.colorPrimary};
                TypedArray typedArray = ScheduleListActivity.this.obtainStyledAttributes(attrs);
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
            mTimePicker = new TimePickerDialog(ScheduleListActivity.this, timeSet, selectedHour, selectedMinute, false);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
    }

}
