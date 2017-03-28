package ph.edu.mobapde.meditake.meditake.activity;

import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.MedicineAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Capsule;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.Syrup;
import ph.edu.mobapde.meditake.meditake.beans.Tablet;
import ph.edu.mobapde.meditake.meditake.listener.OnMedicineClickListener;
import ph.edu.mobapde.meditake.meditake.util.DrawerManager;
import ph.edu.mobapde.meditake.meditake.util.MedicineInstantiatorUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.SearchUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class MedicineListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.rv_medicine)
    RecyclerView rvMedicine;

    @BindView(R.id.fab_add_medicine)
    FloatingActionsMenu addMedicineMenu;

    @BindView(R.id.white_overlay)
    RelativeLayout whiteOverlay;

    @BindView(R.id.toolbar_medicine_list)
    Toolbar medicine_list_toolbar;

    @BindView(R.id.drawer_layout_medicine_list)
    DrawerLayout drawer;

    MenuItem actionSearchMedicineIcon;
    SearchView actionSearchMedicineMenu;


    MedicineAdapter medicineAdapter;
    MedicineUtil medicineUtil;

    long CREATING_NEW_ITEM;

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

        medicineUtil = new MedicineUtil(getBaseContext());

        initializeDrawer();
        initializeAdapter();
        initializeFAM();
        CREATING_NEW_ITEM = -1;

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        rvMedicine.setAdapter(medicineAdapter);
        rvMedicine.setLayoutManager(mLayoutManager);


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int value = (int) viewHolder.getItemId();
                delete(value);
                updateList();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvMedicine);
    }

    public void initializeAdapter(){
        medicineAdapter = new MedicineAdapter(getBaseContext(), medicineUtil.getAllMedicine());
        medicineAdapter.setHasStableIds(true);

        medicineAdapter.setOnMedicineClickListener(new OnMedicineClickListener() {
            @Override
            public void onItemClick(int id) {
                expand(id);
            }

            @Override
            public void onItemDeleteClick(int id) {
                delete(id);
                Toast.makeText(getBaseContext(), R.string.notif_medicine_delete, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemEditClick(int id) {
                Log.wtf("action", "RECEIVED ID " + id);
                edit(id);
            }

            @Override
            public void onItemSaveClick(Medicine medicine) {
                save(medicine);
            }

            @Override
            public void onItemCancelClick(int id) {
                cancel(id);
            }

            @Override
            public void onItemSwipe(int id) {
                delete(id);
            }
        });

    }

    public void initializeDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, medicine_list_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    public void initializeFAM(){
        addMedicineMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                whiteOverlay.getBackground().setAlpha(120);
                whiteOverlay.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        addMedicineMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                whiteOverlay.getBackground().setAlpha(0);
                whiteOverlay.setOnTouchListener(null);
            }
        });
        whiteOverlay.getBackground().setAlpha(0);
        addMedicineMenu.collapse();
    }

    public void updateList(){
        Cursor c = medicineUtil.getAllMedicine();
        medicineAdapter.changeCursor(c);
    }

    public void updateList(Cursor c){
        medicineAdapter.changeCursor(c);
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
        medicineUtil.deleteMedicine(id);
        medicineAdapter.notifyDataSetChanged();
        updateList();
    }

    public void edit(int id){
        if(!medicineAdapter.isExpanded(id)){
            expand(id);
        }
        boolean isEditing = medicineAdapter.isEditing(id);
        Log.wtf("action", "IS EDITING (AT ID" + id + ")? " + isEditing);
        medicineAdapter.setEditingPositionId(isEditing ? -1 : id);
        medicineAdapter.notifyDataSetChanged();
    }

    public void expand(int id){
        if(CREATING_NEW_ITEM == -1){
            boolean isExpanded = medicineAdapter.isExpanded(id);
            medicineAdapter.setExpandedPositionId(isExpanded ? -1 : id);
            TransitionManager.beginDelayedTransition(rvMedicine);
            medicineAdapter.notifyDataSetChanged();
        }else{
            delete((int)CREATING_NEW_ITEM);
            CREATING_NEW_ITEM = -1;
        }
    }

    public void returnToView(int id){
        boolean isEditing = medicineAdapter.isEditing(id);
        medicineAdapter.setEditingPositionId(isEditing ? -1 : id);
        medicineAdapter.notifyDataSetChanged();
    }

    public void save(Medicine medicine){
        Log.wtf("action", "TO UPDATE " + medicine.getBrandName() + " WITH ID " + medicine.getSqlId());
        medicineUtil.updateMedicine(medicine);
        medicineAdapter.notifyDataSetChanged();

        updateList();
        returnToView(medicine.getSqlId());

        if(CREATING_NEW_ITEM != -1)
            CREATING_NEW_ITEM = -1;
    }

    public void search(String query){
        String[] conditions = SearchUtil.searchWith(query);
        if(conditions != null) {
            Cursor medicineList = medicineUtil.search(conditions);
            updateList(medicineList);
        }else{
            updateList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_medicine_list, menu);

        actionSearchMedicineIcon = menu.findItem(R.id.action_search_medicine);
        if(actionSearchMedicineIcon != null){
            actionSearchMedicineMenu = (SearchView) actionSearchMedicineIcon.getActionView();
        }
        if(actionSearchMedicineMenu != null){
            //change stuff here.
            int[] attrs = {android.R.attr.color};
            EditText searchEditText = (EditText) actionSearchMedicineMenu.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            TypedArray typedArray = obtainStyledAttributes(attrs);
            searchEditText.setTextColor(typedArray.getColor(0, Color.WHITE));
            actionSearchMedicineMenu.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    search(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return false;
                }
            });
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(addMedicineMenu.isExpanded()){
            addMedicineMenu.collapse();
        } else if(medicineAdapter.isEditing()){
            cancel(medicineAdapter.getEditingPositionId());
        } else if(medicineAdapter.isExpanded()){
            expand(medicineAdapter.getExpandedPositionId());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.wtf("action", "Clicked something in app bar");
        switch(id){
            case R.id.action_search_medicine:
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

    public void addNewMedicine(String className){
        if(CREATING_NEW_ITEM == -1) {
            actionSearchMedicineIcon.collapseActionView();

            Medicine tempMed = MedicineInstantiatorUtil.createMedicineInstanceFromString(className);

            tempMed.setGenericName("");
            tempMed.setBrandName("");
            tempMed.setMedicineFor("");
            tempMed.setAmount(0.0);
            tempMed.setDosage(0);
            long tempId = medicineUtil.addMedicine(tempMed);

            updateList();

            rvMedicine.smoothScrollToPosition(medicineAdapter.getItemCount() - 1);
            expand((int) medicineAdapter.getItemId(medicineAdapter.getItemCount() - 1));
            edit((int) medicineAdapter.getItemId(medicineAdapter.getItemCount() - 1));
            CREATING_NEW_ITEM = tempId;

            addMedicineMenu.collapse();
        }else{
            delete((int) CREATING_NEW_ITEM);
            CREATING_NEW_ITEM = -1;
            addNewMedicine(className);
        }
    }

    @OnClick(R.id.fab_option_capsule)
    public void addCapsule(){
        addNewMedicine(Capsule.CLASS_NAME);
        Toast.makeText(getBaseContext(), "Something", Toast.LENGTH_SHORT);
    }

    @OnClick(R.id.fab_option_syrup)
    public void addSyrup(){
        addNewMedicine(Syrup.CLASS_NAME);
        Toast.makeText(getBaseContext(), "Something", Toast.LENGTH_SHORT);
    }

    @OnClick(R.id.fab_option_tablet)
    public void addMedicine(){
        addNewMedicine(Tablet.CLASS_NAME);
        Toast.makeText(getBaseContext(), "Something", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(CREATING_NEW_ITEM != -1){
            delete((int) CREATING_NEW_ITEM);
            CREATING_NEW_ITEM = -1;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(CREATING_NEW_ITEM != -1){
            delete((int) CREATING_NEW_ITEM);
            CREATING_NEW_ITEM = -1;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(CREATING_NEW_ITEM != -1){
            delete((int) CREATING_NEW_ITEM);
            CREATING_NEW_ITEM = -1;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(CREATING_NEW_ITEM != -1){
            delete((int) CREATING_NEW_ITEM);
            CREATING_NEW_ITEM = -1;
        }
    }
}
