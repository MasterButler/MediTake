package ph.edu.mobapde.meditake.meditake.fragment.medicine.add;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.stepstone.stepper.internal.widget.TabsContainer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.ReverseInterpolator;
import ph.edu.mobapde.meditake.meditake.adapter.page.AddMedicineStepAdapter;
import ph.edu.mobapde.meditake.meditake.dialog.AlertDialogFragment;

/**
 * Created by Winfred Villaluna on 4/13/2017.
 */

public class AddMedicineTypeFragment extends Fragment implements BlockingStep {

    public static final String TITLE = "Set Medicine Type";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int sectionNumber;

    TabsContainer tabsContainer;

    @BindView(R.id.lin_capsule_selection)
    LinearLayout linCapsule;
    @BindView(R.id.lin_syrup_selection)
    LinearLayout linSyrup;
    @BindView(R.id.lin_tablet_selection)
    LinearLayout linTablet;

    @BindView(R.id.capsule_bg)
    ImageView ivCapsuleBackground;
    @BindView(R.id.syrup_bg)
    ImageView ivSyrupBackground;
    @BindView(R.id.tablet_bg)
    ImageView ivTabletBackground;

    ArrayList<LinearLayout> medicineTypes;
    ArrayList<ImageView> medicineBackground;
    ArrayList<Integer> bgNormal;
    ArrayList<Integer> bgPressed;
    int selected;
    int prev;

    private OnAddMedicineTypeFragmentInteractionListener onAddMedicineTypeFragmentInteractionListener;
    private Context contextHolder;

    public AddMedicineTypeFragment() {
        // Required empty public constructor
    }

    public static AddMedicineTypeFragment newInstance(int sectionNumber) {
        AddMedicineTypeFragment fragment = new AddMedicineTypeFragment();
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
        selected = -1;
        prev = -1;

        bgNormal = new ArrayList<>();
        bgNormal.add(R.drawable.medicine_capsule_background);
        bgNormal.add(R.drawable.medicine_syrup_background);
        bgNormal.add(R.drawable.medicine_tablet_background);

        bgPressed= new ArrayList<>();
        bgPressed.add(R.drawable.medicine_capsule_background_pressed);
        bgPressed.add(R.drawable.medicine_syrup_background_pressed);
        bgPressed.add(R.drawable.medicine_tablet_background_pressed);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_medicine_type, container, false);
        ButterKnife.bind(this, v);

        medicineTypes = new ArrayList<>();
        medicineTypes.add(linCapsule);
        medicineTypes.add(linSyrup);
        medicineTypes.add(linTablet);

        medicineBackground = new ArrayList<>();
        medicineBackground.add(ivCapsuleBackground);
        medicineBackground.add(ivSyrupBackground);
        medicineBackground.add(ivTabletBackground);

        for(int i = 0; i < medicineTypes.size(); i++){
            medicineTypes.get(i).setTag(i);
            medicineTypes.get(i).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final int value = (int) v.getTag();
                    final int defaultBottom = 0;
                    final int paddingBottom = 40;

                    setSelected(value);
                    if(getSelected() != getPrev()) {
                        Log.wtf("CLICK", "SELECTED IS " + value);

                        Animation animationClick = null, animationRevert = null;

                        if (getPrev() != -1) {
                            animationRevert = new Animation() {
                                @Override
                                protected void applyTransformation(float interpolatedTime, Transformation t) {
                                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) medicineTypes.get(getPrev()).getLayoutParams();
                                    params.bottomMargin = (int) (paddingBottom * interpolatedTime);
                                    medicineTypes.get(getPrev()).setLayoutParams(params);
                                    medicineBackground.get(getPrev()).setImageResource(bgNormal.get(getPrev()));
                                }
                            };
                            animationRevert.setInterpolator(new ReverseInterpolator());
                            animationRevert.setDuration(100);
                        }


                            animationClick = new Animation() {
                                @Override
                                protected void applyTransformation(float interpolatedTime, Transformation t) {
                                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) medicineTypes.get(getSelected()).getLayoutParams();
                                    params.bottomMargin = (int) (paddingBottom * interpolatedTime);
                                    medicineTypes.get(getSelected()).setLayoutParams(params);
                                    medicineBackground.get(getSelected()).setImageResource(bgPressed.get(getSelected()));
                                    removeErrorWarning();
                                }
                            };
                            animationClick.setDuration(100);

                        if (animationRevert != null) {
                            medicineTypes.get(getPrev()).startAnimation(animationRevert);
                        }
                        if (animationClick != null) {
                            medicineTypes.get(getSelected()).startAnimation(animationClick);
                        }
                    }

                }
            });
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddMedicineDetailsFragment.OnAddMedicineDetailsFragmentInteractionListener) {
            onAddMedicineTypeFragmentInteractionListener = (OnAddMedicineTypeFragmentInteractionListener) context;
            contextHolder = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddMedicineTypeFragmentInteractionListener");
        }
    }

    public int getPrev(){
        return prev;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.prev = this.selected;
        this.selected = selected;
    }

    public void resetSelected(int selected){
        this.prev = -1;
        this.selected = selected;
    }

    public interface OnAddMedicineTypeFragmentInteractionListener {
        public void onAddMedicineTypeFragmentCancel();
    }

    public void removeErrorWarning(){
        StepperLayout slAddMedicine = (StepperLayout) getActivity().findViewById(R.id.sl_add_medicine);
        slAddMedicine.updateErrorFlag(false);
        slAddMedicine.updateErrorState(false);
    }

    /**************
     * BASIC STEPS
     **************/

    @Override
    public VerificationError verifyStep() {
        return getSelected() == -1 ? new VerificationError("Choose one medicine type"): null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        linCapsule.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake_error));
        linSyrup.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake_error));
        linTablet.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake_error));

        Toast.makeText(contextHolder, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    /*****************
     * BLOCKING STEPS
     *****************/

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        StepperLayout slAddMedicine = (StepperLayout) getActivity().findViewById(R.id.sl_add_medicine);
        AddMedicineDetailsFragment frag = (AddMedicineDetailsFragment) ((AddMedicineStepAdapter) slAddMedicine.getAdapter()).getFragment(1);
        Log.wtf("CHECK", "GOT FRAG " + frag);
        frag.setHeader(getSelected());

        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(final StepperLayout.OnBackClickedCallback callback) {
        AlertDialogFragment adf
                = new AlertDialogFragment(  "Discard Medicine",
                "Are you sure you want to cancel schedule creation?",
                "DISCARD",
                "CANCEL");
        adf.setOnDialogClickListener(new AlertDialogFragment.OnDialogClickListener() {
            @Override
            public void onPositiveSelected() {
                callback.goToPrevStep();
                onAddMedicineTypeFragmentInteractionListener.onAddMedicineTypeFragmentCancel();
            }

            @Override
            public void onNegativeSelected() {
            }
        });
        adf.show(getFragmentManager(), AlertDialogFragment.class.getSimpleName());
    }
}

