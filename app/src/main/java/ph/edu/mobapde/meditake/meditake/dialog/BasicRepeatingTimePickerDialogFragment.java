package ph.edu.mobapde.meditake.meditake.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;

/**
 * Created by Winfred Villaluna on 4/11/2017.
 */

public class BasicRepeatingTimePickerDialogFragment extends DialogFragment {

    OnDialogClickListener onClickListener;
    String title;
    String positiveText;
    String negativeText;
    String repeatTime;
    int type;

    public BasicRepeatingTimePickerDialogFragment(){

    }

    @SuppressLint("ValidFragment")
    public BasicRepeatingTimePickerDialogFragment(String title, String positiveText, String negativeText, String repeatTime){
        this.title = title;
        this.positiveText = positiveText;
        this.negativeText = negativeText;
        this.repeatTime = repeatTime;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FrameLayout container = new FrameLayout(getActivity());
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

//        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_left_margin);
//        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_right_margin);

        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_repeating_time_picker, null);
        final NumberPicker npHour = (NumberPicker) v.findViewById(R.id.number_picker_hours);
        final NumberPicker npMinutes = (NumberPicker) v.findViewById(R.id.number_picker_minutes);

        container.addView(v);

        npHour.setMinValue(0);
        npHour.setMaxValue(24);
        npMinutes.setMinValue(0);
        npMinutes.setMaxValue(59);

        Log.wtf("REPEATTIME", "REPEATING TIME IS " + repeatTime);

        if(repeatTime != null){
            int hourValue = 0;
            int minuteValue = 1;
            int[] timeValues;
            if(!repeatTime.equals(DateUtil.REPEATING_TIME_NOT_SET)){
                timeValues = DateUtil.parseFromTimePicker(repeatTime);

                Log.wtf("GOT VALUES", timeValues[hourValue] + " hour(s) and " + timeValues[minuteValue] + " minutes");
                npHour.setValue(npHour.getMinValue() <= timeValues[hourValue] && npHour.getMaxValue() >= timeValues[hourValue] ? timeValues[hourValue] : 0);
                npMinutes.setValue(npMinutes.getMinValue() <= timeValues[minuteValue] && npMinutes.getMaxValue() >= timeValues[minuteValue] ? timeValues[minuteValue] : 0);
            }else{
                timeValues = new int[]{0, 0};
            }
        }

        npMinutes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(oldVal == npMinutes.getMinValue() && newVal == npMinutes.getMaxValue()){
                    npHour.setValue(npHour.getValue() != npHour.getMinValue() ? npHour.getValue()-1 : npHour.getValue());
                }else if(oldVal == npMinutes.getMaxValue() && newVal == npMinutes.getMinValue()){
                    npHour.setValue(npHour.getValue() != npHour.getMaxValue() ? npHour.getValue()+1 : npHour.getValue());
                }
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(container)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(onClickListener != null){
                            onClickListener.onPositiveSelected(npHour.getValue(), npMinutes.getValue());
                        }
                    }})
                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(onClickListener != null){
                            onClickListener.onNegativeSelected();

                        }
                    }
                }).create();
    }

    @Override
    public void onStart() {
        super.onStart();
        int[] attrs = {android.R.attr.colorAccent};
        TypedArray typedArray = getActivity().obtainStyledAttributes(attrs);
        ((AlertDialog) getDialog()).getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(typedArray.getColor(0, Color.BLACK));
        ((AlertDialog) getDialog()).getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(typedArray.getColor(0, Color.BLACK));
    }

    public void setOnClickListener(OnDialogClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnDialogClickListener{
        void onPositiveSelected(int hourValue, int minuteValue);
        void onNegativeSelected();
    }
}
