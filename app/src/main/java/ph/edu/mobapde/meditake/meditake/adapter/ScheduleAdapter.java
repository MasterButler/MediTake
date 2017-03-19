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
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.listener.OnScheduleClickListener;
import ph.edu.mobapde.meditake.meditake.util.MedicineInstantiatorUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;

/**
 * Created by Winfred Villaluna on 3/17/2017.
 */

public class ScheduleAdapter extends CursorRecyclerViewAdapter<ScheduleViewHolder>{

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
        this.contextHolder = context;
        expandedPositionId = -1;
        editingPositionId = -1;
        setHasStableIds(true);

        isMilitary = false;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder viewHolder, Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Schedule.COLUMN_ID));
        int medicineId = cursor.getInt(cursor.getColumnIndex(Schedule.COLUMN_MEDICINE_TO_DRINK));
        double dosagePerDrinkingInterval = cursor.getDouble(cursor.getColumnIndex(Schedule.COLUMN_DOSAGE_PER_DRINKING_INTERVAL));
        double drinkingInterval = cursor.getDouble(cursor.getColumnIndex(Schedule.COLUMN_DOSAGE_PER_DRINKING_INTERVAL));
        long lastTimeTaken = cursor.getLong(cursor.getColumnIndex(Schedule.COLUMN_LAST_TIME_TAKEN));
        boolean isActivated = cursor.getInt(cursor.getColumnIndex(Schedule.COLUMN_IS_ACTIVATED)) == 1 ? true : false;

        String brandName  = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_BRAND_NAME));
        String genericName  = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_GENERIC_NAME));
        String medicineFor =  cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_FOR));
        double amount = cursor.getDouble(cursor.getColumnIndex(Medicine.COLUMN_AMOUNT));
        String medicineType = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_TYPE));

        Log.d("ID", "STR");
        Log.d("ID", "EXPAND  ID: " + expandedPositionId);
        Log.d("ID", "EDITING ID: " + editingPositionId);
        Log.d("ID", "SCHEDULEID: " + id);
        Log.d("ID", "END");

        if(id != -1){
            Medicine med = MedicineInstantiatorUtil.createMedicineInstanceFromString(medicineType);
            med.setSqlId(medicineId);
            med.setBrandName(brandName);
            med.setGenericName(genericName);
            med.setMedicineFor(medicineFor);
            med.setAmount(amount);

            Schedule sched = new Schedule();
            sched.setSqlId(id);
            sched.setMedicineToDrink(med);
            sched.setDosagePerDrinkingInterval(dosagePerDrinkingInterval);
            sched.setDrinkingInterval(drinkingInterval);
            sched.setLastTimeTaken(lastTimeTaken);
            sched.setActivated(isActivated);

            Log.d("CHECK", "SCHEDULEID: " + sched.getSqlId());

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
                        Log.d("action", "EXPANDING ITEM WITH ID OF " + value /*+ " AND MEDICINE ID OF " +*/);
                        onScheduleClickListener.onItemClick(value);
                    }
                }
            });

            boolean isEditing = id == editingPositionId;
            viewHolder.linEditSchedule.setTag(id);
            setMode(viewHolder, isEditing ? MedicineViewHolder.EDIT_MODE : MedicineViewHolder.VIEW_MODE);
            viewHolder.linEditSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onScheduleClickListener != null){
                        int value = (int)v.getTag();
                        onScheduleClickListener.onItemEditClick(value);
                    }
                }
            });

            boolean isDone = !(id == editingPositionId);
            setMode(viewHolder, isDone ? MedicineViewHolder.VIEW_MODE : MedicineViewHolder.EDIT_MODE);
            viewHolder.linCancelSchedule.setTag(id);
            viewHolder.linCancelSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onScheduleClickListener != null){
                        int value = (int)v.getTag();
                        if(value != -1){
                            onScheduleClickListener.onItemCancelClick(value);
                        }
                    }
                }
            });

            viewHolder.linDeleteSchedule.setTag(id);
            viewHolder.linDeleteSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onScheduleClickListener != null){
                        int value = (int)v.getTag();
                        if(value != -1){
                            Log.wtf("action", "DELETING SCHEDULE WITH ID OF " + value);
                            onScheduleClickListener.onItemDeleteClick(value);
                        }
                    }
                }
            });

            viewHolder.scheduleSwitch.setTag(sched);
            viewHolder.scheduleSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onScheduleClickListener != null){
                        Schedule value = (Schedule) v.getTag();
                        Log.wtf("action", "SWITCHING SCHEDULE WITH ID OF " + value.getSqlId());
                        onScheduleClickListener.onSwitchClick(value);
                    }
                }
            });

        }
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    public void setMode(ScheduleViewHolder viewHolder, int mode){
        viewHolder.setMode(mode);
    }


    public void setOnScheduleClickListener(OnScheduleClickListener onScheduleClickListener) {
        this.onScheduleClickListener = onScheduleClickListener;
    }
}
