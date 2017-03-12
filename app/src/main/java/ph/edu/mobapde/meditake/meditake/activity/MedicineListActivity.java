package ph.edu.mobapde.meditake.meditake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.MedicineAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Capsule;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.db.SQLiteConnection;
import ph.edu.mobapde.meditake.meditake.util.DrawerManager;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class MedicineListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.rv_medicine)
    RecyclerView rvMedicine;

    @BindView(R.id.changeTheme)
    Button changeThemeButton;

    @BindView(R.id.randomMedicine)
    Button medicineButton;

    @BindView(R.id.toolbar_medicine_list)
    Toolbar medicine_list_toolbar;

    @BindView(R.id.drawer_layout_medicine_list)
    DrawerLayout drawer;

    MedicineAdapter medicineAdapter;
    SQLiteConnection connection;

    public void setUpActionBar(){
        setSupportActionBar(medicine_list_toolbar);
        getSupportActionBar().setTitle("Medicines");
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
                this, drawer, medicine_list_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);


        connection = new SQLiteConnection(getBaseContext());
        connection.createMedicine(new Capsule("Biogesic", "randomName", "Sickness", 0.0));

        medicineAdapter = new MedicineAdapter(getBaseContext(), connection.getAllMedicine());
        medicineAdapter.setOnItemClickListener(new MedicineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent i = new Intent(getBaseContext(), ViewMedicineActivity.class);
                i.putExtra(Medicine.COLUMN_ID, id);
                startActivity(i);
            }
        });

        rvMedicine.setAdapter(medicineAdapter);
        rvMedicine.setLayoutManager(new LinearLayoutManager(
                getBaseContext(), LinearLayoutManager.VERTICAL, false)
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_medicine_list, menu);
        return true;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.wtf("action", "Clicked something in app bar");
        switch(id){
            case R.id.action_add_new_medicine:
                addNewMedicine();
                Log.wtf("action", "GOING THERE");
                break;
            default: Toast.makeText(getBaseContext(), "Unexpected error encountered. Please try again", Toast.LENGTH_SHORT);
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerManager.execute(this, item);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addNewMedicine(){
        Intent i = new Intent(getBaseContext(), AddMedicineActivity.class);
        startActivity(i);
    }
/*
    @OnClick(R.id.fab_add_medicine)
    public void addMedicine(){
        Intent i = new Intent(getBaseContext(), AddMedicineActivity.class);
        startActivity(i);
    }
*/
}
