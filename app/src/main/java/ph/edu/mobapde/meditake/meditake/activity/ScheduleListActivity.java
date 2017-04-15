package ph.edu.mobapde.meditake.meditake.activity;

import android.app.TimePickerDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.RequestCodes;
import ph.edu.mobapde.meditake.meditake.adapter.recyclerview.ScheduleAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.dialog.BasicRepeatingTimePickerDialogFragment;
import ph.edu.mobapde.meditake.meditake.fragment.schedule.view.ViewScheduleDetailsFragment;
import ph.edu.mobapde.meditake.meditake.fragment.schedule.view.ViewScheduleFragment;
import ph.edu.mobapde.meditake.meditake.listener.CustomOnTimeSetListener;
import ph.edu.mobapde.meditake.meditake.listener.OnScheduleClickListener;
import ph.edu.mobapde.meditake.meditake.util.AlarmUtil;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;
import ph.edu.mobapde.meditake.meditake.util.DrawerUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class ScheduleListActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener,
                    ViewScheduleDetailsFragment.OnViewScheduleDetailsFragmentInteractionListener{

    public static final float alphaValue = 0.54f;

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

    @BindView(R.id.black_overlay)
    View blackOverlay;

    ViewScheduleFragment viewScheduleFragment;

    ScheduleAdapter scheduleAdapter;

    ScheduleUtil scheduleUtil;
    MedicineUtil medicineUtil;

    FragmentTransaction ft;

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
                rvSchedule.smoothScrollToPosition(scheduleAdapter.getItemCount()-1);
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
            int snoozeTime = data.getInt(getString(R.string.schedule_snooze));
            if(snoozeTime == 0){
                setNextDrinking(schedule);
//                    view(schedule.getSqlId());
            }else {
                setSnooze(schedule, snoozeTime);
            }
            scheduleAdapter.notifyDataSetChanged();
            updateList();
        }
    }

    public void setSnooze(Schedule schedule, int snoozeTime){
        schedule.setNextDrinkingTime(schedule.getNextDrinkingTime() + snoozeTime * DateUtil.MILLIS_TO_MINUTES);

        scheduleUtil.updateSchedule(schedule);
        scheduleAdapter.notifyDataSetChanged();
        updateList();
        setAlarmForSchedule(schedule);
    }

    public void setNextDrinking(Schedule schedule){
        Log.wtf("NEW", "PREV DRINKING SCHED IS " + schedule.getNextDrinkingTime());
        Log.wtf("NEW", "DRINKING INTERVAL   IS " + schedule.getDrinkingInterval());
        Log.wtf("NEW", "NEXT TIME AFTER " + schedule.getDrinkingInterval() * DateUtil.MILLIS_TO_MINUTES);

        if (schedule.getDrinkingInterval() == 0) {
            schedule.setActivated(false);
            AlarmUtil.stopAssociatedAlarmsWithSchedule(getBaseContext(), schedule);
        } else {
            schedule.setNextDrinkingTime(schedule.getNextDrinkingTime() + schedule.getDrinkingInterval() * DateUtil.MILLIS_TO_MINUTES);
            setAlarmForSchedule(schedule);
        }
        scheduleUtil.updateSchedule(schedule);
        scheduleAdapter.notifyDataSetChanged();
        updateList();
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

        checkIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            public void onSwitchClick(Schedule schedule) {
                toggleSwitch(schedule);
            }
        });
    }

    public void addNewSchedule(){
        Intent i = new Intent(getBaseContext(), AddScheduleActivity.class);
        startActivityForResult(i, RequestCodes.REQUEST_ADD_SCHEDULE);
    }

    public void delete(int id){
        AlarmUtil.stopAssociatedAlarmsWithSchedule(getBaseContext(), scheduleUtil.getSchedule(id));
        scheduleUtil.deleteSchedule(id);
        scheduleAdapter.notifyDataSetChanged();
        updateList();
        Toast.makeText(getBaseContext(), R.string.notf_schedule_delete, Toast.LENGTH_SHORT).show();
    }


    public void save(Schedule schedule){
        Log.wtf("action", "TO UPDATE SCHEDULE WITH ID " + schedule.getSqlId());
        scheduleUtil.updateSchedule(schedule);
        scheduleAdapter.notifyDataSetChanged();

        updateList();
        closeViewScheduleFragment();
    }

    public void toggleSwitch(Schedule schedule){
        schedule.setActivated(!schedule.isActivated());
        scheduleUtil.updateSchedule(schedule);
        scheduleAdapter.notifyDataSetChanged();
        updateList();

        if(schedule.isActivated()){
            setAlarmForSchedule(schedule);
        }else{
            AlarmUtil.stopAssociatedAlarmsWithSchedule(getBaseContext(), schedule);
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
        TransitionManager.beginDelayedTransition(rvSchedule);

        int rvVisibility = scheduleAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE;
        int linVisibility = scheduleAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE;

        rvSchedule.setVisibility(rvVisibility);
        linRvEmpty.setVisibility(linVisibility);
    }


    public void view(int id){
        Log.wtf("SCHEDLIST", "SCHEDULE IS " + scheduleUtil.getSchedule(id));
        viewScheduleFragment = ViewScheduleFragment.newInstance(scheduleUtil.getSchedule(id));
        viewScheduleFragment.setOnViewScheduleFragmentInteractionListener(new ViewScheduleFragment.OnViewScheduleFragmentInteractionListener() {
            @Override
            public void onViewScheduleBackgroundClick(Schedule schedule) {
                Log.wtf("????", "???????????????????");
                save(schedule);
                closeViewScheduleFragment();
                setAlarmForSchedule(schedule);
            }
        });
        blackOverlay.animate().alpha(alphaValue);
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down);
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
        DrawerUtil.execute(this, item);
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
        getFragmentManager().popBackStack();
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, android.R.anim.fade_in, android.R.anim.fade_out);
        ft.hide(viewScheduleFragment);
//        ft.remove(viewScheduleFragment);
        ft.commit();

        blackOverlay.animate().alpha(0.0f);
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


    /**********************************
     * REPEATING TIME PICKER FRAGMENT
     **********************************/

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

    /***********************
     * TIME PICKER FRAGMENT
     ***********************/

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
