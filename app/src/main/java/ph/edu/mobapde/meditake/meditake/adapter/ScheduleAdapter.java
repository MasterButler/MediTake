package ph.edu.mobapde.meditake.meditake.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.listener.OnScheduleClickListener;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;

/**
 * Created by Winfred Villaluna on 3/17/2017.
 */

public class ScheduleAdapter extends CursorRecyclerViewAdapter<ScheduleAdapter.ScheduleViewHolder>{

    int expandedPositionId;
    int editingPositionId;
    RecyclerView mRecyclerView;
    Context contextHolder;

    MedicineUtil medicineUtil;
    ScheduleUtil scheduleUtil;

    boolean isMilitary;

    private OnScheduleClickListener onScheduleClickListener;

    public ScheduleAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        medicineUtil = new MedicineUtil(contextHolder);
        this.contextHolder = context;
        expandedPositionId = -1;
        editingPositionId = -1;
        setHasStableIds(true);

        scheduleUtil = new ScheduleUtil(context);
        medicineUtil = new MedicineUtil(context);
        isMilitary = false;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(ScheduleAdapter.ScheduleViewHolder viewHolder, Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Schedule.COLUMN_ID));
        int medicineId = cursor.getInt(cursor.getColumnIndex(Schedule.COLUMN_MEDICINE_TO_DRINK));
        double dosagePerDrinkingInterval = cursor.getDouble(cursor.getColumnIndex(Schedule.COLUMN_DOSAGE_PER_DRINKING_INTERVAL));
        double drinkingInterval = cursor.getDouble(cursor.getColumnIndex(Schedule.COLUMN_DOSAGE_PER_DRINKING_INTERVAL));
        long lastTimeTaken = cursor.getLong(cursor.getColumnIndex(Schedule.COLUMN_LAST_TIME_TAKEN));
        boolean isActivated = cursor.getInt(cursor.getColumnIndex(Schedule.COLUMN_IS_ACTIVATED)) == 1 ? true : false;

        Log.d("ID", "EXPAND  ID: " + expandedPositionId);
        Log.d("ID", "EDITING ID: " + editingPositionId);

        if(id != -1){
            Schedule sched = new Schedule();
            sched.setMedicineToDrink(medicineUtil.getMedicine(medicineId));
            sched.setDosagePerDrinkingInterval(dosagePerDrinkingInterval);
            sched.setDrinkingInterval(drinkingInterval);
            sched.setLastTimeTaken(lastTimeTaken);
            sched.setActivated(isActivated);

            long nextTimeToTake = sched.getNextDrinkingTime();
            String displayTime = DateUtil.getTime(nextTimeToTake, isMilitary);
            if(isMilitary){
                viewHolder.tvScheduleTimePeriod.setVisibility(View.GONE);
                viewHolder.tvScheduleTime.setText(displayTime);
            }else{
                viewHolder.tvScheduleTimePeriod.setVisibility(View.VISIBLE);
                viewHolder.tvScheduleTime.setText(displayTime.split("\\s")[0]);
                viewHolder.tvScheduleTimePeriod.setText(displayTime.split("\\s")[1]);
            }
            String medicineDisplay = dosagePerDrinkingInterval
                    + sched.getMedicineToDrink().getModifier() + " of " + sched.getMedicineToDrink().getName();
            viewHolder.tvMedicineToDrink.setText(medicineDisplay);
            viewHolder.scheduleSwitch.setChecked(isActivated);
            viewHolder.tvDrinkingInterval.setText("Medicine taken every " + sched.getDrinkingInterval() + " hours.");
            viewHolder.tvLastTaken.setText("Last taken: " + DateUtil.getDateTime(sched.getLastTimeTaken(), isMilitary));

            Log.wtf("action", "ACTIVATED: " + isActivated);
            Log.wtf("check", "VALUE OF SWITCH IS " + viewHolder.scheduleSwitch.isChecked());

            //TODO add listeners here
            boolean isExpanded = id == expandedPositionId;
            viewHolder.parentView.setTag(id);
            viewHolder.linExpandedInformation.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            viewHolder.parentView.setActivated(isExpanded);
            viewHolder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onScheduleClickListener != null){
                        int value = (int)v.getTag();
                        onScheduleClickListener.onItemClick(value);
                    }
                }
            });
        }
    }

    @Override
    public ScheduleAdapter.ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_schedule, parent, false);
        return new ScheduleViewHolder(v);
    }

    public boolean isExpanded(int id){
        return (id == expandedPositionId) ? true : false;
    }

    public boolean isEditing(int id){
        return (id == editingPositionId) ? true: false;
    }

    public int getExpandedPositionId() {
        return expandedPositionId;
    }

    public void setExpandedPositionId(int expandedPositionId) {
        this.expandedPositionId = expandedPositionId;
    }

    public int getEditingPositionId() {
        return editingPositionId;
    }

    public void setEditingPositionId(int editingPositionId) {
        this.editingPositionId = editingPositionId;
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder{

        View parentView;
        @BindView(R.id.switch_schedule)
        Switch scheduleSwitch;
        @BindView(R.id.tv_schedule_medicine_to_drink)
        TextView tvMedicineToDrink;
        @BindView(R.id.tv_schedule_time)
        TextView tvScheduleTime;
        @BindView(R.id.tv_schedule_time_period)
        TextView tvScheduleTimePeriod;
        @BindView(R.id.tv_medicine_drinking_interval)
        TextView tvDrinkingInterval;
        @BindView(R.id.tv_medicine_last_taken)
        TextView tvLastTaken;

        @BindView(R.id.et_schedule_time)
        EditText etScheduleTime;
        @BindView(R.id.et_schedule_time_period)
        TextView etScheduleTimePeriod;
        @BindView(R.id.list_medicine_to_drink)
        Spinner spinnerMedicineToDrink;
        @BindView(R.id.et_schedule_dosage)
        EditText etDosagePerDrinkingInterval;
        @BindView(R.id.et_medicine_drinking_interval)
        EditText etDrinkingIntervals;

        @BindView(R.id.lin_edit_schedule)
        LinearLayout linEditMedicine;
        @BindView(R.id.lin_delete_schedule)
        LinearLayout linDeleteMedicine;

        @BindView(R.id.lin_save_schedule)
        LinearLayout linSaveMedicine;
        @BindView(R.id.lin_cancel_schedule)
        LinearLayout linCancelMedicine;

        @BindView(R.id.lin_schedule_expanded_information)
        LinearLayout linExpandedInformation;
        @BindView(R.id.card_view_schedule)
        LinearLayout viewSchedule;
        @BindView(R.id.card_edit_schedule)
        LinearLayout editSchedule;

        private boolean isMilitary;
        public static final int VIEW_MODE = 1;
        public static final int EDIT_MODE = 2;


        public ScheduleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parentView = itemView;

            setMode(VIEW_MODE);
        }

        public ScheduleViewHolder(View itemView, boolean isMilitary) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parentView = itemView;

            this.isMilitary = isMilitary;

        }

        public void setMode(int mode){
            int view_mode = mode == VIEW_MODE ? View.VISIBLE : View.GONE;
            int edit_mode = mode == EDIT_MODE ? View.VISIBLE : View.GONE;

            viewSchedule.setVisibility(view_mode);
            tvScheduleTimePeriod.setVisibility(isMilitary ? View.GONE : view_mode);
            editSchedule.setVisibility(edit_mode);
            etScheduleTimePeriod.setVisibility(isMilitary ? View.GONE : view_mode);

        }
    }

    public void setOnScheduleClickListener(OnScheduleClickListener onScheduleClickListener) {
        this.onScheduleClickListener = onScheduleClickListener;
    }
}
