package ph.edu.mobapde.meditake.meditake.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.SystemClock;
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
import ph.edu.mobapde.meditake.meditake.service.AlarmReceiver;
import ph.edu.mobapde.meditake.meditake.util.MedicineInstantiatorUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleInstantiatorUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;

import static android.content.Context.ALARM_SERVICE;

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

    public ScheduleAdapter(Context context, Cursor cursor, String column) {
        super(context, cursor, column);
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
        if(id != -1){

            Schedule sched = ScheduleInstantiatorUtil.createBeanFromCursor(cursor);

            Log.d("CHECK", "SCHEDULEID: " + sched.getSqlId());

            String displayTime = DateUtil.convertToReadableFormat(sched.getNextDrinkingTime(), isMilitary);
            Log.d("CHECK", "HOURS: " + sched.getNextDrinkingTime()/DateUtil.MILLIS_TO_HOURS);
            Log.d("CHECK", "DISPLAYTIME: " + displayTime);

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

            String drinkingTime = DateUtil.parseToTimePickerDisplay(sched.getDrinkingInterval());
            viewHolder.tvMedicineToDrink.setText("TO BE FIXED");
            viewHolder.scheduleSwitch.setChecked(sched.isActivated());
            viewHolder.tvDrinkingInterval.setText(sched.getNextDrinkingTime() == 0 ? drinkingTime : "Medicine taken " + drinkingTime.toLowerCase() + ".");

            viewHolder.etDrinkingIntervals.setText(String.valueOf(sched.getDrinkingInterval()));

            Log.wtf("action", "ACTIVATED: " + sched.isActivated());
            Log.wtf("check", "VALUE OF SWITCH IS " + viewHolder.scheduleSwitch.isChecked());



//            if(sched.isActivated()){
//            Intent intent = new Intent(contextHolder, AlarmReceiver.class);
////            intent.putExtra(getString(R.string.SCHEDULE_ID), (int)schedule.getSqlId());
////            intent.putExtra(getString(R.string.MEDICINE_ID), (int)schedule.getMedicineToDrink().getSqlId());
//
//            PendingIntent pendingAlarm = PendingIntent.getBroadcast(contextHolder, AlarmReceiver.PENDING_ALARMRECEIVER, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            AlarmManager alarmManager = (AlarmManager) contextHolder.getSystemService(ALARM_SERVICE);
//
//            long delay = sched.getNextDrinkingTime() - System.currentTimeMillis();
//            Log.d("action", "DELAY IS " + delay + " milliseconds (" + (delay/DateUtil.MILLIS_TO_SECONDS) + ")");
//            alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 5 * 1000, pendingAlarm);
//            }

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
            setMode(viewHolder, isEditing ? ScheduleViewHolder.EDIT_MODE : ScheduleViewHolder.VIEW_MODE);
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
            setMode(viewHolder, isDone ? ScheduleViewHolder.VIEW_MODE : ScheduleViewHolder.EDIT_MODE);
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
//                        boolean isDosagePerDrinkingIntervalEmpty = viewHolder.etDosagePerDrinkingInterval.getText().toString().isEmpty();
//                        boolean isDrinkingIntervalsEmpty = viewHolder.etDrinkingIntervals.getText().toString().isEmpty();
//                        Log.d("check bool", "dosage is " + isDosagePerDrinkingIntervalEmpty);
//                        if(isDosagePerDrinkingIntervalEmpty || isDrinkingIntervalsEmpty){
//                            Log.d("false bool", "SHOWING ERROR");
//                            viewHolder.etDosagePerDrinkingInterval.setError(isDosagePerDrinkingIntervalEmpty ? "Dosage must not be empty" : null);
//                            viewHolder.etDrinkingIntervals.setError(isDrinkingIntervalsEmpty ? "Drinking Intervals must not be empty" : null);
//                        }else{
//                            double updatedDosagePerDrinkingInterval = Double.valueOf(viewHolder.etDosagePerDrinkingInterval.getText().toString());
//                            double updatedDrinkingInterval = Double.valueOf(viewHolder.etDrinkingIntervals.getText().toString());
//                            long updatedTime;
//
//                            while(updatedTime < System.currentTimeMillis()){
//                                updatedTime += System.currentTimeMillis();
//                            }
//
//                            Schedule sched = (Schedule) v.getTag(R.string.SCHEDULE);
//                            sched.setDosagePerDrinkingInterval(updatedDosagePerDrinkingInterval);
//                            sched.setDrinkingInterval(updatedDrinkingInterval);
//                            Log.d("action", "COMPARING " + DateUtil.getTime(updatedTime,isMilitary) + " AND " + DateUtil.getTime(sched.getNextDrinkingTime(), isMilitary));
//
//                            if(updatedTime != sched.getLastTimeTaken() + sched.getDrinkingInterval() * DateUtil.MILLIS_TO_HOURS){
//                                sched.setCustomNextDrinkingTime(updatedTime);
//                            }
//                            onScheduleClickListener.onItemSaveClick(sched);
//                        }
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
                        //onScheduleClickListener.onEditTimeClick(sched, viewHolder.etScheduleTime, viewHolder.etScheduleTimePeriod, isMilitary);
                    }
                }
            });

            viewHolder.tvToFragmentMedicineToDrink.setTag(sched);
            viewHolder.tvToFragmentMedicineToDrink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onScheduleClickListener != null){
                        Schedule sched = (Schedule) v.getTag();
                        //onScheduleClickListener.onMedicineListClick(sched, viewHolder.tvToFragmentMedicineToDrink);
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
