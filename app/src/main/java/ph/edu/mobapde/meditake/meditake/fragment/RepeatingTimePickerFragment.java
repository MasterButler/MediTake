package ph.edu.mobapde.meditake.meditake.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RepeatingTimePickerFragment.OnRepeatingTimePickerFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RepeatingTimePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepeatingTimePickerFragment extends Fragment {

    @BindView(R.id.lin_save_repeat_time_picker)
    LinearLayout linSave;
    @BindView(R.id.lin_cancel_repeat_time_picker)
    LinearLayout linCancel;

    @BindView(R.id.number_picker_hours)
    NumberPicker npHour;
    @BindView(R.id.number_picker_minutes)
    NumberPicker npMinutes;

    private TextView tvRepeat;

    private OnRepeatingTimePickerFragmentInteractionListener onRepeatingTimePickerFragmentInteractionListener;

    public RepeatingTimePickerFragment() {
    }

    public static RepeatingTimePickerFragment newInstance() {
        RepeatingTimePickerFragment fragment = new RepeatingTimePickerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void attachTextView(TextView tvRepeat){
        this.tvRepeat = tvRepeat;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_repeating_time_picker, container, false);
        ButterKnife.bind(this, v);

        npHour.setMinValue(0);
        npHour.setMaxValue(168);
        npMinutes.setMinValue(0);
        npMinutes.setMaxValue(59);

        if(tvRepeat != null){
            int hourValue = 0;
            int minuteValue = 1;
            int[] timeValues;
            if(!tvRepeat.getText().toString().equals(DateUtil.REPEATING_TIME_NOT_SET)){
                timeValues = DateUtil.parseFromTimePicker(tvRepeat.getText().toString());

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

        linCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onRepeatingTimePickerFragmentInteractionListener != null){
                    onRepeatingTimePickerFragmentInteractionListener.onRepeatingTimePickerFragmentCancel();
                }
            }
        });

        linSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onRepeatingTimePickerFragmentInteractionListener != null){
                    String output = DateUtil.parseToTimePickerDisplay(npHour.getValue(), npMinutes.getValue());
                    tvRepeat.setText(output);
                    onRepeatingTimePickerFragmentInteractionListener.onRepeatingTimePickerFragmentSave();
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRepeatingTimePickerFragmentInteractionListener) {
            onRepeatingTimePickerFragmentInteractionListener = (OnRepeatingTimePickerFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onRepeatingTimePickerFragmentInteractionListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRepeatingTimePickerFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentSave(Schedule schedule);
        void onRepeatingTimePickerFragmentCancel();
        void onRepeatingTimePickerFragmentSave();
    }
}
