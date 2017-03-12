package ph.edu.mobapde.meditake.meditake.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.util.MedicineInstantiatorUtil;

/**
 * Created by Winfred Villaluna on 3/12/2017.
 */

public class MedicineAdapter extends CursorRecyclerViewAdapter<MedicineAdapter.MedicineViewHolder> {

    public MedicineAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(MedicineViewHolder viewHolder, Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Medicine.COLUMN_ID));
        String brandName  = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_BRAND_NAME));
        String genericName  = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_GENERIC_NAME));
        String medicineFor =  cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_FOR));
        double amount = cursor.getDouble(cursor.getColumnIndex(Medicine.COLUMN_AMOUNT));
        String medicineType = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_TYPE));


        Medicine med = MedicineInstantiatorUtil.createMedicineInstanceFromString(medicineType);
        Log.wtf("RECYLCER_VIEW", "RECEIVED " + medicineType);
        Log.wtf("RECYLCER_VIEW", "Adding " + med.getClass().getSimpleName());
        med.setBrandName(brandName);
        med.setGenericName(genericName);
        med.setMedicineFor(medicineFor);
        med.setAmount(amount);

        viewHolder.ivMedicineType.setImageResource(med.getIcon());
        viewHolder.tvMedicineName.setText(med.getName());
        viewHolder.tvMedicineFor.setText(med.getMedicineFor().isEmpty() ? "Not specified" : "For" + med.getMedicineFor());
        viewHolder.parentView.setTag(id);
        viewHolder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    int id = (int) v.getTag();
                    onItemClickListener.onItemClick(id);
                }
            }
        });
        //TODO add listener here

    }

    @Override
    public MedicineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_medicine, parent, false);
        return new MedicineViewHolder(v);
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder{

        View parentView;
        @BindView(R.id.iv_medicine_display_image)
        ImageView ivMedicineType;
        @BindView(R.id.tv_medicine_display_name)
        TextView tvMedicineName;
        @BindView(R.id.tv_medicine_for)
        TextView tvMedicineFor;

        public MedicineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.parentView = itemView;
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void onItemClick(int id);
    }
}
