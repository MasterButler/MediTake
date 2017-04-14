package ph.edu.mobapde.meditake.meditake.adapter.recyclerview;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import ph.edu.mobapde.meditake.meditake.listener.OnScheduleClickListener;
import ph.edu.mobapde.meditake.meditake.util.AlarmUtil;
import ph.edu.mobapde.meditake.meditake.util.TopCropImageView;
import ph.edu.mobapde.meditake.meditake.util.instantiator.ScheduleInstantiatorUtil;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;

/**
 * Created by Winfred Villaluna on 3/17/2017.
 */

public class ScheduleAdapter extends CursorRecyclerViewAdapter<ScheduleAdapter.ScheduleViewHolder>{

    RecyclerView mRecyclerView;
    Context contextHolder;

    boolean isMilitary;

    private OnScheduleClickListener onScheduleClickListener;

    public ScheduleAdapter(Context context, Cursor cursor, String column) {
        super(context, cursor, column);
        this.contextHolder = context;
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

            viewHolder.cardViewBackground.setImageResource(DateUtil.pickBackground(sched.getNextDrinkingTime()));
            viewHolder.cardViewBackground.setColorFilter(Color.parseColor("#AAFFFFFF"), PorterDuff.Mode.SRC_ATOP);

            viewHolder.tvMedicineToDrink.setText(DateUtil.convertToCardDisplayFormat(sched.getDrinkingInterval()));
            if(isMilitary){
                viewHolder.tvScheduleTimePeriod.setVisibility(View.GONE);

                viewHolder.tvScheduleTime.setText(displayTime);
            }else{
                viewHolder.tvScheduleTimePeriod.setVisibility(View.VISIBLE);

                viewHolder.tvScheduleTime.setText(displayTime.split("\\s")[0]);
                viewHolder.tvScheduleTimePeriod.setText(displayTime.split("\\s")[1]);
            }

//            String drinkingTime = DateUtil.parseToTimePickerDisplay(sched.getDrinkingInterval());
//            viewHolder.tvDrinkingInterval.setText(sched.getNextDrinkingTime() == 0 ? drinkingTime : "Medicine taken " + drinkingTime.toLowerCase() + ".");

            viewHolder.scheduleSwitch.setChecked(sched.isActivated());
            if(sched.isActivated()){
                AlarmUtil.setAlarmForSchedule(contextHolder, sched);
            }else{
                AlarmUtil.stopAssociatedAlarmsWithSchedule(contextHolder, sched);
            }

            viewHolder.parentView.setTag(id);
            viewHolder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onScheduleClickListener != null){
                        int value = (int)v.getTag();
                        onScheduleClickListener.onItemClick(value);
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

    public void setOnScheduleClickListener(OnScheduleClickListener onScheduleClickListener) {
        this.onScheduleClickListener = onScheduleClickListener;
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder{

        View parentView;
        @BindView(R.id.card_view_background)
        TopCropImageView cardViewBackground;
        @BindView(R.id.switch_schedule)
        Switch scheduleSwitch;
        @BindView(R.id.tv_schedule_medicine_to_drink)
        TextView tvMedicineToDrink;
        @BindView(R.id.tv_schedule_time)
        TextView tvScheduleTime;
        @BindView(R.id.tv_schedule_time_period)
        TextView tvScheduleTimePeriod;
//    @BindView(R.id.tv_medicine_drinking_interval)
//    TextView tvDrinkingInterval;
//    @BindView(R.id.tv_medicine_last_taken)
//    TextView tvLastTaken;

        private boolean isMilitary;
        public static final int VIEW_MODE = 1;
        public static final int EDIT_MODE = 2;


        public ScheduleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parentView = itemView;
        }

        public ScheduleViewHolder(View itemView, boolean isMilitary) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parentView = itemView;

            this.isMilitary = isMilitary;
        }
    }
}
