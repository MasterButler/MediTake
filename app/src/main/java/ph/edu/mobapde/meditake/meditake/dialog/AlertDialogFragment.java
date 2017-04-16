package ph.edu.mobapde.meditake.meditake.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by DELL-PC on 23/01/2017.
 */

public class AlertDialogFragment extends DialogFragment {

    String title;
    String message;
    String positiveButton;
    String negativeButton;
    private OnDialogClickListener mOnDialogClickListener;

    public AlertDialogFragment(){

    }

    @SuppressLint("ValidFragment")
    public AlertDialogFragment(String title, String message, String positiveButton, String negativeButton) {
        this.title = title;
        this.message = message;
        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return new android.app.AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mOnDialogClickListener != null) {
                            mOnDialogClickListener.onPositiveSelected();
                        }}

                })
                .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mOnDialogClickListener != null){
                            mOnDialogClickListener.onNegativeSelected();
                        }
                    }})
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        int[] attrs = {android.R.attr.colorAccent};
        TypedArray typedArray = getActivity().obtainStyledAttributes(attrs);
        ((android.app.AlertDialog) getDialog()).getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(typedArray.getColor(0, Color.BLACK));
        ((android.app.AlertDialog) getDialog()).getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(typedArray.getColor(0, Color.BLACK));
    }

    public void setOnDialogClickListener(OnDialogClickListener mOnDialogClickListener) {
        this.mOnDialogClickListener = mOnDialogClickListener;
    }

    public OnDialogClickListener getOnDialogClickListener() {
        return this.mOnDialogClickListener;
    }


    public interface OnDialogClickListener{
        void onPositiveSelected();
        void onNegativeSelected();
    }
}
