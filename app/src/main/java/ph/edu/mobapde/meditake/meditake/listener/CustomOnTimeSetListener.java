package ph.edu.mobapde.meditake.meditake.listener;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Winfred Villaluna on 3/19/2017.
 */

public class CustomOnTimeSetListener implements TimePickerDialog.OnTimeSetListener {
    EditText etTimeDisplay = null;
    TextView tvTimePeriodDisplay = null;
    Context contextHolder;
    boolean isMilitary;

    public CustomOnTimeSetListener(Context context, EditText etTimeDisplay, TextView tvTimePeriodDisplay, boolean isMilitary){
        this.etTimeDisplay = etTimeDisplay;
        this.tvTimePeriodDisplay = tvTimePeriodDisplay;
        this.contextHolder = context;
    }

    @SuppressWarnings("WrongConstant")
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // TODO time set
        String am_pm = "";

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);

        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        int editedHourOfDay = isMilitary ? hourOfDay : hourOfDay % 12 ;

        this.etTimeDisplay.setText(String.format("%02d", editedHourOfDay) + ":" + String.format("%02d", minute));
        this.tvTimePeriodDisplay.setText(am_pm);
    }

}
