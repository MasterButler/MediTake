package ph.edu.mobapde.meditake.meditake.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.transition.TransitionManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.MedicineAdapter;
import ph.edu.mobapde.meditake.meditake.adapter.ScheduleAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.beans.Syrup;
import ph.edu.mobapde.meditake.meditake.listener.OnScheduleClickListener;
import ph.edu.mobapde.meditake.meditake.util.DrawerManager;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class ScheduleListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar_view_medicine)
    Toolbar view_schedule_toolbar;

    @BindView(R.id.drawer_layout_schedule_list)
    DrawerLayout drawer;

    @BindView(R.id.rv_schedule)
    RecyclerView rvSchedule;

    ScheduleAdapter scheduleAdapter;

    ScheduleUtil scheduleUtil;
    MedicineUtil medicineUtil;

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

        Medicine medicine = new Syrup();
        medicine.setBrandName("brand Medicine");
        medicine.setGenericName("generic Medicine");
        medicine.setMedicineFor("for something");
        medicine.setAmount(100);

        long medicineId = medicineUtil.addMedicine(medicine);

        Schedule schedule = new Schedule();
        schedule.setActivated(false);
        schedule.setLastTimeTaken(System.currentTimeMillis());
        schedule.setDrinkingInterval(1);
        schedule.setDosagePerDrinkingInterval(5);
        schedule.setMedicineToDrink(medicineUtil.getMedicine((int) medicineId));

        scheduleUtil.addNewSchedule(schedule);

        initializeDrawer();
        initializeAdapter();

        rvSchedule.setAdapter(scheduleAdapter);
        rvSchedule.setLayoutManager(new LinearLayoutManager(
                getBaseContext(), LinearLayoutManager.VERTICAL, false)
        );
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

            }

            @Override
            public void onItemEditClick(int id) {

            }

            @Override
            public void onItemSaveClick(Medicine medicine) {

            }

            @Override
            public void onItemCancelClick(int id) {

            }

            @Override
            public void onSwitchClick(int id) {

            }
        });
    }

    public void expand(int id){
//        if(CREATING_NEW_ITEM == -1){
            boolean isExpanded = scheduleAdapter.isExpanded(id);
            scheduleAdapter.setExpandedPositionId(isExpanded ? -1 : id);
            TransitionManager.beginDelayedTransition(rvSchedule);
            scheduleAdapter.notifyDataSetChanged();
//        }else{
//            delete((int)CREATING_NEW_ITEM);
//            CREATING_NEW_ITEM = -1;
//        }
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
}
