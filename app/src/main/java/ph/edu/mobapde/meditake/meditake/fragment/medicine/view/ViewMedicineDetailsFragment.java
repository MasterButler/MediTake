package ph.edu.mobapde.meditake.meditake.fragment.medicine.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.stepstone.stepper.StepperLayout;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.recyclerview.ViewScheduleMedicineAdapter;
import ph.edu.mobapde.meditake.meditake.adapter.recyclerview.ViewScheduleMedicineSelectionAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.MedicinePlan;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.dialog.BasicNumberPickerDialogFragment;
import ph.edu.mobapde.meditake.meditake.dialog.BasicTextInputDialogFragment;
import ph.edu.mobapde.meditake.meditake.util.AlarmUtil;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicinePlanUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.SchedulePlanUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.instantiator.MedicineInstantiatorUtil;
import ph.edu.mobapde.meditake.meditake.util.instantiator.MedicinePlanInstantiatorUtil;

import static android.widget.FrameLayout.*;


public class ViewMedicineDetailsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_MEDICINE = "medicine";

    public static final int DOSAGE_LOWER_LIMIT = 0;
    public static final int DOSAGE_UPPER_LIMIT = 20;

    public static final int AMOUNT_LOWER_LIMIT = 0;
    public static final int AMOUNT_UPPER_LIMIT = 1000;

    @BindView(R.id.rv_medicine_background_color)
    RelativeLayout rvMedicineBgColor;
    @BindView(R.id.iv_medicine_drawable)
    ImageView ivMedicineDrawable;

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

    @BindView(R.id.lin_close_medicine)
    LinearLayout linClose;
    @BindView(R.id.lin_delete_medicine)
    LinearLayout linDelete;

    ArrayList<Integer> medicineDrawable;
    ArrayList<Integer> medicineBackground;

    ViewScheduleMedicineAdapter viewScheduleMedicineAdapter;

    MedicineUtil medicineUtil;

    Medicine medicine;
    int medId;
    int sectionNumber;

    Uri ringtoneUriUsed;

    boolean isMilitary;

    OnViewMedicineDetailsFragmentInteractionListener onViewMedicineDetailsFragmentInteractionListener;
    Context contextHolder;

    public static ViewMedicineDetailsFragment newInstance(int sectionNumber, int medId) {
        ViewMedicineDetailsFragment fragment = new ViewMedicineDetailsFragment();
        Log.wtf("IN CONSTRUCTOR VIEWSCHED", "CREATED THE FRAGMENT");
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt(ARG_MEDICINE, medId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            this.medId = getArguments().getInt(ARG_MEDICINE);
        }
        medicineUtil = new MedicineUtil(contextHolder);
        medicine = medicineUtil.getMedicine(medId);

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

        View v = inflater.inflate(R.layout.fragment_view_medicine_details, container, false);
        ButterKnife.bind(this, v);

        int selection = MedicineInstantiatorUtil.getMedicineInstanceOf(medicine);
        rvMedicineBgColor.setBackgroundColor(medicineBackground.get(selection));
        ivMedicineDrawable.setImageResource(medicineDrawable.get(selection));

        String genericName = medicine.getGenericName();
        String brandName = medicine.getBrandName();
        String medicineFor = medicine.getMedicineFor();
        int dosage = medicine.getDosage();
        long amount = medicine.getAmount();

        tvGenericName.setText(genericName);
        tvBrandName.setText(brandName);
        tvMedicineFor.setText(medicineFor);
        tvDosage.setText(dosage + " " + medicine.getModifier() + " per dosage");
        tvAmount.setText(amount + " " + medicine.getModifier() + " remaining");

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
                showNumberPickerDialog("Dosage", tvDosage, DOSAGE_LOWER_LIMIT, DOSAGE_UPPER_LIMIT);
            }
        });

        tvAmount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPickerDialog("Amount remaining", tvAmount, AMOUNT_LOWER_LIMIT, AMOUNT_UPPER_LIMIT);
            }
        });

        linClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onViewMedicineDetailsFragmentInteractionListener != null){
                    onViewMedicineDetailsFragmentInteractionListener.onViewScheduleDetailsFragmentClose(updateMedicineFromUserInput(medicine));
                }
            }
        });

        linDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onViewMedicineDetailsFragmentInteractionListener != null){
                    onViewMedicineDetailsFragmentInteractionListener.onViewScheduleMedicineFragmentDelete(medicine.getSqlId());
                }
            }
        });

        return v;
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

        textInput.show(getActivity().getFragmentManager(), BasicTextInputDialogFragment.class.toString());
    }

    public void showNumberPickerDialog(String title, final TextView tvEdit, int lowerLimit, int upperLimit){
        BasicNumberPickerDialogFragment numberPicker = new BasicNumberPickerDialogFragment(title, "SET", "CANCEL", tvEdit.getText().toString(), lowerLimit, upperLimit);
        numberPicker.setOnClickListener(new BasicNumberPickerDialogFragment.OnDialogClickListener() {
            @Override
            public void onPositiveSelected(String msg) {
                editTextView(tvEdit, msg);
            }

            @Override
            public void onNegativeSelected() {

            }
        });

        numberPicker.show(getActivity().getFragmentManager(), BasicNumberPickerDialogFragment.class.getSimpleName());
    }

    public void removeErrorWarnings(){
        if(!tvGenericName.getText().toString().isEmpty()) {
            int[] attrs = {android.R.attr.textColor};
            TypedArray typedArray = getActivity().obtainStyledAttributes(attrs);
            tvGenericName.setTextColor(typedArray.getColor(0, Color.BLACK));
            Toast.makeText(contextHolder, "Generic name must not be empty!", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnViewMedicineDetailsFragmentInteractionListener) {
            onViewMedicineDetailsFragmentInteractionListener = (OnViewMedicineDetailsFragmentInteractionListener) context;
            contextHolder = context;
            Log.wtf("CHECKING CONTEXT", "CONTEXT IS " + (contextHolder != null));
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnViewMedicineDetailsFragmentInteractionListener");
        }
    }


    public interface OnViewMedicineDetailsFragmentInteractionListener {
        void onViewScheduleDetailsFragmentClose(Medicine medicine);
        void onViewScheduleMedicineFragmentDelete(int id);
    }

    public Medicine getMedicine() {
        return medicine;
    }

    //TODO lookign for better ways to do this
    public Medicine updateMedicineFromUserInput(Medicine medicine){
        String genericName = tvGenericName.getText().toString();
        String brandName = tvBrandName.getText().toString();
        String medicineFor = tvMedicineFor.getText().toString();
        int dosage = Integer.valueOf(tvDosage.getText().toString().split("\\s+")[0]);
        long amount = Long.valueOf(tvAmount.getText().toString().split("\\s+")[0]);

        medicine.setGenericName(genericName);
        medicine.setBrandName(brandName);
        medicine.setMedicineFor(medicineFor);
        medicine.setDosage(dosage);
        medicine.setAmount(amount);

        return medicine;
    }
}
