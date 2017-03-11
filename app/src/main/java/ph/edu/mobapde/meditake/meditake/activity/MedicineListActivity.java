package ph.edu.mobapde.meditake.meditake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class MedicineListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    @BindView(R.id.fab_add_medicine)
//    FloatingActionButton fab_add_medicine;

    @BindView(R.id.changeTheme)
    Button changeThemeButton;

    @BindView(R.id.randomMedicine)
    Button medicineButton;

    @BindView(R.id.toolbar_view_medicine)
    Toolbar view_medicine_toolbar;

    @BindView(R.id.drawer_layout_medicine_list)
    DrawerLayout drawer;

    public void setUpActionBar(){
        setSupportActionBar(view_medicine_toolbar);
        getSupportActionBar().setTitle("Schedules");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_medicine_list);

        ButterKnife.bind(this);
        setUpActionBar();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, view_medicine_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_medicine_list);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
/*
    @OnClick(R.id.fab_add_medicine)
    public void addMedicine(){
        Intent i = new Intent(getBaseContext(), AddMedicineActivity.class);
        startActivity(i);
    }
*/
    @OnClick(R.id.changeTheme)
    public void changeTheme(){
        int newTheme = ThemeUtil.getSelectedTheme()+1;
        ThemeUtil.changeToTheme(this, newTheme%2);
        Toast.makeText(getBaseContext(), "Changing theme", Toast.LENGTH_SHORT);
    }

    @OnClick(R.id.randomMedicine)
    public void viewMedicineInfo(){
        Intent i = new Intent();
        i.setClass(getBaseContext(), ViewMedicineActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_schedule) {
            Intent i = new Intent(getBaseContext(), ScheduleListActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_medicine) {
            //do nothing
        } else if(id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_medicine_list);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
