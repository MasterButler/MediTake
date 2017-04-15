package ph.edu.mobapde.meditake.meditake.fragment.medicine.add;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.page.AddMedicineStepAdapter;
import ph.edu.mobapde.meditake.meditake.adapter.page.AddScheduleStepAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.dialog.AlertDialogFragment;
import ph.edu.mobapde.meditake.meditake.dialog.BasicTextInputDialogFragment;
import ph.edu.mobapde.meditake.meditake.fragment.schedule.add.AddScheduleDetailsFragment;
import ph.edu.mobapde.meditake.meditake.util.AlarmUtil;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;
import ph.edu.mobapde.meditake.meditake.util.instantiator.MedicineInstantiatorUtil;

import static android.widget.FrameLayout.OnClickListener;

/**
 * Created by Winfred Villaluna on 4/13/2017.
 */
public class AddMedicineDetailsFragment extends Fragment implements BlockingStep {

    public static final String TITLE = "Add Information";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int sectionNumber;

    @BindView(R.id.rv_medicine_background_color)
    RelativeLayout rvMedicineBgColor;
    @BindView(R.id.iv_medicine_drawable)
    ImageView ivMedicineDrawable;

    @BindView(R.id.tv_medicine_generic_name_label)
    TextView tvGenericNameLabel;

    @BindView(R.id.tv_medicine_generic_name)
    TextView tvGenericName;
    @BindView(R.id.tv_medicine_brand_name)
    TextView tvBrandName;
    @BindView(R.id.tv_medicine_for)
    TextView tvMedicineFor;
    @BindView(R.id.tv_medicine_dosage)
    TextView tvDosage;
    @BindView(R.id.tv_medicine_amount)
    TextView tvAmount;

    ArrayList<Integer> medicineDrawable;
    ArrayList<Integer> medicineBackground;

    private Uri ringtoneUriUsed;
    boolean isMilitary;

    private OnAddMedicineDetailsFragmentInteractionListener onAddMedicineDetailsFragmentInteractionListener;
    private Context contextHolder;
    private int selectedValue;

    public AddMedicineDetailsFragment() {
        // Required empty public constructor
    }

    public static AddMedicineDetailsFragment newInstance(int sectionNumber) {
        AddMedicineDetailsFragment fragment = new AddMedicineDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        medicineDrawable = new ArrayList<>();
        medicineDrawable.add(R.drawable.capsule_white_large);
        medicineDrawable.add(R.drawable.syrup_white_large);
        medicineDrawable.add(R.drawable.tablet_white_large);

        medicineBackground = new ArrayList<>();
        medicineBackground.add(getResources().getColor(R.color.medicine_capsule_selection));
        medicineBackground.add(getResources().getColor(R.color.medicine_syrup_selection));
        medicineBackground.add(getResources().getColor(R.color.medicine_tablet_selection));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_medicine_details, container, false);
        ButterKnife.bind(this, v);

        tvGenericName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog("Generic Name", tvGenericName);
            }
        });

        tvBrandName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog("Brand Name",tvBrandName);
            }
        });

        tvMedicineFor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog("Medicine For", tvMedicineFor);
            }
        });

        tvDosage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog("Dosage", tvDosage);
            }
        });

        tvAmount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog("Amount remaining", tvAmount);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddMedicineDetailsFragmentInteractionListener) {
            onAddMedicineDetailsFragmentInteractionListener = (OnAddMedicineDetailsFragmentInteractionListener) context;
            contextHolder = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddMedicineDetailsFragmentInteractionListener");
        }
    }

    public void editTextView(TextView tv, String message){
        if(!message.trim().isEmpty())
            tv.setText(message);
    }

    public void showEditDialog(String title, final TextView tvEdit){
        BasicTextInputDialogFragment textInput = new BasicTextInputDialogFragment(title, "Ok", "Cancel", tvEdit.getText().toString(), EditorInfo.TYPE_CLASS_TEXT);
        textInput.setOnClickListener(new BasicTextInputDialogFragment.OnDialogClickListener() {
            @Override
            public void onPositiveSelected(String msg) {
                editTextView(tvEdit, msg);
                if(tvEdit.equals(tvGenericName)){
                    removeErrorWarnings();
                }
            }

            @Override
            public void onNegativeSelected() {

            }
        });

        textInput.show(getActivity().getFragmentManager(), BasicTextInputDialogFragment.class.getSimpleName());
    }

    public void removeErrorWarnings(){
        if(!tvGenericName.getText().toString().isEmpty()) {
            StepperLayout slAddMedicine = (StepperLayout) getActivity().findViewById(R.id.sl_add_medicine);
            slAddMedicine.updateErrorFlag(false);
            slAddMedicine.updateErrorState(false);

            int[] attrs = {android.R.attr.textColor};
            TypedArray typedArray = getActivity().obtainStyledAttributes(attrs);
            tvGenericNameLabel.setTextColor(typedArray.getColor(0, Color.BLACK));
        }
    }

    public interface OnAddMedicineDetailsFragmentInteractionListener {
        void OnAddMedicineDetailsFragmentSave(Medicine medicine);
    }

    public Medicine constructNewMedicine(){
        Medicine med = MedicineInstantiatorUtil.createMedicineFromSelectedValue(selectedValue);
        med.setGenericName(tvGenericName.getText().toString());
        med.setBrandName(tvBrandName.getText().toString());
        med.setMedicineFor(tvMedicineFor.getText().toString());
        med.setDosage(0);
        med.setAmount(0);

        return med;
    }

    public void setHeader(int selected){
        Log.wtf("CHECK SELECTED", "GOT " + selected);
        this.selectedValue = selected;
        rvMedicineBgColor.setBackgroundColor(medicineBackground.get(selected));
        ivMedicineDrawable.setImageResource(medicineDrawable.get(selected));
    }

    /**************
     * BASIC STEPS
     **************/

    @Override
    public VerificationError verifyStep() {
        return tvGenericName.getText().toString().isEmpty() ? new VerificationError("Generic Name must not be empty") : null;
    }

    @Override
    public void onSelected() {
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        int errorColor = getResources().getColor(R.color.ms_errorColor);
        tvGenericNameLabel.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake_error));
        tvGenericNameLabel.setTextColor(errorColor);

        Toast.makeText(contextHolder, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
        Medicine med = constructNewMedicine();
        onAddMedicineDetailsFragmentInteractionListener.OnAddMedicineDetailsFragmentSave(med);
        callback.complete();
    }

    @Override
    public void onBackClicked(final StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}
