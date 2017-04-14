package ph.edu.mobapde.meditake.meditake.fragment.schedule.add;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.dialog.AlertDialogFragment;
import ph.edu.mobapde.meditake.meditake.dialog.BasicTextInputDialogFragment;
import ph.edu.mobapde.meditake.meditake.util.AlarmUtil;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;

import static android.widget.FrameLayout.*;

/**
 * Created by Winfred Villaluna on 4/13/2017.
 */
public class AddScheduleDetailsFragment extends Fragment implements BlockingStep {

    public static final String TITLE = "Set Alarm";
    private static final String ARG_SECTION_NUMBER = "section_number";

    @BindView(R.id.checkbox_repeat)
    CheckBox cbRepeat;

    @BindView(R.id.tv_display_time)
    TextView tvScheduleTime;
    @BindView(R.id.tv_display_time_period)
    TextView tvScheduleTimePeriod;
    @BindView(R.id.lin_display_edit_time)
    LinearLayout linScheduleTime;
    @BindView(R.id.lin_schedule_repeat)
    LinearLayout linRepeat;
    @BindView(R.id.lin_schedule_repeat_selection)
    LinearLayout linRepeatSelection;

    @BindView(R.id.lin_schedule_ringtone)
    LinearLayout linRingtone;
    @BindView(R.id.tv_schedule_ringtone_name)
    TextView tvRingtone;
    @BindView(R.id.tv_schedule_repeat_time)
    TextView tvRepeat;
    @BindView(R.id.tv_schedule_label)
    TextView tvScheduleLabel;

    @BindView(R.id.switch_schedule_vibrate)
    Switch switchIsVibrate;

    private int sectionNumber;
    private Uri ringtoneUriUsed;

    boolean isMilitary;

    private OnAddScheduleDetailsFragmentInteractionListener onAddScheduleFragmentInteractionListener;
    private Context contextHolder;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AlarmUtil.REQUEST_RINGTONE && resultCode == Activity.RESULT_OK){
            ringtoneUriUsed = AlarmUtil.getRingtoneUri(contextHolder, data);
            Ringtone ringtone = RingtoneManager.getRingtone(contextHolder, ringtoneUriUsed);
            String title = ringtone.getTitle(contextHolder);
            tvRingtone.setText(title);
        }
    }

    public AddScheduleDetailsFragment() {
        // Required empty public constructor
    }

    public static AddScheduleDetailsFragment newInstance(int sectionNumber) {
        AddScheduleDetailsFragment fragment = new AddScheduleDetailsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_schedule_details, container, false);
        ButterKnife.bind(this, v);

        ringtoneUriUsed = RingtoneManager.getActualDefaultRingtoneUri(getActivity().getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
        Ringtone ringtone = RingtoneManager.getRingtone(contextHolder, ringtoneUriUsed);
        String title = ringtone.getTitle(contextHolder);
        tvRingtone.setText(title);

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        String displayTime = DateUtil.format(hour, minute, isMilitary);
        this.tvScheduleTime.setText(displayTime.split("\\s+")[0]);
        this.tvScheduleTimePeriod.setText(displayTime.split("\\s+")[1]);

        linRepeat.setVisibility(cbRepeat.isChecked() ? View.VISIBLE : View.GONE);
        cbRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int view_mode = isChecked ? View.VISIBLE : View.GONE;
                linRepeat.setVisibility(view_mode);
            }
        });

        linRepeatSelection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAddScheduleFragmentInteractionListener != null){
                    onAddScheduleFragmentInteractionListener.onAddScheduleDetailsFragmentRepeatClick(tvRepeat);
                }
            }
        });

        linScheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAddScheduleFragmentInteractionListener != null){
                    onAddScheduleFragmentInteractionListener.onAddScheduleDetailsFragmentTimeClick(tvScheduleTime, tvScheduleTimePeriod);
                }
            }
        });

        linRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseRingtone();
            }
        });

        tvScheduleLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditLabel();
            }
        });


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddScheduleDetailsFragmentInteractionListener) {
            onAddScheduleFragmentInteractionListener = (OnAddScheduleDetailsFragmentInteractionListener) context;
            contextHolder = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnViewScheduleDetailsFragmentInteractionListener");
        }
    }

    public void chooseRingtone(){
        AlarmUtil.chooseRingtone(this);
    }

    public void showEditLabel(){
        BasicTextInputDialogFragment textInput = new BasicTextInputDialogFragment("Label", "Ok", "Cancel", tvScheduleLabel.getText().toString(), EditorInfo.TYPE_CLASS_TEXT);
        textInput.setOnClickListener(new BasicTextInputDialogFragment.OnDialogClickListener() {
            @Override
            public void onPositiveSelected(String msg) {
                editLabel(msg);
            }

            @Override
            public void onNegativeSelected() {

            }
        });

        textInput.show(getActivity().getFragmentManager(), BasicTextInputDialogFragment.class.toString());
    }

    private void editLabel(String newValue){
        tvScheduleLabel.setText(newValue);
    }

    public  Schedule constructScheduleFromUserInput(){
        long nextDrinkingTime = DateUtil.getTime(tvScheduleTime.getText().toString() + " " + tvScheduleTimePeriod.getText().toString());
        String label = tvScheduleLabel.getText().toString();
        boolean isVibrate = switchIsVibrate.isChecked();
        int[] timeValues = DateUtil.parseFromTimePicker(tvRepeat.getText().toString());
        long drinkingInterval = cbRepeat.isChecked() ? (timeValues[0] * 60 + timeValues[1]) : 0;

        Log.wtf("NEW MEDICINES", "NEXT DRINKING TIME: " + nextDrinkingTime);
        Log.wtf("NEW MEDICINES", "LABEL: " + label);
        Log.wtf("NEW MEDICINES", "ISVIBRATE IS " + isVibrate);
        Log.wtf("NEW MEDICINES", "DRINKING INTERVAL IS " + drinkingInterval);

        Schedule schedule = new Schedule(nextDrinkingTime, label, ringtoneUriUsed.toString(), drinkingInterval, isVibrate, true);
        return schedule;
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

    }

    @Override
    public void onBackClicked(final StepperLayout.OnBackClickedCallback callback) {

        AlertDialogFragment adf
                = new AlertDialogFragment(  "Discard Schedule",
                                            "Are you sure you want to cancel schedule creation?",
                                            "DISCARD",
                                            "CANCEL");
        adf.setOnDialogClickListener(new AlertDialogFragment.OnDialogClickListener() {
            @Override
            public void onPositiveSelected() {
                if(onAddScheduleFragmentInteractionListener != null) {
                    callback.goToPrevStep();
                    onAddScheduleFragmentInteractionListener.onAddScheduleDetailsFragmentCancel();
                }
            }

            @Override
            public void onNegativeSelected() {
            }
        });
        adf.show(getFragmentManager(), AlertDialogFragment.class.getSimpleName());
    }

    public interface OnAddScheduleDetailsFragmentInteractionListener {
        void onAddScheduleDetailsFragmentRepeatClick(TextView tvRepeat);
        void onAddScheduleDetailsFragmentTimeClick(TextView tvTime, TextView tvTimePeriod);
        void onAddScheduleDetailsFragmentCancel();
    }
}
