package ph.edu.mobapde.meditake.meditake.adapter.recyclerview;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.util.instantiator.MedicineInstantiatorUtil;

/**
 * Created by Winfred Villaluna on 4/2/2017.
 */

public class ViewScheduleMedicineSelectionAdapter extends CursorRecyclerViewAdapter<ViewScheduleMedicineSelectionAdapter.ViewScheduleMedicineSelectionViewHolder>{

    Context contextHolder;
    RecyclerView mRecyclerView;

    ArrayList<Integer> checkedIds;

    ArrayList<Integer> medicineDrawable;
    ArrayList<Integer> medicineBackground;

    private OnViewScheduleMedicineSelectionClickListener onViewScheduleMedicineSelectionClickListener;

    public ViewScheduleMedicineSelectionAdapter(Context context, Cursor cursor, String column, ArrayList<Integer> idsToRemove) {
        super(context, cursor, column);
        this.contextHolder = context;
        setHasStableIds(true);
        checkedIds = idsToRemove;

        medicineDrawable = new ArrayList<>();
        medicineDrawable.add(R.drawable.pill_capsule_white);
        medicineDrawable.add(R.drawable.medicine_bottle_white);
        medicineDrawable.add(R.drawable.aspirins_white);

        medicineBackground = new ArrayList<>();
        medicineBackground.add(R.drawable.medicine_capsule_background);
        medicineBackground.add(R.drawable.medicine_syrup_background);
        medicineBackground.add(R.drawable.medicine_tablet_background);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }


    @Override
    public void onBindViewHolder(ViewScheduleMedicineSelectionViewHolder viewHolder, Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Medicine.COLUMN_ID));

        if(id != -1){
            Medicine med = MedicineInstantiatorUtil.createBeanFromCursor(cursor);

            int selected = MedicineInstantiatorUtil.getMedicineInstanceOf(med);
            viewHolder.ivMedicineBgColor.setImageResource(medicineBackground.get(selected));
            viewHolder.ivMedicineDrawable.setImageResource(medicineDrawable.get(selected));

            viewHolder.tvMedicineName.setText(med.getName());
            viewHolder.linHolder.setTag(viewHolder.cbSelected);
            viewHolder.linHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox value = (CheckBox) v.getTag();
                    value.setChecked(!value.isChecked());
                }
            });

            viewHolder.cbSelected.setTag(med);
            viewHolder.cbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(onViewScheduleMedicineSelectionClickListener != null){
                        Medicine value = (Medicine) buttonView.getTag();
                        if(isChecked){
                            onViewScheduleMedicineSelectionClickListener.onItemCheck(value);
                        }else{
                            onViewScheduleMedicineSelectionClickListener.onItemUncheck(value);
                        }
                    }
                }
            });
            viewHolder.cbSelected.setChecked(checkedIds.contains(Integer.valueOf(id)));
        }
    }

    @Override
    public ViewScheduleMedicineSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_medicine_selection, parent, false);
        return new ViewScheduleMedicineSelectionViewHolder(v);
    }

    public class ViewScheduleMedicineSelectionViewHolder extends RecyclerView.ViewHolder{
        View parentView;

        @BindView(R.id.lin_holder)
        LinearLayout linHolder;

        @BindView(R.id.iv_medicine_background_color)
        ImageView ivMedicineBgColor;
        @BindView(R.id.iv_medicine_drawable)
        ImageView ivMedicineDrawable;

        @BindView(R.id.tv_selection_medicine_name)
        TextView tvMedicineName;
        @BindView(R.id.cb_selection_medicine_selected)
        CheckBox cbSelected;

        public ViewScheduleMedicineSelectionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.parentView = itemView;

        }
    }

    public OnViewScheduleMedicineSelectionClickListener getOnViewScheduleMedicineSelectionClickListener() {
        return onViewScheduleMedicineSelectionClickListener;
    }

    public void setOnViewScheduleMedicineSelectionClickListener(OnViewScheduleMedicineSelectionClickListener onOnViewScheduleMedicineSelectionClickListener) {
        this.onViewScheduleMedicineSelectionClickListener = onOnViewScheduleMedicineSelectionClickListener;
    }

    public interface OnViewScheduleMedicineSelectionClickListener{
        void onItemCheck(Medicine medicine);
        void onItemUncheck(Medicine medicine);
    }
}
