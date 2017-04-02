package ph.edu.mobapde.meditake.meditake.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.util.MedicineInstantiatorUtil;

/**
 * Created by Winfred Villaluna on 4/2/2017.
 */

public class AddScheduleMedicineAdapter extends CursorRecyclerViewAdapter<AddScheduleMedicineAdapter.MedicineSelectionViewHolder>{

    Context contextHolder;
    RecyclerView mRecyclerView;

    public AddScheduleMedicineAdapter(Context context, Cursor cursor, String column) {
        super(context, cursor, column);
        this.contextHolder = context;
        setHasStableIds(true);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }


    @Override
    public void onBindViewHolder(AddScheduleMedicineAdapter.MedicineSelectionViewHolder viewHolder, Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Medicine.COLUMN_ID));

        if(id != -1){
            Medicine med = MedicineInstantiatorUtil.createBeanFromCursor(cursor);
            viewHolder.ivMedicineType.setImageResource(med.getIcon());
            viewHolder.tvMedicineName.setText(med.getName());
            viewHolder.parentView.setTag(viewHolder.cbSelected);
            viewHolder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox value = (CheckBox) v.getTag();
                    value.setChecked(!value.isChecked());
                }
            });

        }
    }

    @Override
    public AddScheduleMedicineAdapter.MedicineSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_medicine_selection, parent, false);
        return new MedicineSelectionViewHolder(v);
    }

    public class MedicineSelectionViewHolder extends RecyclerView.ViewHolder{
        View parentView;

        @BindView(R.id.iv_selection_medicine_type)
        ImageView ivMedicineType;
        @BindView(R.id.tv_selection_medicine_name)
        TextView tvMedicineName;
        @BindView(R.id.cb_selection_medicine_selected)
        CheckBox cbSelected;

        public MedicineSelectionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.parentView = itemView;

        }
    }
}
