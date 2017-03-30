package ph.edu.mobapde.meditake.meditake.activity;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.ScheduleAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.beans.Syrup;
import ph.edu.mobapde.meditake.meditake.fragment.AddScheduleFragment;
import ph.edu.mobapde.meditake.meditake.fragment.MedicineListFragment;
import ph.edu.mobapde.meditake.meditake.listener.CustomOnTimeSetListener;
import ph.edu.mobapde.meditake.meditake.listener.OnScheduleClickListener;
import ph.edu.mobapde.meditake.meditake.service.AlarmReceiver;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;
import ph.edu.mobapde.meditake.meditake.util.DrawerManager;
import ph.edu.mobapde.meditake.meditake.util.MedicineInstantiatorUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class ScheduleListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MedicineListFragment.OnDataPass, AddScheduleFragment.OnAddScheduleFragmentInteractionListener {

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

    MedicineListFragment medicineListFragment;
    AddScheduleFragment addScheduleFragment;

    ScheduleAdapter scheduleAdapter;

    ScheduleUtil scheduleUtil;
    MedicineUtil medicineUtil;

    int CREATING_NEW_ITEM;
    Schedule schedule;
    Medicine selectedMedicine;
    TextView tvToFragmentMedicineToDrink;

    public void setUpActionBar(){
        setSupportActionBar(view_schedule_toolbar);
        getSupportActionBar().setTitle("Schedules");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        CREATING_NEW_ITEM = -1;

        rvSchedule.setAdapter(scheduleAdapter);
        rvSchedule.setLayoutManager(new LinearLayoutManager(
                getBaseContext(), LinearLayoutManager.VERTICAL, false)
        );

        addScheduleFragment = new AddScheduleFragment();
    }

    public void addHardCodedData(){

//        Medicine medicine = new Syrup();
//        medicine.setBrandName("brand Medicine");
//        medicine.setGenericName("generic Medicine");
//        medicine.setMedicineFor("for something");
//        medicine.setAmount(100);
//
//        long medicineId = medicineUtil.addMedicine(medicine);
//        medicine.setSqlId((int) medicineId);

        Schedule schedule = new Schedule();

        scheduleUtil.addNewSchedule(schedule);
    }

    public void initializeAdapter(){
        scheduleAdapter = new ScheduleAdapter(getBaseContext(), scheduleUtil.getAllSchedule(), Schedule.COLUMN_ID);
        scheduleAdapter.setHasStableIds(true);
        scheduleAdapter.setOnScheduleClickListener(new OnScheduleClickListener() {
            @Override
            public void onItemClick(int id) {
                expand(id);
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
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            ft.replace(R.id.fragment_medicine_list_placeholder, addScheduleFragment);
            ft.addToBackStack(null);
            ft.commit();
            } else {
                Toast.makeText(getBaseContext(), "No medicines in list. Adding Schedule without any medicine not yet supported.", Toast.LENGTH_LONG).show();
            }
    }

    public void cancel(int id){
        if(CREATING_NEW_ITEM == -1){
            returnToView(id);
        }else{
            delete((int)CREATING_NEW_ITEM);
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

    public void editTime(TextView tvTime, TextView tvTimePeriod){
        if(tvTime != null){
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                CustomOnTimeSetListener timeSet = new CustomOnTimeSetListener(getBaseContext(), tvTime, tvTimePeriod, tvTimePeriod==null);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ScheduleListActivity.this, timeSet, hour, minute, tvTimePeriod==null);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
        }
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
        returnToView(schedule.getSqlId());

        if(CREATING_NEW_ITEM != -1)
            CREATING_NEW_ITEM = -1;
    }

    public void showMedicineList(Schedule schedule, TextView tvToFragmentMedicineToDrink){
        this.tvToFragmentMedicineToDrink = tvToFragmentMedicineToDrink;
        this.schedule = schedule;

        medicineListFragment = new MedicineListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.add(R.id.fragment_medicine_list_placeholder, medicineListFragment);
        ft.commit();

        Toast.makeText(getBaseContext(), medicineListFragment.isVisible() + " << ", Toast.LENGTH_SHORT).show();

    }

    public void closeMedicineList(){
        getSupportFragmentManager().beginTransaction().remove(medicineListFragment).commit();
        Log.d("action", "REMOVED");

        this.tvToFragmentMedicineToDrink = null;
        this.schedule = null;
    }

    public void toggleSwitch(Schedule schedule){
        Log.d("click", "ON SWITCH");
        schedule.setActivated(!schedule.isActivated());
        Log.d("com", "actual interval is " + schedule.getDrinkingInterval());
        Log.d("com", "switching to " + schedule.isActivated());
        scheduleUtil.updateSchedule(schedule);
        scheduleAdapter.notifyDataSetChanged();
        updateList();


        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra(Schedule.TABLE, schedule);
        PendingIntent pendingAlarm = PendingIntent.getBroadcast(getBaseContext(), AlarmReceiver.PENDING_ALARMRECEIVER, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5 * 1000, pendingAlarm);
    }

    public void updateList(){
        Cursor c = scheduleUtil.getAllSchedule();
        Log.d("count", "FOUND " + c.getCount() + " ROWS IN SCHEDULE");
        scheduleAdapter.changeCursor(c);
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

    public void closeAddScheduleFragment(){
        getSupportFragmentManager().beginTransaction().remove(addScheduleFragment).commit();
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
        } else {
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerManager.execute(this, item);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDataPass(Medicine medicine) {
        selectedMedicine = medicine;
//        schedule.setMedicineToDrink(selectedMedicine);
        this.tvToFragmentMedicineToDrink.setText(medicine.getModifier() + " of " + medicine.getName());
        closeMedicineList();
    }

    @Override
    public void onFragmentTimeClick(TextView tvTime, TextView tvTimePeriod) {
        editTime(tvTime, tvTimePeriod);
    }

    @Override
    public void onFragmentSave(Schedule schedule) {
        scheduleUtil.addNewSchedule(schedule);
        updateList();
        closeAddScheduleFragment();
    }

    @Override
    public void onFragmentCancel(){
        closeAddScheduleFragment();
    }
}
