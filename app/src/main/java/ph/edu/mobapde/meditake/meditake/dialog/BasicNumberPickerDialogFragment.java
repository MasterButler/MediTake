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
import android.widget.TextView;

import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;

/**
 * Created by Winfred Villaluna on 4/11/2017.
 */

public class BasicNumberPickerDialogFragment extends DialogFragment {
    OnDialogClickListener onClickListener;
    String title;
    String positiveText;
    String negativeText;
    String inputText;
    int upperLimit;
    int lowerLimit;

    int initialValue;
    String label;

    public BasicNumberPickerDialogFragment(){

    }

    @SuppressLint("ValidFragment")
    public BasicNumberPickerDialogFragment(String title, String positiveText, String negativeText, String inputText, int lowerLimit, int upperLimit){
        this.title = title;
        this.positiveText = positiveText;
        this.negativeText = negativeText;
        this.inputText = inputText;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FrameLayout container = new FrameLayout(getActivity());
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_left_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_right_margin);

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_number_picker, null);
        final NumberPicker numPicker = (NumberPicker) v.findViewById(R.id.medicine_number_picker);
        final TextView npLabel = (TextView) v.findViewById(R.id.medicine_number_picker_label);

        container.addView(v);

        numPicker.setMinValue(lowerLimit);
        numPicker.setMaxValue(upperLimit);

        if(inputText!= null){
            initialValue = Integer.valueOf(inputText.trim().split("\\s+")[0]);
            label = inputText.trim().split("\\s+")[1];

            numPicker.setValue(initialValue);
            npLabel.setText(label);
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(container)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(onClickListener != null){
                            onClickListener.onPositiveSelected(numPicker.getValue() + " " + label);
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
        void onPositiveSelected(String msg);
        void onNegativeSelected();
    }
}
