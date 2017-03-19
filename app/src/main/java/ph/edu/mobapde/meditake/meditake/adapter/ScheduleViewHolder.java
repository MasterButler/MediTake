package ph.edu.mobapde.meditake.meditake.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;

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
    LinearLayout linEditSchedule;
    @BindView(R.id.lin_delete_schedule)
    LinearLayout linDeleteSchedule;

    @BindView(R.id.lin_save_schedule)
    LinearLayout linSaveSchedule;
    @BindView(R.id.lin_cancel_schedule)
    LinearLayout linCancelSchedule;

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
        etScheduleTimePeriod.setVisibility(isMilitary ? View.GONE : edit_mode);

    }
}