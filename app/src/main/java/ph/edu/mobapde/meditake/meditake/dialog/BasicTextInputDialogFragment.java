package ph.edu.mobapde.meditake.meditake.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import ph.edu.mobapde.meditake.meditake.R;

/**
 * Created by Winfred Villaluna on 4/11/2017.
 */

public class BasicTextInputDialogFragment extends DialogFragment {

    OnDialogClickListener onClickListener;
    String title;
    String positiveText;
    String negativeText;
    String inputText;
    int type;

    EditText input;

    public BasicTextInputDialogFragment(){

    }

    @SuppressLint("ValidFragment")
    public BasicTextInputDialogFragment(String title, String positiveText, String negativeText, String inputText, int type){
        this.title = title;
        this.positiveText = positiveText;
        this.negativeText = negativeText;
        this.inputText = inputText;
        this.type = type;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FrameLayout container = new FrameLayout(getActivity());
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_left_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_right_margin);

        input = new EditText(getActivity());
        input.setText(inputText);
        input.setLayoutParams(params);
        input.setSingleLine();
        input.setInputType(type);

        container.addView(input);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(container)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(onClickListener != null){
                            onClickListener.onPositiveSelected(input.getText().toString());
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
