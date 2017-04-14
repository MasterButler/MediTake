package ph.edu.mobapde.meditake.meditake.fragment.schedule.add;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.RequestCodes;
import ph.edu.mobapde.meditake.meditake.activity.AddMedicineActivity;
import ph.edu.mobapde.meditake.meditake.adapter.page.AddScheduleStepAdapter;
import ph.edu.mobapde.meditake.meditake.adapter.recyclerview.AddScheduleMedicineAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.MedicineList;
import ph.edu.mobapde.meditake.meditake.beans.MedicinePlan;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.util.instantiator.MedicinePlanInstantiatorUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;

/**
 * Created by Winfred Villaluna on 4/13/2017.
 */
public class AddScheduleMedicineFragment extends Fragment implements BlockingStep {
    public static final String TITLE = "Add Medicines";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int sectionNumber;

    @BindView(R.id.rv_schedule_medicine_selection)
    RecyclerView rvMedicineSelection;

    @BindView(R.id.lin_rv_schedule_medicine_empty)
    LinearLayout linRvEmpty;

    Context contextHolder;

    MedicineList medicineList;
    AddScheduleMedicineAdapter addScheduleMedicineAdapter;
    MedicineUtil medicineUtil;

    private OnAddScheduleMedicineFragmentInteractionListener onAddScheduleMedicineFragmentInteractionListener;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestCodes.REQUEST_ADD_MEDICINE && resultCode == Activity.RESULT_OK){
            addScheduleMedicineAdapter.notifyDataSetChanged();
            updateList();
        }
    }

    public AddScheduleMedicineFragment() {
        super();
    }

    public static AddScheduleMedicineFragment newInstance(int sectionNumber) {
        AddScheduleMedicineFragment fragment = new AddScheduleMedicineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextHolder = this.getActivity();

        medicineUtil = new MedicineUtil(contextHolder);

        if (getArguments() != null) {
            sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_schedule_medicine, container, false);
        ButterKnife.bind(this, v);

        medicineList = new MedicineList();
        initializeAdapter();
        initializeContents();

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddScheduleMedicineFragmentInteractionListener) {
            onAddScheduleMedicineFragmentInteractionListener = (OnAddScheduleMedicineFragmentInteractionListener) context;
            contextHolder = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void initializeAdapter(){
        addScheduleMedicineAdapter = new AddScheduleMedicineAdapter(contextHolder, medicineUtil.getAllMedicine(), Medicine.COLUMN_ID);
        addScheduleMedicineAdapter.setOnAddScheduleMedicineClickListener(new AddScheduleMedicineAdapter.OnAddScheduleMedicineClickListener() {
            @Override
            public void onItemCheck(Medicine medicine) {
                medicineList.add(medicine);
            }

            @Override
            public void onItemUncheck(Medicine medicine) {
                medicineList.remove(medicine);
            }
        });
        Log.wtf("ADDSCHEDULEMEDICINEFRAG", "ADAPTER NULL: " + (addScheduleMedicineAdapter == null));
        if(addScheduleMedicineAdapter != null) {
            Log.wtf("ADDSCHEDULEMEDICINEFRAG", "ADAPTER SIZE: " + (addScheduleMedicineAdapter.getItemCount()));
        }

    }

    public void initializeContents(){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(contextHolder);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        rvMedicineSelection.setAdapter(addScheduleMedicineAdapter);
        rvMedicineSelection.setLayoutManager(mLayoutManager);

        int rvVisibility = addScheduleMedicineAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE;
        int linVisibility = addScheduleMedicineAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE;

        rvMedicineSelection.setVisibility(rvVisibility);
        linRvEmpty.setVisibility(linVisibility);
    }


    @OnClick(R.id.lin_schedule_add_new_medicine)
    public void addNewMedicine(){
        Intent i = new Intent(contextHolder, AddMedicineActivity.class);
        startActivityForResult(i, RequestCodes.REQUEST_ADD_MEDICINE);
    }

    public interface OnAddScheduleMedicineFragmentInteractionListener {
        void onAddScheduleMedicineFragmentSave(Schedule schedule);
    }

    public void updateList(){
        Cursor c = medicineUtil.getAllMedicine();
        addScheduleMedicineAdapter.changeCursor(c);

        int rvVisibility = addScheduleMedicineAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE;
        int linVisibility = addScheduleMedicineAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE;

        rvMedicineSelection.setVisibility(rvVisibility);
        linRvEmpty.setVisibility(linVisibility);
    }

    /**************
     * BASIC STEPS
     **************/

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    /*****************
     * BLOCKING STEPS
     *****************/

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        if(onAddScheduleMedicineFragmentInteractionListener != null) {
            StepperLayout slAddSchedule = (StepperLayout) getActivity().findViewById(R.id.sl_add_schedule);
            AddScheduleDetailsFragment frag = (AddScheduleDetailsFragment) ((AddScheduleStepAdapter) slAddSchedule.getAdapter()).getFragment(0);
            Log.wtf("CHECK", "GOT FRAG " + frag);
            ArrayList<MedicinePlan> medicinePlanList = MedicinePlanInstantiatorUtil.convertMedicineToMedicinePlan(medicineList);
            Schedule sched = frag.constructScheduleFromUserInput();
            sched.setMedicinePlanList(medicinePlanList);

            onAddScheduleMedicineFragmentInteractionListener.onAddScheduleMedicineFragmentSave(sched);
            callback.complete();
        }
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        StepperLayout slAddSchedule = (StepperLayout) getActivity().findViewById(R.id.sl_add_schedule);
        AddScheduleDetailsFragment frag = (AddScheduleDetailsFragment) ((AddScheduleStepAdapter) slAddSchedule.getAdapter()).getFragment(0);
        Log.wtf("CHECK", "GOING BACK TO " + frag);

        callback.goToPrevStep();
    }

}
