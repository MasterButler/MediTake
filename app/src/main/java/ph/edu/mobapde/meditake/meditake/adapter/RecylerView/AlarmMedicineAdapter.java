package ph.edu.mobapde.meditake.meditake.adapter.RecylerView;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.util.instantiator.MedicineInstantiatorUtil;

/**
 * Created by DELL-PC on 04/04/2017.
 */

public class AlarmMedicineAdapter extends CursorRecyclerViewAdapter<AlarmMedicineAdapter.MedicineAlertViewHolder> {
    private ArrayList<Medicine> listMedicine;
    Context contextHolder;
    public AlarmMedicineAdapter(Context context, Cursor cursor, String column) {
        super(context, cursor, column);
        this.contextHolder = context;
        setHasStableIds(true);
    }

    @Override
    public void onBindViewHolder(MedicineAlertViewHolder viewHolder, Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Medicine.COLUMN_ID));
        if(id != -1){
            Medicine med = MedicineInstantiatorUtil.createBeanFromCursor(cursor);
            viewHolder.ivMedicineType.setImageResource(med.getIcon());
            viewHolder.tvMedicineName.setText(med.getName());
            viewHolder.tvMedicineDosage.setText(med.getDosage() + med.getModifier());
        }
    }
        @Override
    public MedicineAlertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_medicine_alarm, parent, false);
        return new MedicineAlertViewHolder(v);
    }

    public class MedicineAlertViewHolder extends RecyclerView.ViewHolder{

        View parentView;
        @BindView(R.id.iv_medicine_alarm_type)
        ImageView ivMedicineType;
        @BindView(R.id.tv_medicine_alarm_name)
        TextView tvMedicineName;
        @BindView(R.id.tv_medicine_alarm_dosage)
        TextView tvMedicineDosage;

        public MedicineAlertViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parentView = itemView;
        }
    }
}
