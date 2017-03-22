package ph.edu.mobapde.meditake.meditake.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.transition.TransitionManager;
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
        implements NavigationView.OnNavigationItemSelectedListener, MedicineListFragment.OnDataPass{


    @BindView(R.id.fab_add_schedule)
    FloatingActionButton addSchedule;

    @BindView(R.id.toolbar_view_medicine)
    Toolbar view_schedule_toolbar;

    @BindView(R.id.drawer_layout_schedule_list)
    DrawerLayout drawer;

    @BindView(R.id.rv_schedule)
    RecyclerView rvSchedule;

    MedicineListFragment medicineListFragment;

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

    }

    public void addHardCodedData(){

        Medicine medicine = new Syrup();
        medicine.setBrandName("brand Medicine");
        medicine.setGenericName("generic Medicine");
        medicine.setMedicineFor("for something");
        medicine.setAmount(100);

        long medicineId = medicineUtil.addMedicine(medicine);
        medicine.setSqlId((int) medicineId);

        Schedule schedule = new Schedule();
        schedule.setActivated(false);
        schedule.setLastTimeTaken(System.currentTimeMillis());
        schedule.setDrinkingInterval(1);
        schedule.setDosagePerDrinkingInterval(5);
        schedule.setMedicineToDrink(medicine);

        Schedule scheduleB = new Schedule();
        scheduleB.setActivated(false);
        scheduleB.setLastTimeTaken(System.currentTimeMillis());
        scheduleB.setDrinkingInterval(1);
        scheduleB.setDosagePerDrinkingInterval(5);
        scheduleB.setMedicineToDrink(null);

        scheduleUtil.addNewSchedule(schedule);
        scheduleUtil.addNewSchedule(scheduleB);

    }

    public void initializeAdapter(){
        scheduleAdapter = new ScheduleAdapter(getBaseContext(), scheduleUtil.getAllSchedule());
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

            @Override
            public void onEditTimeClick(Schedule schedule, EditText etTime, TextView tvTimePeriod, boolean isMilitary) {
                editTime(schedule, etTime, tvTimePeriod, isMilitary);
            }

            @Override
            public void onMedicineListClick(Schedule schedule, TextView tvToFragmentMedicineToDrink) {
                Log.d("action", "ON MEDICINE LIST CLICK");
                showMedicineList(schedule, tvToFragmentMedicineToDrink);
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
            Medicine medicine = medicineUtil.getMedicineFirstRow();
            if (medicine != null) {
                Schedule tempSched = new Schedule();
                tempSched.setActivated(true);
                tempSched.setDosagePerDrinkingInterval(0);
                tempSched.setDrinkingInterval(0);
                tempSched.setLastTimeTaken(0);
                tempSched.setMedicineToDrink(medicine);
                tempSched.setCustomNextDrinkingTime(0);
                long tempId = scheduleUtil.addNewSchedule(tempSched);

                Log.d("action", "FOUND MEDICINE OF VALUE " + medicine.getSqlId() + " WITH NAME " + medicine.getBrandName());

//                Log.d("action", "ADDING SCHEDULE WITH ID " + tempId);

                updateList();
                rvSchedule.smoothScrollToPosition(scheduleAdapter.getItemCount() - 1);
                expand((int) scheduleAdapter.getItemId(scheduleAdapter.getItemCount() - 1));
                edit((int) scheduleAdapter.getItemId(scheduleAdapter.getItemCount() - 1));
                CREATING_NEW_ITEM = (int) tempId;
//                expand((int) scheduleAdapter.getItemId(0));
//                edit((int) scheduleAdapter.getItemId(0));
//                if(scheduleAdapter.getItemCount() > 0){
//
//                }else{
//                    Toast.makeText(getBaseContext(), "Error creating schedule.", Toast.LENGTH_LONG).show();
//                }
            } else {
                Toast.makeText(getBaseContext(), "No medicines in list. Adding Schedule without any medicine not yet supported.", Toast.LENGTH_LONG).show();
            }
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

    public void editTime(Schedule schedule, EditText etTime, TextView tvTimePeriod, boolean isMilitary) {
        // TODO Auto-generated method stub
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        Log.d("check", "IS MILITARY IS " + isMilitary);
        CustomOnTimeSetListener timeSet = new CustomOnTimeSetListener(getBaseContext(), etTime, tvTimePeriod, isMilitary);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ScheduleListActivity.this, timeSet, hour, minute, isMilitary);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
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
        ft.replace(R.id.fragment_medicine_list_placeholder, medicineListFragment);
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
        if(schedule.isActivated()){
            Toast.makeText(getBaseContext(), "Alarm set for " + DateUtil.getDifferenceInMinutes(System.currentTimeMillis(), schedule.getNextDrinkingTime()) + " from now.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
            intent.putExtra(getString(R.string.SCHEDULE_ID), (int)schedule.getSqlId());
            intent.putExtra(getString(R.string.MEDICINE_ID), (int)schedule.getMedicineToDrink().getSqlId());

            PendingIntent pendingAlarm = PendingIntent.getBroadcast(getBaseContext(), AlarmReceiver.PENDING_ALARMRECEIVER, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            long delay = schedule.getNextDrinkingTime() - System.currentTimeMillis();
            Log.d("action", "DELAY IS " + delay + " milliseconds (" + (delay/DateUtil.MILLIS_TO_SECONDS) + ")");
            alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 5 * 1000, pendingAlarm);
        }
        updateList();
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
        schedule.setMedicineToDrink(selectedMedicine);
        this.tvToFragmentMedicineToDrink.setText(medicine.getModifier() + " of " + medicine.getName());
        closeMedicineList();
    }

}
