package ph.edu.mobapde.meditake.meditake.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.TimeUtil;

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

    public ScheduleAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        medicineUtil = new MedicineUtil(contextHolder);
        this.contextHolder = context;
        expandedPositionId = -1;
        editingPositionId = -1;
        setHasStableIds(true);

        scheduleUtil = new ScheduleUtil(context);
        medicineUtil = new MedicineUtil(context);
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

            boolean isMilitaryTime = true;
            long nextTimeToTake = sched.getNextDrinkingTime();
            String displayTime = TimeUtil.getTime(nextTimeToTake, isMilitaryTime);
            if(isMilitaryTime){
                viewHolder.tvScheduleTime.setText(displayTime);
                viewHolder.tvScheduleTimePeriod.setVisibility(View.GONE);
            }else{
                viewHolder.tvScheduleTime.setText(displayTime.split("\\s")[0]);
                viewHolder.tvScheduleTimePeriod.setText(displayTime.split("\\s")[1]);
            }
            String medicineDisplay = dosagePerDrinkingInterval
                    + sched.getMedicineToDrink().getModifier() + " of " + sched.getMedicineToDrink().getName();
            viewHolder.tvMedicineToDrink.setText(medicineDisplay);
            viewHolder.scheduleSwitch.setChecked(isActivated);

            Log.wtf("action", "ACTIVATED: " + isActivated);
            Log.wtf("check", "VALUE OF SWITCH IS " + viewHolder.scheduleSwitch.isChecked());
        }
    }

    @Override
    public ScheduleAdapter.ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_schedule, parent, false);
        return new ScheduleViewHolder(v);
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

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parentView = itemView;

        }
    }
}
