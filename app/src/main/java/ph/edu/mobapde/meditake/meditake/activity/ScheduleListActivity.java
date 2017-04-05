package ph.edu.mobapde.meditake.meditake.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.RecylerView.ScheduleAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.fragment.AddSchedule.AddScheduleDetailsFragment;
import ph.edu.mobapde.meditake.meditake.fragment.AddSchedule.AddScheduleMedicineFragment;
import ph.edu.mobapde.meditake.meditake.fragment.MedicineListFragment;
import ph.edu.mobapde.meditake.meditake.fragment.RepeatingTimePickerFragment;
import ph.edu.mobapde.meditake.meditake.fragment.ViewSchedule.ViewScheduleDetailsFragment;
import ph.edu.mobapde.meditake.meditake.fragment.ViewSchedule.ViewScheduleFragment;
import ph.edu.mobapde.meditake.meditake.listener.CustomOnTimeSetListener;
import ph.edu.mobapde.meditake.meditake.listener.OnScheduleClickListener;
import ph.edu.mobapde.meditake.meditake.service.AlarmReceiver;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;
import ph.edu.mobapde.meditake.meditake.util.DrawerManager;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class ScheduleListActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener,
                    RepeatingTimePickerFragment.OnRepeatingTimePickerFragmentInteractionListener,
                    ViewScheduleDetailsFragment.OnViewScheduleDetailsFragmentInteractionListener{

    public static final int REQUEST_ADD_SCHEDULE = 1;

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

    RepeatingTimePickerFragment repeatingTimePickerFragment;
    ViewScheduleFragment viewScheduleFragment;

    ScheduleAdapter scheduleAdapter;

    ScheduleUtil scheduleUtil;
    MedicineUtil medicineUtil;

    FragmentTransaction ft;

    int CREATING_NEW_ITEM;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ADD_SCHEDULE){
            if(resultCode == RESULT_OK) {
                Schedule addedScheudle = data.getExtras().getParcelable(Schedule.TABLE);
                updateList();
                if(addedScheudle.isActivated()) {
                    setAlarmForSchedule(addedScheudle);
                }
            }else if(resultCode == RESULT_CANCELED){
                showGenericSnackbar("Medicine creation cancelled", Snackbar.LENGTH_SHORT);
            }
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
            Log.wtf("NEW", "PREV DRINKING SCHED IS " + schedule.getNextDrinkingTime());
            Log.wtf("NEW", "DRINKING INTERVAL   IS " + schedule.getDrinkingInterval());
            Log.wtf("NEW", "NEXT TIME AFTER " + schedule.getDrinkingInterval()*DateUtil.MILLIS_TO_MINUTES);
            if(schedule.getDrinkingInterval() == 0){
                schedule.setActivated(false);
            }else{
                schedule.setNextDrinkingTime(schedule.getNextDrinkingTime() + schedule.getDrinkingInterval()*DateUtil.MILLIS_TO_MINUTES);
            }
            scheduleUtil.updateSchedule(schedule);
            updateList();
            setAlarmForSchedule(schedule);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    public void initializeDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, view_schedule_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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

        repeatingTimePickerFragment = RepeatingTimePickerFragment.newInstance();
    }

    public void initializeAdapter(){
        scheduleAdapter = new ScheduleAdapter(getBaseContext(), scheduleUtil.getAllSchedule(), Schedule.COLUMN_ID);
        scheduleAdapter.setHasStableIds(true);
        scheduleAdapter.setOnScheduleClickListener(new OnScheduleClickListener() {
            @Override
            public void onItemClick(int id) {
//                expand(id);
                Log.wtf("SCHEDLIST", "SCHEDULE IS " + scheduleUtil.getSchedule(id));
                viewScheduleFragment = ViewScheduleFragment.newInstance(scheduleUtil.getSchedule(id));
                ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                ft.replace(R.id.fragment_medicine_list_placeholder, viewScheduleFragment);
                ft.commit();
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
    }

    public void addNewSchedule(){
        if(CREATING_NEW_ITEM == -1) {
            //showAddScheduleFragment();
            Intent i = new Intent(getBaseContext(), AddScheduleActivity.class);
            startActivityForResult(i, REQUEST_ADD_SCHEDULE);
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
        showGenericSnackbar("Alarm set for " + DateUtil.convertToNotificationFormat(schedule.getNextDrinkingTime()) + " from now.", Snackbar.LENGTH_SHORT);
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra(Schedule.COLUMN_ID, schedule.getSqlId());
        PendingIntent pendingAlarm = PendingIntent
                .getBroadcast(
                        getBaseContext(),
                        schedule.getSqlId(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + DateUtil.getDelay(schedule.getNextDrinkingTime()), pendingAlarm);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public void updateList(){
        Cursor c = scheduleUtil.getAllSchedule();
        Log.d("count", "FOUND " + c.getCount() + " ROWS IN SCHEDULE");
        scheduleAdapter.changeCursor(c);
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
        }else if(viewScheduleFragment.isVisible()){
            closeViewScheduleFragment();
        }else{
            super.onBackPressed();
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
        ft.addToBackStack(null);
        ft.commit();

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
    }

    @Override
    public void onViewScheduleMedicineFragmentDelete(int id) {
        delete(id);
        updateList();
        closeViewScheduleFragment();
    }


    @Override
    public void onRepeatingTimePickerFragmentCancel() {
        closeRepeatingTimePickerFragment();
    }

    @Override
    public void onRepeatingTimePickerFragmentSave() {
        closeRepeatingTimePickerFragment();
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
            mTimePicker = new TimePickerDialog(ScheduleListActivity.this, timeSet, selectedHour, selectedMinute, false);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
    }
}
