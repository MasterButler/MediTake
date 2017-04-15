package ph.edu.mobapde.meditake.meditake.adapter.recyclerview;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.listener.OnMedicineClickListener;
import ph.edu.mobapde.meditake.meditake.util.instantiator.MedicineInstantiatorUtil;

/**
 * Created by Winfred Villaluna on 3/12/2017.
 */

public class MedicineAdapter extends CursorRecyclerViewAdapter<MedicineAdapter.MedicineViewHolder> {

    public static final String NO_BRAND_NAME = "No Brand Name entered";
    public static final String NO_MEDICINE_FOR = "Not specified";
    public static final String NO_AMOUNT = "Out of stock";
    public static final String NO_DOSAGE = "No default dosage entered";

    public static final int MODE_EDIT = 0;
    public static final int MODE_VIEW = 1;


    ArrayList<Integer> medicineDrawable;
    ArrayList<Integer> medicineBackground;

    int expandedPositionId;
    int editingPositionId;
    RecyclerView mRecyclerView;
    Context contextHolder;

    private OnMedicineClickListener onMedicineClickListener;

    public MedicineAdapter(Context context, Cursor cursor, String column) {
        super(context, cursor, column);
        this.contextHolder = context;
        expandedPositionId = -1;
        editingPositionId = -1;
        setHasStableIds(true);

        medicineDrawable = new ArrayList<>();
        medicineDrawable.add(R.drawable.capsule_white_large);
        medicineDrawable.add(R.drawable.syrup_white_large);
        medicineDrawable.add(R.drawable.tablet_white_large);

        medicineBackground = new ArrayList<>();
        medicineBackground.add(context.getResources().getColor(R.color.medicine_capsule_selection_light));
        medicineBackground.add(context.getResources().getColor(R.color.medicine_syrup_selection_light));
        medicineBackground.add(context.getResources().getColor(R.color.medicine_tablet_selection_light));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(final MedicineViewHolder viewHolder, Cursor cursor) {

        int id = cursor.getInt(cursor.getColumnIndex(Medicine.COLUMN_ID));

        Log.d("ID", "EXPAND  ID: " + expandedPositionId);
        Log.d("ID", "EDITING ID: " + editingPositionId);
        Log.d("ID", "MEDICINEID: " + id + "\n");

        if(id != -1){
            Medicine med = MedicineInstantiatorUtil.createBeanFromCursor(cursor);

            viewHolder.tvMedicineBrandName.setText(med.getBrandName().isEmpty() ? NO_BRAND_NAME : med.getBrandName());
            viewHolder.tvMedicineGenericName.setText(med.getGenericName());
            viewHolder.tvMedicineFor.setText(med.getMedicineFor().isEmpty() ? NO_MEDICINE_FOR : med.getMedicineFor());

            int selected = MedicineInstantiatorUtil.getMedicineInstanceOf(med);
            viewHolder.rvMedicineBgColor.setBackgroundColor(medicineBackground.get(selected));
            viewHolder.ivMedicineDrawable.setImageResource(medicineDrawable.get(selected));

            viewHolder.cvHolder.setTag(id);
            viewHolder.cvHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onMedicineClickListener != null){
                        int value = (int)v.getTag();
                        onMedicineClickListener.onItemClick(value);
                    }
                }
            });
        }
    }

    @Override
    public MedicineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_medicine, parent, false);
        return new MedicineViewHolder(v);
    }

    public boolean isExpanded(int id){
        return (id == expandedPositionId) ? true : false;
    }

    public boolean isExpanded(){
        return expandedPositionId != -1 ? true : false;
    }

    public int getExpandedPositionId() {
        return expandedPositionId;
    }

    public void setExpandedPositionId(int expandedPositionId) {
        this.expandedPositionId = expandedPositionId;
    }

    public boolean isEditing(int id){
        return (id == editingPositionId) ? true : false;
    }

    public boolean isEditing(){
        return (editingPositionId != -1) ? true : false;
    }

    public int getEditingPositionId() {
        return editingPositionId;
    }

    public void setEditingPositionId(int editingPositionId) {
        this.editingPositionId = editingPositionId;
    }

    public void setOnMedicineClickListener(OnMedicineClickListener onMedicineClickListener) {
        this.onMedicineClickListener = onMedicineClickListener;
    }


    public class MedicineViewHolder extends RecyclerView.ViewHolder{

        View parentView;

        @BindView(R.id.rv_medicine_type_background)
        RelativeLayout rvMedicineBgColor;
        @BindView(R.id.iv_medicine_type_drawable)
        ImageView ivMedicineDrawable;

        @BindView(R.id.cv_holder)
        CardView cvHolder;
        @BindView(R.id.tv_medicine_generic_name)
        TextView tvMedicineGenericName;
        @BindView(R.id.tv_medicine_brand_name)
        TextView tvMedicineBrandName;
        @BindView(R.id.tv_medicine_for)
        TextView tvMedicineFor;

        public MedicineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.parentView = itemView;
        }
    }


}
