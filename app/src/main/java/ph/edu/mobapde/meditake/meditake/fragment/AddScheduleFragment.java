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
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * {@link AddScheduleFragment.OnAddScheduleFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddScheduleFragment extends Fragment {

    @BindView(R.id.tv_display_time)
    TextView tvScheduleTime;
    @BindView(R.id.tv_display_time_period)
    TextView tvScheduleTimePeriod;
    @BindView(R.id.lin_display_edit_time)
    LinearLayout linScheduleTime;

    @BindView(R.id.lin_cancel_schedule)
    LinearLayout linCancelSchedule;
    @BindView(R.id.lin_save_schedule)
    LinearLayout linSaveSchedule;

    @BindView(R.id.lin_schedule_ringtone)
    LinearLayout linRingtone;
    @BindView(R.id.tv_schedule_ringtone_name)
    TextView tvRingtone;
    @BindView(R.id.iv_schedule_ringtone_selector)
    ImageView ivScheduleRingtonePicker;

    @BindView(R.id.tv_schedule_label)
    TextView tvScheduleLabel;

    @BindView(R.id.switch_schedule_vibrate)
    Switch switchIsVibrate;

    private Uri ringtoneUriUsed;

    boolean isMilitary;

    private OnAddScheduleFragmentInteractionListener onAddScheduleFragmentInteractionListener;
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

    public AddScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddScheduleFragment newInstance(String param1, String param2) {
        AddScheduleFragment fragment = new AddScheduleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_schedule, container, false);
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

        linScheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAddScheduleFragmentInteractionListener != null){
                    onAddScheduleFragmentInteractionListener.onFragmentTimeClick(tvScheduleTime, tvScheduleTimePeriod);
                }
            }
        });

        linSaveSchedule.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAddScheduleFragmentInteractionListener != null){
                    long nextDrinkingTime = DateUtil.getTime(tvScheduleTime.getText().toString() + " " + tvScheduleTimePeriod.getText().toString());
                    String label = tvScheduleLabel.getText().toString();
                    boolean isVibrate = switchIsVibrate.isChecked();

                    Schedule schedule = new Schedule(nextDrinkingTime, label, ringtoneUriUsed.toString(), 10, isVibrate, true);
                    onAddScheduleFragmentInteractionListener.onFragmentSave(schedule);
                }
            }
        });
        linCancelSchedule.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAddScheduleFragmentInteractionListener != null){
                    onAddScheduleFragmentInteractionListener.onFragmentCancel();
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
        if (context instanceof OnAddScheduleFragmentInteractionListener) {
            onAddScheduleFragmentInteractionListener = (OnAddScheduleFragmentInteractionListener) context;
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

    public interface OnAddScheduleFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentTimeClick(TextView tvTime, TextView tvTimePeriod);
        void onFragmentSave(Schedule schedule);
        void onFragmentCancel();
    }
}
