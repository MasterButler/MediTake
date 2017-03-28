package ph.edu.mobapde.meditake.meditake.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.util.MedicineInstantiatorUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class AddMedicineActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_add_medicine)
    Toolbar add_medicine_toolbar;
    @BindView(R.id.selection_capsule)
    ImageView selctedCapsule;
    @BindView(R.id.selection_lozenge)
    ImageView selctedLozenge;
    @BindView(R.id.selection_syrup)
    ImageView selctedSyrup;
    @BindView(R.id.medicine_type)
    ImageView selectedMedicineType;

    @BindView(R.id.et_add_brand_name)
    EditText et_brandName;
    @BindView(R.id.et_add_generic_name)
    EditText et_genericName;
    @BindView (R.id.et_add_medicine_for)
    EditText et_medicineFor;

    @BindView(R.id.add_medicine_information)
    LinearLayout add_medicine_information;
    @BindView(R.id.add_medicine_type)
    LinearLayout add_medicine_type;

    @BindView(R.id.layout_add_brand_name)
    TextInputLayout tilBrandName;

    @BindView(R.id.layout_add_generic_name)
    TextInputLayout tilGenericName;

    @BindView(R.id.layout_add_medicine_for)
    TextInputLayout tilMedicineFor;

    MenuItem actionAddMedicine;
    Medicine newMedicine;

    private void setUpActionBar(){
        setSupportActionBar(add_medicine_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_add_medicine);
        ButterKnife.bind(this);

        setUpActionBar();
        tilGenericName.setError(null);
    }

    public void showAddMedicineInformation(ImageView imageView){
        Animation slideUp = AnimationUtils.makeInAnimation(getBaseContext(), false);
        add_medicine_type.setVisibility(View.GONE);
        add_medicine_information.startAnimation(slideUp);

        newMedicine = MedicineInstantiatorUtil.createMedicineInstanceFromImageView(imageView);
        selectedMedicineType.setImageResource(newMedicine.getIcon());

        actionAddMedicine.setVisible(true);
    }

    public boolean addMedicine(){
        String brandName = et_brandName.getText().toString();
        String genericName = et_genericName.getText().toString();
        String medicineFor = et_medicineFor.getText().toString();

        MedicineUtil medicineUtil = new MedicineUtil(getBaseContext());
        medicineUtil.setMedicineInfo(newMedicine, brandName, genericName, medicineFor, 0.0, 0);
        long result = medicineUtil.addMedicine(newMedicine);
        return result == -1 ? false : true;
    }


    public boolean isInputValid(){
        if(et_genericName.getText().toString().isEmpty()){
            tilGenericName.setError("Generic name must not be empty.");
            return false;
        }
        return true;
    }

    public boolean confirmUnsavedAction(){
        //TODO create a dialogFragment here
        return true;
    }

    @OnClick({R.id.selection_lozenge, R.id.selection_capsule, R.id.selection_syrup})
    public void onMedicineSelectionClick(ImageView imageView){

        Log.wtf("action", "Clicked a medicine type");
        showAddMedicineInformation(imageView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add_medicine, menu);

        actionAddMedicine = add_medicine_toolbar.getMenu().findItem(R.id.action_add_medicine);
        actionAddMedicine.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_add_medicine:
                if(isInputValid()){
                    if(addMedicine()){
                        Intent i = new Intent(getBaseContext(), MedicineListActivity.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(getBaseContext(), "ERROR DURING MEDICINE CREATION", Toast.LENGTH_SHORT);
                    };
                }else{
                    Toast.makeText(getBaseContext(), "????", Toast.LENGTH_SHORT);
                }
                break;
            case android.R.id.home:
                confirmUnsavedAction();
                finish();
                break;
        }

        return false;
    }
}
