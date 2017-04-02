package ph.edu.mobapde.meditake.meditake.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.listener.OnMedicineListFragmentClickListener;
import ph.edu.mobapde.meditake.meditake.util.instantiator.MedicineInstantiatorUtil;

/**
 * Created by Winfred Villaluna on 3/20/2017.
 */

public class MedicineListFragmentAdapter extends CursorRecyclerViewAdapter<MedicineListFragmentAdapter.MedicineListFragmentViewHolder>{

    Context contextHolder;

    OnMedicineListFragmentClickListener onMedicineListFragmentClickListener;

    public MedicineListFragmentAdapter(Context context, Cursor cursor, String column) {
        super(context, cursor, column);
        this.contextHolder = context;
    }

    @Override
    public void onBindViewHolder(MedicineListFragmentViewHolder viewHolder, Cursor cursor) {

        int id = cursor.getInt(cursor.getColumnIndex(Medicine.COLUMN_ID));
        String brandName  = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_BRAND_NAME));
        String genericName  = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_GENERIC_NAME));
        String medicineFor =  cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_FOR));
        double amount = cursor.getDouble(cursor.getColumnIndex(Medicine.COLUMN_AMOUNT));
        String medicineType = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_TYPE));

        if(id != -1){
            Medicine med = MedicineInstantiatorUtil.createMedicineInstanceFromString(medicineType);
            med.setSqlId(id);
            med.setBrandName(brandName);
            med.setGenericName(genericName);
            med.setMedicineFor(medicineFor);
            med.setAmount(amount);

            viewHolder.tvMedicineName.setText(med.getName());
            viewHolder.ivMedicineType.setImageResource(med.getIcon());

            viewHolder.linMedicine.setTag(med);
            viewHolder.linMedicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("action", "CLICKING MEDICINE");
                    Medicine med = (Medicine) v.getTag();
                    onMedicineListFragmentClickListener.onItemClick(med);
                }
            });
        }
    }

    @Override
    public MedicineListFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spinner_row_medicine, parent, false);
        return new MedicineListFragmentViewHolder(v);
    }

    public class MedicineListFragmentViewHolder extends RecyclerView.ViewHolder{
        View parentView;
        @BindView(R.id.medicine_row)
        LinearLayout linMedicine;
        @BindView(R.id.iv_spinner_medicine_type)
        ImageView ivMedicineType;
        @BindView(R.id.tv_spinner_medicine_name)
        TextView tvMedicineName;

        public MedicineListFragmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.parentView = itemView;
        }
    }

    public OnMedicineListFragmentClickListener getOnMedicineListFragmentClickListener() {
        return onMedicineListFragmentClickListener;
    }

    public void setOnMedicineListFragmentClickListener(OnMedicineListFragmentClickListener onMedicineListFragmentClickListener) {
        this.onMedicineListFragmentClickListener = onMedicineListFragmentClickListener;
    }
}
