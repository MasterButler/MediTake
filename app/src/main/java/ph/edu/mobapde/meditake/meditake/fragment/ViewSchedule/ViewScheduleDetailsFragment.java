package ph.edu.mobapde.meditake.meditake.fragment.ViewSchedule;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.RecylerView.ViewScheduleMedicineAdapter;
import ph.edu.mobapde.meditake.meditake.adapter.RecylerView.ViewScheduleMedicineSelectionAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.MedicineList;
import ph.edu.mobapde.meditake.meditake.beans.MedicinePlan;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.util.AlarmUtil;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicinePlanUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.SchedulePlanUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.instantiator.MedicinePlanInstantiatorUtil;

import static android.widget.FrameLayout.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnViewScheduleDetailsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewScheduleDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewScheduleDetailsFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SCHEDULE = "schedule";

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

    @BindView(R.id.lin_delete_schedule)
    LinearLayout linDeleteSchedule;
    @BindView(R.id.lin_close_schedule)
    LinearLayout linCloseSchedule;

    @BindView(R.id.lin_schedule_ringtone)
    LinearLayout linRingtone;
    @BindView(R.id.tv_schedule_ringtone_name)
    TextView tvRingtone;
    @BindView(R.id.tv_schedule_repeat_time)
    TextView tvRepeat;
    @BindView(R.id.tv_schedule_label)
    TextView tvLabel;
    @BindView(R.id.schedule_medicine_view_empty)
    TextView tvMedicineEmpty;

    @BindView(R.id.rv_schedule_medicine_view)
    RecyclerView rvMedicineView;

    @BindView(R.id.switch_schedule_vibrate)
    Switch switchIsVibrate;

    @BindView(R.id.siv_schedule_medicine_list)
    ScrollView sivMedicineList;

    ViewScheduleMedicineAdapter viewScheduleMedicineAdapter;

    ScheduleUtil scheduleUtil;
    MedicineUtil medicineUtil;
    MedicinePlanUtil medicinePlanUtil;
    SchedulePlanUtil schedulePlanUtil;

    Schedule schedule;
    int sectionNumber;

    Uri ringtoneUriUsed;

    boolean isMilitary;

    OnViewScheduleDetailsFragmentInteractionListener OnViewScheduleDetailsFragmentInteractionListener;
    Context contextHolder;

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

    public static ViewScheduleDetailsFragment newInstance(int sectionNumber, Schedule schedule) {
        ViewScheduleDetailsFragment fragment = new ViewScheduleDetailsFragment();
        Log.wtf("IN CONSTRUCTOR VIEWSCHED", "CREATED THE FRAGMENT");
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putParcelable(ARG_SCHEDULE, schedule);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            this.schedule = getArguments().getParcelable(ARG_SCHEDULE);
        }
        scheduleUtil = new ScheduleUtil(contextHolder);
        medicineUtil = new MedicineUtil(contextHolder);
        medicinePlanUtil = new MedicinePlanUtil(contextHolder);
        schedulePlanUtil = new SchedulePlanUtil(contextHolder);
        Log.wtf("UTIL", "INTIALIZED ALL UTIL: " + (scheduleUtil != null) + ", " + (medicineUtil != null) + ", " + (medicinePlanUtil != null));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_view_schedule_details, container, false);
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
                if(OnViewScheduleDetailsFragmentInteractionListener != null){
                    OnViewScheduleDetailsFragmentInteractionListener.onViewScheduleDetailsFragmentRepeatClick(tvRepeat);
                }
            }
        });

        linScheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OnViewScheduleDetailsFragmentInteractionListener != null){
                    OnViewScheduleDetailsFragmentInteractionListener.onViewScheduleDetailsFragmentTimeClick(tvScheduleTime, tvScheduleTimePeriod);
                }
            }
        });

        linDeleteSchedule.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OnViewScheduleDetailsFragmentInteractionListener != null){
                    OnViewScheduleDetailsFragmentInteractionListener.onViewScheduleMedicineFragmentDelete(schedule.getSqlId());
                }
            }
        });
        linCloseSchedule.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OnViewScheduleDetailsFragmentInteractionListener != null){
                    OnViewScheduleDetailsFragmentInteractionListener.onViewScheduleDetailsFragmentClose(updateScheduleFromUserInput(schedule));
                }
            }
        });


        linRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseRingtone();
            }
        });

        tvLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditLabel();
            }
        });

        initializeAdapter();

        Log.wtf("ADAPTER", "INITIALIZED VIEWSCHEDMED ADAPTER WITH SIZE OF " + viewScheduleMedicineAdapter.getItemCount());


        tvMedicineEmpty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("CLICK", "CHECKED MEDICINE LIST WITH SIZE " + viewScheduleMedicineAdapter.getItemCount());
                if(viewScheduleMedicineAdapter.getItemCount() == 0){
                    Log.wtf("IN", "SHOW TOAST");
                    Toast.makeText(contextHolder, "There are currently no medicine to show.", Toast.LENGTH_SHORT).show();
                }else{
                    Log.wtf("OUT", "SHOW MED");
                    showMedicineList();
                }
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(contextHolder);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        rvMedicineView.setAdapter(viewScheduleMedicineAdapter);
        rvMedicineView.setLayoutManager(mLayoutManager);
        updateRecyclerViewVisibility();

        cbRepeat.setChecked(schedule.getDrinkingInterval() != 0);
        tvScheduleTime.setText(DateUtil.convertToReadableFormat(schedule.getNextDrinkingTime(), isMilitary).split(" ")[0]);
        tvScheduleTimePeriod.setText(DateUtil.convertToReadableFormat(schedule.getNextDrinkingTime(), isMilitary).split(" ")[1]);
        tvRepeat.setText(DateUtil.parseToTimePickerDisplay(schedule.getDrinkingInterval()));
        tvRingtone.setText(AlarmUtil.convertStringToRingtone(contextHolder, schedule.getRingtone()).getTitle(contextHolder));
        switchIsVibrate.setChecked(schedule.isVibrate());
        tvLabel.setText(schedule.getLabel());

        return v;
    }

    public void updateMedicineList(){
        Cursor c = getMedicine(schedule);
        viewScheduleMedicineAdapter.changeCursor(c);
        viewScheduleMedicineAdapter.notifyDataSetChanged();
        updateRecyclerViewVisibility();
    }

    public Cursor getMedicine(Schedule schedule){
        ArrayList<MedicinePlan> medicinePlanList = medicinePlanUtil.getMedicinePlanListWithScheduleId(schedule.getSqlId());
        schedule.setMedicinePlanList(medicinePlanList);
        Cursor c = null;
        if(medicinePlanList != null) {
            int[] schedId = new int[medicinePlanList.size()];
            for (int i = 0; i < medicinePlanList.size(); i++) {
                schedId[i] = medicinePlanList.get(i).getMedicineId();
            }
            c = medicineUtil.getMedicine(schedId);
        }
        return c;
    }

    public void initializeAdapter(){
        viewScheduleMedicineAdapter = new ViewScheduleMedicineAdapter(contextHolder, getMedicine(schedule), Medicine.COLUMN_ID);
        viewScheduleMedicineAdapter.setOnViewScheduleMedicineClickListener(new ViewScheduleMedicineAdapter.OnViewScheduleMedicineClickListener() {
            @Override
            public void onItemClick() {
                showMedicineList();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnViewScheduleDetailsFragmentInteractionListener) {
            OnViewScheduleDetailsFragmentInteractionListener = (OnViewScheduleDetailsFragmentInteractionListener) context;
            contextHolder = context;
            Log.wtf("CHECKING CONTEXT", "CONTEXT IS " + (contextHolder != null));
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnViewScheduleDetailsFragmentInteractionListener");
        }
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        OnViewScheduleDetailsFragmentInteractionListener = null;
//    }

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

        input.setText(tvLabel.getText().toString());
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
        tvLabel.setText(newValue);
    }

    public interface OnViewScheduleDetailsFragmentInteractionListener {
        void onViewScheduleDetailsFragmentRepeatClick(TextView tvRepeat);
        void onViewScheduleDetailsFragmentTimeClick(TextView tvTime, TextView tvTimePeriod);
        void onViewScheduleDetailsFragmentClose(Schedule schedule);
        void onViewScheduleMedicineFragmentDelete(int id);
    }


    //TODO lookign for better ways to do this
    public  Schedule updateScheduleFromUserInput(Schedule schedule){
        long nextDrinkingTime = DateUtil.getTime(tvScheduleTime.getText().toString() + " " + tvScheduleTimePeriod.getText().toString());
        String label = tvLabel.getText().toString();
        boolean isVibrate = switchIsVibrate.isChecked();
        int[] timeValues = DateUtil.parseFromTimePicker(tvRepeat.getText().toString());
        long drinkingInterval = cbRepeat.isChecked() ? (timeValues[0] * 60 + timeValues[1]) : 0;

        Log.wtf("NEW MEDICINES", "NEXT DRINKING TIME: " + nextDrinkingTime);
        Log.wtf("NEW MEDICINES", "LABEL: " + label);
        Log.wtf("NEW MEDICINES", "ISVIBRATE IS " + isVibrate);
        Log.wtf("NEW MEDICINES", "DRINKING INTERVAL IS " + drinkingInterval);

        schedule.setNextDrinkingTime(nextDrinkingTime);
        schedule.setLabel(label);
        schedule.setVibrate(isVibrate);
        schedule.setRingtone(ringtoneUriUsed.toString());
        schedule.setDrinkingInterval(drinkingInterval);
        //OnViewScheduleDetailsFragmentInteractionListener.onAddScheduleFragmentSave(schedule);
        return schedule;
    }

    public void showMedicineList(){
        Log.wtf("CLICK", "CHECKED MEDICINE LIST");
        final ArrayList<Medicine> newMedicineList = new ArrayList<>();

        final AlertDialog.Builder alert = new AlertDialog.Builder(contextHolder);
        final RecyclerView rvMedicineSelection = new RecyclerView(contextHolder);
        final EditText input = new EditText(contextHolder);

        ArrayList<Integer> idsToRemove = new ArrayList<>();
        if(schedule.getMedicinePlanList() != null) {
            for (int i = 0; i < schedule.getMedicinePlanList().size(); i++) {
                idsToRemove.add(schedule.getMedicinePlanList().get(i).getMedicineId());
            }
        }

        ViewScheduleMedicineSelectionAdapter viewScheduleMedicineSelectionAdapter =
                new ViewScheduleMedicineSelectionAdapter(
                        contextHolder,
                        medicineUtil.getAllMedicine(),
                        Medicine.COLUMN_ID,
                        idsToRemove);
        viewScheduleMedicineSelectionAdapter.setOnViewScheduleMedicineSelectionClickListener(new ViewScheduleMedicineSelectionAdapter.OnViewScheduleMedicineSelectionClickListener() {
            @Override
            public void onItemCheck(Medicine medicine) {
                newMedicineList.add(medicine);
            }

            @Override
            public void onItemUncheck(Medicine medicine) {
                newMedicineList.remove(medicine);
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(contextHolder);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        rvMedicineSelection.setAdapter(viewScheduleMedicineSelectionAdapter);
        rvMedicineSelection.setLayoutManager(mLayoutManager);

        ScrollView container = new ScrollView(contextHolder);
        ScrollView.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_left_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_right_margin);

//        input.setText(tvScheduleLabel.getText().toString());
//        input.setSingleLine();
        rvMedicineSelection.setLayoutParams(params);

        container.addView(rvMedicineSelection);

        alert.setTitle("Choose Medicines To Add");
        alert.setView(container);
        alert.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString().trim();
                //TODO get Medicine checked from the dialog
                //schedule.setMedicinePlanList();
                editMedicineList(newMedicineList);
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = alert.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                int[] attrs = {android.R.attr.colorPrimary};
                TypedArray typedArray = contextHolder.obtainStyledAttributes(attrs);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(typedArray.getColor(0, Color.BLACK));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(typedArray.getColor(0, Color.BLACK));
            }
        });


        dialog.show();
    }

    public void editMedicineList(ArrayList<Medicine> medicinePlanToAdd){
        int schedulePlanIdToChange = schedule.getSqlId();
        if(schedule.getMedicinePlanList() != null) {
            int[] medicinePlanIdToRemove = new int[schedule.getMedicinePlanList().size()];

            for (int i = 0; i < medicinePlanIdToRemove.length; i++) {
                medicinePlanIdToRemove[i] = schedule.getMedicinePlanList().get(i).getMedicineId();
                Log.wtf("MEDICINEPLAN", "WILL REMOVE MED PLAN WITH ID " + medicinePlanIdToRemove);
                medicinePlanUtil.deleteMedicinePlan(medicinePlanIdToRemove[i]);
            }
            schedulePlanUtil.deleteSchedulePlan(schedulePlanIdToChange);
            Log.wtf("END", "WILL REMOVE " + medicinePlanIdToRemove.length + " MED PLAN");
        }
        for(int i = 0; i < medicinePlanToAdd.size(); i++){
            long medicinePlanIdToAdd = medicinePlanUtil.addMedicinePlan(MedicinePlanInstantiatorUtil.convertMedicineToMedicinePlan(medicinePlanToAdd.get(i)));
            schedulePlanUtil.addSchedulePlan(schedulePlanIdToChange, medicinePlanIdToAdd);
        }
        updateMedicineList();
    }

    public void updateRecyclerViewVisibility(){
        int rv_visibility = viewScheduleMedicineAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE;
        int tv_visibility = viewScheduleMedicineAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE;
        rvMedicineView.setVisibility(rv_visibility);
        tvMedicineEmpty.setVisibility(tv_visibility);
    }
}
