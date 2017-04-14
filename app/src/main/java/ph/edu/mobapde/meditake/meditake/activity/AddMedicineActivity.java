package ph.edu.mobapde.meditake.meditake.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
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

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.page.AddMedicineStepAdapter;
import ph.edu.mobapde.meditake.meditake.adapter.page.AddSchedulePagerAdapter;
import ph.edu.mobapde.meditake.meditake.adapter.page.AddScheduleStepAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.fragment.medicine.add.AddMedicineDetailsFragment;
import ph.edu.mobapde.meditake.meditake.fragment.medicine.add.AddMedicineTypeFragment;
import ph.edu.mobapde.meditake.meditake.fragment.schedule.add.AddScheduleDetailsFragment;
import ph.edu.mobapde.meditake.meditake.fragment.schedule.add.AddScheduleMedicineFragment;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.instantiator.MedicineInstantiatorUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class AddMedicineActivity extends AppCompatActivity
            implements  AddMedicineDetailsFragment.OnAddMedicineDetailsFragmentInteractionListener,
                        AddMedicineTypeFragment.OnAddMedicineTypeFragmentInteractionListener,
                        StepperLayout.StepperListener{

    AddMedicineStepAdapter mAddMedicineStepAdapter;
    MedicineUtil medicineUtil;

    @BindView(R.id.sl_add_medicine)
    StepperLayout slAddMedicine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_add_medicine);

        ButterKnife.bind(this);
//        setUpActionBar();

        medicineUtil = new MedicineUtil(getBaseContext());

        initializePager();
        initializeContents();

    }

    public void initializePager(){
//        mAddSchedulePagerAdapter = new AddSchedulePagerAdapter(getSupportFragmentManager());
//        mAddSchedulePagerAdapter.add(AddScheduleDetailsFragment.newInstance(1));
//        mAddSchedulePagerAdapter.add(AddScheduleMedicineFragment.newInstance(2));

//        mViewPager.setAdapter(mAddSchedulePagerAdapter);
        int[] attrs = {android.R.attr.colorBackground};
        TypedArray typedArray = obtainStyledAttributes(attrs);

        //mViewPager.setBackgroundColor(typedArray.getColor(0, Color.WHITE));
        //slAddSchedule.setAdapter();
        //ciSteps.setViewPager(mViewPager);
        //tabLayout.setupWithViewPager(mViewPager);
        mAddMedicineStepAdapter = new AddMedicineStepAdapter(getSupportFragmentManager(), getBaseContext());
        mAddMedicineStepAdapter.add(AddMedicineTypeFragment.newInstance(1));
        mAddMedicineStepAdapter.add(AddMedicineDetailsFragment.newInstance(2));
        slAddMedicine.setAdapter(mAddMedicineStepAdapter, 0);
        slAddMedicine.setBackgroundColor(typedArray.getColor(0, Color.WHITE));
        slAddMedicine.setListener(this);
    }

    public void initializeContents(){
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.toolbar_add_medicine, menu);
        //......
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                finish();
                break;
        }

        return false;
    }

    @Override
    public void onCompleted(View completeButton) {

    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }

    @Override
    public void onAddMedicineTypeFragmentCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void OnAddMedicineDetailsFragmentSave(Medicine medicine) {
        int id = (int) medicineUtil.addMedicine(medicine);
        medicine.setSqlId(id);

        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
    }
}
