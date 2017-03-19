package ph.edu.mobapde.meditake.meditake.listener;

import android.widget.EditText;
import android.widget.TextView;

import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;

/**
 * Created by Winfred Villaluna on 3/18/2017.
 */

public interface OnScheduleClickListener {
    public void onItemClick(int id);
    public void onItemDeleteClick(int id);
    public void onItemEditClick(int id);
    public void onItemSaveClick(Schedule schedule);
    public void onItemCancelClick(int id);
    public void onSwitchClick(Schedule schedule);
    public void onEditTimeClick(Schedule schedule, EditText etTime, TextView tvTimePeriod, boolean isMilitary);
    public void onMedicineListClick(Schedule schdule, TextView tvToFragmentMedicineToDrink);
}
