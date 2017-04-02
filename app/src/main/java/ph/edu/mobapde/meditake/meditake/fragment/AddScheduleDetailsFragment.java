package ph.edu.mobapde.meditake.meditake.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.util.AlarmUtil;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;

import static android.widget.FrameLayout.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAddScheduleDetailsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddScheduleDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddScheduleDetailsFragment extends Fragment {

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

    @BindView(R.id.lin_discard_schedule)
    LinearLayout linDiscardSchedule;
    @BindView(R.id.lin_continue_schedule)
    LinearLayout linContinueSchedule;

    @BindView(R.id.lin_schedule_ringtone)
    LinearLayout linRingtone;
    @BindView(R.id.tv_schedule_ringtone_name)
    TextView tvRingtone;
    @BindView(R.id.iv_schedule_ringtone_selector)
    ImageView ivScheduleRingtonePicker;
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

        linContinueSchedule.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAddScheduleFragmentInteractionListener != null){
                    ViewPager viewPager = (ViewPager)getActivity().findViewById(R.id.container);
                    viewPager.setCurrentItem(1);

                }
            }
        });
        linDiscardSchedule.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAddScheduleFragmentInteractionListener != null){
                    onAddScheduleFragmentInteractionListener.onAddScheduleDetailsFragmentCancel();
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
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onAddScheduleFragmentInteractionListener = null;
    }

    public void chooseRingtone(){
        AlarmUtil.chooseRingtone(this);
    }

    public void showEditLabel(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(contextHolder);
        final EditText input = new EditText(contextHolder);

        FrameLayout container = new FrameLayout(contextHolder);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_left_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_right_margin);

        input.setText(tvScheduleLabel.getText().toString());
        input.setLayoutParams(params);
        input.setSingleLine();

        container.addView(input);

        alert.setTitle("Label");
        alert.setView(container);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString().trim();
                editLabel(value);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = alert.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                int[] attrs = {android.R.attr.colorAccent};
                TypedArray typedArray = contextHolder.obtainStyledAttributes(attrs);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(typedArray.getColor(0, Color.BLACK));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(typedArray.getColor(0, Color.BLACK));
            }
        });


        dialog.show();
    }

    private void editLabel(String newValue){
        tvScheduleLabel.setText(newValue);
    }

    public interface OnAddScheduleDetailsFragmentInteractionListener {
        void onAddScheduleDetailsFragmentRepeatClick(TextView tvRepeat);
        void onAddScheduleDetailsFragmentTimeClick(TextView tvTime, TextView tvTimePeriod);
        void onAddScheduleDetailsFragmentCancel();
    }


    //TODO lookign for better ways to do this
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
        //onAddScheduleFragmentInteractionListener.onAddScheduleFragmentSave(schedule);
        return schedule;
    }
}
