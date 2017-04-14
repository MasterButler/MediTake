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
    public void onSwitchClick(Schedule schedule);
}
