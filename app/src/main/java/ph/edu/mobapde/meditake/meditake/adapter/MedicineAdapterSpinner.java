package ph.edu.mobapde.meditake.meditake.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.util.MedicineInstantiatorUtil;

/**
 * Created by Winfred Villaluna on 3/19/2017.
 */

public class MedicineAdapterSpinner extends CursorAdapter {

    Context contextHolder;

    public MedicineAdapterSpinner(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.contextHolder = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spinner_row_medicine, parent, false);
        MedicineSpinnerViewHolder holder = new MedicineSpinnerViewHolder(v);

        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MedicineSpinnerViewHolder holder = (MedicineSpinnerViewHolder) view.getTag();

        int id = cursor.getInt(cursor.getColumnIndex(Medicine.COLUMN_ID));
        String brandName = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_BRAND_NAME));
        String genericName = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_GENERIC_NAME));
        String medicineFor = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_FOR));
        double amount = cursor.getDouble(cursor.getColumnIndex(Medicine.COLUMN_AMOUNT));
        String medicineType = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_TYPE));

        if(id != -1){
            Medicine med = MedicineInstantiatorUtil.createMedicineInstanceFromString(medicineType);
            med.setBrandName(brandName);
            med.setGenericName(genericName);
            med.setMedicineFor(medicineFor);
            med.setAmount(amount);

            holder.tvMedicineName.setText(med.getName());
            holder.ivMedicineType.setImageResource(med.getIcon());
        }
    }


    public class MedicineSpinnerViewHolder{
        @BindView(R.id.iv_spinner_medicine_type)
        ImageView ivMedicineType;
        @BindView(R.id.tv_spinner_medicine_name)
        TextView tvMedicineName;

        public MedicineSpinnerViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
