package ph.edu.mobapde.meditake.meditake.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.RequestCodes;
import ph.edu.mobapde.meditake.meditake.adapter.recyclerview.MedicineAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Capsule;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.Syrup;
import ph.edu.mobapde.meditake.meditake.beans.Tablet;
import ph.edu.mobapde.meditake.meditake.fragment.medicine.view.ViewMedicineDetailsFragment;
import ph.edu.mobapde.meditake.meditake.fragment.medicine.view.ViewMedicineFragment;
import ph.edu.mobapde.meditake.meditake.listener.OnMedicineClickListener;
import ph.edu.mobapde.meditake.meditake.util.DrawerUtil;
import ph.edu.mobapde.meditake.meditake.util.instantiator.MedicineInstantiatorUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.SearchUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class MedicineListActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener,
                    ViewMedicineDetailsFragment.OnViewMedicineDetailsFragmentInteractionListener,
                    ViewMedicineFragment.OnViewMedicineFragmentInteractionListener {

    @BindView(R.id.snackbar_position)
    CoordinatorLayout clSnackbar;

    @BindView(R.id.rv_medicine)
    RecyclerView rvMedicine;

    @BindView(R.id.white_overlay)
    RelativeLayout whiteOverlay;

    @BindView(R.id.toolbar_medicine_list)
    Toolbar medicine_list_toolbar;

    @BindView(R.id.drawer_layout_medicine_list)
    DrawerLayout drawer;

    @BindView(R.id.lin_rv_medicine_empty)
    LinearLayout linRvEmpty;

    @BindView(R.id.black_overlay)
    View blackOverlay;

    MenuItem actionSearchMedicineIcon;
    SearchView actionSearchMedicineMenu;

    MedicineAdapter medicineAdapter;
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback;
    ItemTouchHelper itemTouchHelper;

    LinearLayoutManager mLayoutManager;
    MedicineUtil medicineUtil;

    long CREATING_NEW_ITEM;
    Medicine LAST_DELETED;

    String columnName, order;
    private boolean doubleBackToExitPressedOnce;

    ViewMedicineFragment viewMedicineFragment;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_medicine_list);

        ButterKnife.bind(this);
        setUpActionBar();
        medicineUtil = new MedicineUtil(getBaseContext());
        columnName = "";
        order = "";


        initializeDrawer();
        initializeAdapter();
        initializeFAM();
        initializeContents();
        CREATING_NEW_ITEM = -1;
        LAST_DELETED = null;
        //addHardcodedData()
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestCodes.REQUEST_ADD_MEDICINE){
            if(resultCode == RESULT_OK){
                medicineAdapter.notifyDataSetChanged();
                updateListVisibility();
                updateList();
            }else if(resultCode == RESULT_CANCELED){

            }
        }else if(requestCode == RequestCodes.REQUEST_SETTINGS_UPDATE){
            ThemeUtil.reloadWithTheme(this);
        }
    }

    public void addHardcodedData(){
        Medicine medA = new Capsule("Capsule A", "Generic capsule", "Something bad", 0);
        Medicine medB = new Syrup("Syrup A", "Generic syrup", "Something bad", 0);
        Medicine medC = new Tablet("Tablet A", "Generic tablet", "Something bad", 0);
        medicineUtil.addMedicine(medA);
        medicineUtil.addMedicine(medB);
        medicineUtil.addMedicine(medC);
        updateList();
    }

    public void setUpActionBar(){
        setSupportActionBar(medicine_list_toolbar);
        getSupportActionBar().setTitle("Medicines");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initializeContents(){
        mLayoutManager = new LinearLayoutManager(getBaseContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        rvMedicine.setAdapter(medicineAdapter);
        rvMedicine.setLayoutManager(mLayoutManager);

        itemTouchHelper.attachToRecyclerView(rvMedicine);

        updateListVisibility();
    }

    public void initializeAdapter(){
        medicineAdapter = new MedicineAdapter(getBaseContext(), getMedicineList(columnName, order), Medicine.COLUMN_ID);
        medicineAdapter.setHasStableIds(true);

        medicineAdapter.setOnMedicineClickListener(new OnMedicineClickListener() {
            @Override
            public void onItemClick(int id) {
                view(id);
                //expand(id);
            }

            @Override
            public void onItemDeleteClick(int id) {
                //delete(id);
            }

            @Override
            public void onItemEditClick(int id) {
                //edit(id);
            }

            @Override
            public void onItemSaveClick(Medicine medicine) {
                //save(medicine);
            }

            @Override
            public void onItemCancelClick(int id) {
                //cancel(id);
            }

            @Override
            public void onItemSwipe(int id) {
                //delete(id);
            }
        });

        simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
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

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    }

    public void initializeDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, medicine_list_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    public void initializeFAM(){
        whiteOverlay.getBackground().setAlpha(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_medicine_list, menu);

        actionSearchMedicineIcon = menu.findItem(R.id.action_search_medicine);
        if(actionSearchMedicineIcon != null){
            actionSearchMedicineMenu = (SearchView) actionSearchMedicineIcon.getActionView();
        }
        if(actionSearchMedicineMenu != null){
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
        } else if(medicineAdapter.isEditing()){
            cancel(medicineAdapter.getEditingPositionId());
        } else if(medicineAdapter.isExpanded()){
            expand(medicineAdapter.getExpandedPositionId());
        } else{
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.wtf("action", "Clicked something in app bar");
        switch(id){
            case R.id.action_search_medicine:
                break;
            case R.id.action_sort_medicine:
                break;
            case R.id.action_sort_medicine_brandname_ascending:
                updateList(getMedicineList(Medicine.COLUMN_BRAND_NAME, MedicineUtil.ORDER_ASCENDING));
                break;
            case R.id.action_sort_medicine_brandname_descending:
                updateList(getMedicineList(Medicine.COLUMN_BRAND_NAME, MedicineUtil.ORDER_DESENDING));
                break;
            case R.id.action_sort_medicine_genericname_ascending:
                updateList(getMedicineList(Medicine.COLUMN_GENERIC_NAME, MedicineUtil.ORDER_ASCENDING));
                break;
            case R.id.action_sort_medicine_genericname_descending:
                updateList(getMedicineList(Medicine.COLUMN_GENERIC_NAME, MedicineUtil.ORDER_DESENDING));
                break;
            case R.id.action_sort_medicine_medicinetype_ascending:
                updateList(getMedicineList(Medicine.COLUMN_MEDICINE_TYPE, MedicineUtil.ORDER_ASCENDING));
                break;
            case R.id.action_sort_medicine_medicinetype_descending:
                updateList(getMedicineList(Medicine.COLUMN_MEDICINE_TYPE, MedicineUtil.ORDER_DESENDING));
                break;
            case R.id.action_sort_medicine_id_ascending:
                updateList(getMedicineList(Medicine.COLUMN_ID, MedicineUtil.ORDER_ASCENDING));
                break;
            case R.id.action_sort_medicine_id_descending:
                updateList(getMedicineList(Medicine.COLUMN_ID, MedicineUtil.ORDER_DESENDING));
                break;
            default: showGenericSnackbar("Unexpected error encoutered. Please try again.", Snackbar.LENGTH_SHORT);
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerUtil.execute(this, item);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @OnClick(R.id.fab_option_capsule)
//    public void addCapsule(){
//        addNewMedicine(Capsule.CLASS_NAME);
//    }
//
//    @OnClick(R.id.fab_option_syrup)
//    public void addSyrup(){
//        addNewMedicine(Syrup.CLASS_NAME);
//    }
//
//    @OnClick(R.id.fab_option_tablet)
//    public void addMedicine(){
//        addNewMedicine(Tablet.CLASS_NAME);
//    }
    @OnClick(R.id.fab_add_medicine)
    public void onClickFab(){
        Intent i = new Intent(getBaseContext(), AddMedicineActivity.class);
        startActivityForResult(i, RequestCodes.REQUEST_ADD_MEDICINE);
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
        updateList();
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

    public Cursor getMedicineList(String columnName, String order){
        Cursor cursor = null;
        if((columnName + order).trim().isEmpty()){
            Log.wtf("SORT STATE", "EMPTY --> GENERIC NAME ASC BY DEFAULT");
            columnName = Medicine.COLUMN_GENERIC_NAME;
            order = MedicineUtil.ORDER_ASCENDING;
        }
        return medicineUtil.getAllMedicine(columnName, order);
    }

    public void updateList(){
        medicineAdapter.changeCursor(getMedicineList(columnName, order));
    }

    public void updateListVisibility(){
        int rvVisibility = medicineAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE;
        int linVisibility = medicineAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE;

        rvMedicine.setVisibility(rvVisibility);
        linRvEmpty.setVisibility(linVisibility);
    }

    public void updateList(Cursor c){
        medicineAdapter.changeCursor(c);
    }

    public void addNewMedicine(String className){
        if(CREATING_NEW_ITEM == -1) {
            linRvEmpty.setVisibility(View.GONE);
            rvMedicine.setVisibility(View.VISIBLE);
            actionSearchMedicineIcon.collapseActionView();

            Medicine tempMed = MedicineInstantiatorUtil.createMedicineInstanceFromString(className);

            tempMed.setGenericName("");
            tempMed.setBrandName("");
            tempMed.setMedicineFor("");
            tempMed.setAmount(0);
            tempMed.setDosage(0);
            long tempId = medicineUtil.addMedicine(tempMed);

            updateList();

            rvMedicine.smoothScrollToPosition(medicineAdapter.getItemCount() - 1);
            expand((int) medicineAdapter.getItemId(medicineAdapter.getItemCount() - 1));
            //edit((int) medicineAdapter.getItemId(medicineAdapter.getItemCount() - 1));
            CREATING_NEW_ITEM = tempId;
        }else{
            delete((int) CREATING_NEW_ITEM);
            CREATING_NEW_ITEM = -1;
            addNewMedicine(className);
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

    public void closeViewMedicineFragment(){
        getFragmentManager().popBackStack();
        ft = getSupportFragmentManager().beginTransaction();
//        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        ft.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, android.R.anim.fade_in, android.R.anim.fade_out);
        ft.hide(viewMedicineFragment);
//        ft.remove(viewMedicineFragment);
        ft.commit();
        blackOverlay.animate().alpha(0.0f);
    }

    public void delete(int id){
        LAST_DELETED = medicineUtil.getMedicine(id);
        medicineUtil.deleteMedicine(id);
        medicineAdapter.notifyDataSetChanged();
        updateList();
        if(CREATING_NEW_ITEM == -1)
            showUndoSnackbar();

        int rvVisibility = medicineAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE;
        int linVisibility = medicineAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE;

        rvMedicine.setVisibility(rvVisibility);
        linRvEmpty.setVisibility(linVisibility);
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
        if(!medicineAdapter.isEditing(id)) {
            if (CREATING_NEW_ITEM == -1) {
                boolean isExpanded = medicineAdapter.isExpanded(id);
                medicineAdapter.setExpandedPositionId(isExpanded ? -1 : id);
                TransitionManager.beginDelayedTransition(rvMedicine);
                medicineAdapter.notifyDataSetChanged();
                //rvMedicine.smoothScrollToPosition(medicineAdapter.getExpandedPosition() );
            } else {
                delete((int) CREATING_NEW_ITEM);
                CREATING_NEW_ITEM = -1;
            }
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
        //returnToView(medicine.getSqlId());

        String message = getString(R.string.message_medicine_edit);
        if(CREATING_NEW_ITEM != -1) {
            CREATING_NEW_ITEM = -1;
            message = getString(R.string.message_medicine_add);
        }
        showGenericSnackbar(message, Snackbar.LENGTH_SHORT);
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

    public void view(int id){
        Log.wtf("SCHEDLIST", "SCHEDULE IS " + medicineUtil.getMedicine(id));
        blackOverlay.animate().alpha(0.50f);
        viewMedicineFragment = ViewMedicineFragment.newInstance(id);
        viewMedicineFragment.setOnViewScheduleFragmentInteractionListener(new ViewMedicineFragment.OnViewMedicineFragmentInteractionListener() {
            @Override
            public void onViewMedicineBackgroundClick(Medicine medicine) {
                save(medicine);
                closeViewMedicineFragment();
            }
        });
        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down);
        ft.replace(R.id.fragment_medicine_view_placeholder, viewMedicineFragment);
        ft.commit();

    }

    public void showUndoSnackbar(){
        int[] attrs = {android.R.attr.color};
        final Snackbar snackbarRestore = Snackbar.make(clSnackbar, R.string.message_medicine_restore, Snackbar.LENGTH_SHORT);
        Snackbar snackbarDelete = Snackbar.make(clSnackbar, R.string.message_medicine_delete, Snackbar.LENGTH_LONG);
        snackbarDelete.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoDelete();
                snackbarRestore.show();
            }
        });

        TypedArray typedArray = obtainStyledAttributes(attrs);

        TextView snackBarDeleteTextView = (TextView) snackbarDelete.getView().findViewById(android.support.design.R.id.snackbar_text);
        snackBarDeleteTextView.setTextColor(typedArray.getColor(0, Color.WHITE));

        TextView snackBarTextView = (TextView) snackbarRestore.getView().findViewById(android.support.design.R.id.snackbar_text);
        snackBarTextView.setTextColor(typedArray.getColor(0, Color.WHITE));

        snackbarDelete.show();
    }

    public void showGenericSnackbar(String message, int length){
        int[] attrs = {android.R.attr.color};
        Snackbar snackbarNotify = Snackbar.make(clSnackbar, message, length);

        TypedArray typedArray = obtainStyledAttributes(attrs);

        TextView snackBarNotifyTextView = (TextView) snackbarNotify.getView().findViewById(android.support.design.R.id.snackbar_text);
        snackBarNotifyTextView.setTextColor(typedArray.getColor(0, Color.WHITE));

        snackbarNotify.show();
    }

    public void undoDelete(){

        itemTouchHelper.attachToRecyclerView(null);
        itemTouchHelper.attachToRecyclerView(rvMedicine);

        int prevId = LAST_DELETED.getSqlId();
        int newId = (int) medicineUtil.addMedicine(LAST_DELETED);

        medicineUtil.updateMedicineId(prevId, newId);
        medicineAdapter.notifyDataSetChanged();
        LAST_DELETED = null;

        int rvVisibility = medicineAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE;
        int linVisibility = medicineAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE;

        rvMedicine.setVisibility(rvVisibility);
        linRvEmpty.setVisibility(linVisibility);

        updateList();
    }

    @Override
    public void onViewScheduleDetailsFragmentClose(Medicine medicine) {
        save(medicine);
        updateList();
        closeViewMedicineFragment();
    }

    @Override
    public void onViewScheduleMedicineFragmentDelete(int id) {
        delete(id);
        updateList();
        closeViewMedicineFragment();
    }

    @Override
    public void onViewMedicineBackgroundClick(Medicine medicine) {

    }
}
