package ph.edu.mobapde.meditake.meditake.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
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
import android.widget.TimePicker;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.activity.ScheduleListActivity;
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
    public void onBindViewHolder(final ScheduleViewHolder viewHolder, Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Schedule.COLUMN_ID));
        int medicineId = cursor.getInt(cursor.getColumnIndex(Schedule.COLUMN_MEDICINE_TO_DRINK));
        double dosagePerDrinkingInterval = cursor.getDouble(cursor.getColumnIndex(Schedule.COLUMN_DOSAGE_PER_DRINKING_INTERVAL));
        double drinkingInterval = cursor.getDouble(cursor.getColumnIndex(Schedule.COLUMN_DOSAGE_PER_DRINKING_INTERVAL));
        long lastTimeTaken = cursor.getLong(cursor.getColumnIndex(Schedule.COLUMN_LAST_TIME_TAKEN));
        boolean isActivated = cursor.getInt(cursor.getColumnIndex(Schedule.COLUMN_IS_ACTIVATED)) == 1 ? true : false;
        long customNextDrinkingTime = cursor.getLong(cursor.getColumnIndex(Schedule.COLUMN_CUSTOM_NEXT_DRINKING_TIME));

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
            sched.setCustomNextDrinkingTime(customNextDrinkingTime);

            Log.d("CHECK", "SCHEDULEID: " + sched.getSqlId());

            long nextTimeToTake = sched.getNextDrinkingTime();
            String displayTime = DateUtil.getTime(nextTimeToTake, isMilitary);

            viewHolder.cardViewBackground.setImageResource(DateUtil.pickBackground(sched.getNextDrinkingTime()));
            viewHolder.cardViewBackground.setColorFilter(Color.parseColor("#AAFFFFFF"), PorterDuff.Mode.SRC_ATOP);

            viewHolder.cardEditBackground.setImageResource(DateUtil.pickBackground(sched.getNextDrinkingTime()));
            viewHolder.cardEditBackground.setColorFilter(Color.parseColor("#AAFFFFFF"), PorterDuff.Mode.SRC_ATOP);

            if(isMilitary){
                viewHolder.tvScheduleTimePeriod.setVisibility(View.GONE);

                viewHolder.tvScheduleTime.setText(displayTime);
                viewHolder.etScheduleTime.setText(displayTime);
            }else{
                viewHolder.tvScheduleTimePeriod.setVisibility(View.VISIBLE);

                viewHolder.tvScheduleTime.setText(displayTime.split("\\s")[0]);
                viewHolder.tvScheduleTimePeriod.setText(displayTime.split("\\s")[1]);

                viewHolder.etScheduleTime.setText(displayTime.split("\\s")[0]);
                viewHolder.etScheduleTimePeriod.setText(displayTime.split("\\s")[1]);
            }

            Log.d("action", String.valueOf(dosagePerDrinkingInterval).split("\\.")[1].equals("0") + " <-- RESULT");

            String dosagePerDrinkingIntervalDisplay;
            String drinkingIntervalDisplay;
            String medicineDisplay;

            if(String.valueOf(dosagePerDrinkingInterval).split("\\.")[1].equals("0")){
                dosagePerDrinkingIntervalDisplay = String.valueOf(dosagePerDrinkingInterval).split("\\.")[0];
            }else{
                dosagePerDrinkingIntervalDisplay = String.valueOf(dosagePerDrinkingInterval);
            }

            if(String.valueOf(drinkingInterval).split("\\.")[1].equals("0")){
                drinkingIntervalDisplay = String.valueOf(drinkingInterval).split("\\.")[0];
            }else{
                drinkingIntervalDisplay = String.valueOf(drinkingInterval);
            }

            medicineDisplay = dosagePerDrinkingIntervalDisplay  + " "
                    + sched.getMedicineToDrink().getModifier() + " of " + sched.getMedicineToDrink().getName();
            viewHolder.tvMedicineToDrink.setText(medicineDisplay);
            viewHolder.scheduleSwitch.setChecked(isActivated);
            viewHolder.tvDrinkingInterval.setText("Medicine taken every " + drinkingIntervalDisplay + " hours.");
            viewHolder.tvLastTaken.setText("Last taken: " + DateUtil.getDateTime(sched.getLastTimeTaken(), isMilitary));

            //viewHolder.spinnerMedicineToDrink.
            viewHolder.etDosagePerDrinkingInterval.setText(dosagePerDrinkingIntervalDisplay);
            viewHolder.etDrinkingIntervals.setText(String.valueOf(drinkingInterval));

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

            viewHolder.linSaveSchedule.setTag(R.string.SCHEDULE, sched);
            viewHolder.linSaveSchedule.setTag(R.string.SCHEDULE_HOLDER, viewHolder);
            viewHolder.linSaveSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onScheduleClickListener != null){
                        boolean isDosagePerDrinkingIntervalEmpty = viewHolder.etDosagePerDrinkingInterval.getText().toString().isEmpty();
                        boolean isDrinkingIntervalsEmpty = viewHolder.etDrinkingIntervals.getText().toString().isEmpty();
                        Log.d("check bool", "dosage is " + isDosagePerDrinkingIntervalEmpty);
                        if(isDosagePerDrinkingIntervalEmpty || isDrinkingIntervalsEmpty){
                            Log.d("false bool", "SHOWING ERROR");
                            viewHolder.etDosagePerDrinkingInterval.setError(isDosagePerDrinkingIntervalEmpty ? "Dosage must not be empty" : null);
                            viewHolder.etDrinkingIntervals.setError(isDrinkingIntervalsEmpty ? "Drinking Intervals must not be empty" : null);
                        }else{
                            double updatedDosagePerDrinkingInterval = Double.valueOf(viewHolder.etDosagePerDrinkingInterval.getText().toString());
                            double updatedDrinkingInterval = Double.valueOf(viewHolder.etDrinkingIntervals.getText().toString());
                            long updatedTime;
                            if(isMilitary){
                                updatedTime = DateUtil.addDate(DateUtil.parseToLong(viewHolder.etScheduleTime.getText().toString()));
                            }else{
                                updatedTime = DateUtil.addDate(DateUtil.parseToLong(viewHolder.etScheduleTime.getText().toString(), viewHolder.etScheduleTimePeriod.getText().toString()));
                            }

                            Schedule sched = (Schedule) v.getTag(R.string.SCHEDULE);
                            sched.setDosagePerDrinkingInterval(updatedDosagePerDrinkingInterval);
                            sched.setDrinkingInterval(updatedDrinkingInterval);

                            Log.d("action", "COMPARING " + DateUtil.getTime(updatedTime,isMilitary) + " AND " + DateUtil.getTime(sched.getNextDrinkingTime(), isMilitary));

                            if(!DateUtil.getTime(updatedTime, isMilitary).equals(DateUtil.getTime(sched.getNextDrinkingTime(), isMilitary))){
                                long updatedTimeDate = DateUtil.addDate(updatedTime);
                                Log.d("action", "CHANGING SET CUSTOM NEXT DRINKING TIME TO " + DateUtil.getTime(updatedTimeDate, isMilitary));
                                sched.setCustomNextDrinkingTime(updatedTimeDate);
                            }
                            onScheduleClickListener.onItemSaveClick(sched);
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

            viewHolder.etScheduleTime.setTag(sched);
            viewHolder.etScheduleTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onScheduleClickListener!= null){
                        Schedule sched = (Schedule) v.getTag();
                        onScheduleClickListener.onEditTimeClick(sched, viewHolder.etScheduleTime, viewHolder.etScheduleTimePeriod, isMilitary);
                    }
                }
            });

            viewHolder.tvToFragmentMedicineToDrink.setTag(sched);
            viewHolder.tvToFragmentMedicineToDrink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onScheduleClickListener != null){
                        Schedule sched = (Schedule) v.getTag();
                        onScheduleClickListener.onMedicineListClick(sched, viewHolder.tvToFragmentMedicineToDrink);
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
