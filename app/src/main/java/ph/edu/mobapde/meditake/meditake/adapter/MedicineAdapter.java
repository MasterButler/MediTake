package ph.edu.mobapde.meditake.meditake.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Capsule;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.util.MedicineInstantiatorUtil;

/**
 * Created by Winfred Villaluna on 3/12/2017.
 */

public class MedicineAdapter extends CursorRecyclerViewAdapter<MedicineAdapter.MedicineViewHolder> {

    public static final String NO_BRAND_NAME = "No Brand Name entered";
    public static final String NO_MEDICINE_FOR = "Not specified";
    public static final String NO_AMOUNT = "Out of stock";

    public static final int MODE_EDIT = 0;
    public static final int MODE_VIEW = 1;

    int expandedPositionId;
    int editingPositionId;
    RecyclerView mRecyclerView;

    public MedicineAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        expandedPositionId = -1;
        editingPositionId = -1;
        setHasStableIds(true);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(MedicineViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);

    }

    @Override
    public void onBindViewHolder(MedicineViewHolder viewHolder, Cursor cursor) {

        int id = cursor.getInt(cursor.getColumnIndex(Medicine.COLUMN_ID));
        String brandName  = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_BRAND_NAME));
        String genericName  = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_GENERIC_NAME));
        String medicineFor =  cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_FOR));
        double amount = cursor.getDouble(cursor.getColumnIndex(Medicine.COLUMN_AMOUNT));
        String medicineType = cursor.getString(cursor.getColumnIndex(Medicine.COLUMN_MEDICINE_TYPE));

        Log.d("ID", "EXPAND  ID: " + expandedPositionId);
        Log.d("ID", "EDITING ID: " + editingPositionId);

        if(id != -1){
            Medicine med = MedicineInstantiatorUtil.createMedicineInstanceFromString(medicineType);
            med.setBrandName(brandName);
            med.setGenericName(genericName);
            med.setMedicineFor(medicineFor);
            med.setAmount(amount);

            viewHolder.tvMedicineBrandName.setText(med.getBrandName().isEmpty() ? NO_BRAND_NAME : med.getBrandName());
            viewHolder.tvMedicineGenericName.setText(med.getGenericName());
            viewHolder.tvMedicineFor.setText(med.getMedicineFor().isEmpty() ? NO_MEDICINE_FOR : med.getMedicineFor());
            viewHolder.tvMedicineAmount.setText(med.getAmount() <= 0 ? NO_AMOUNT : amount + " " + med.getModifier() + " remaining");

            viewHolder.etMedicineGenericName.setText(med.getGenericName());
            viewHolder.etMedicineBrandName.setText(med.getBrandName());
            viewHolder.etMedicineFor.setText(med.getMedicineFor());
            viewHolder.etMedicineAmount.setText(med.getAmount() + "");
            viewHolder.tvMedicineAmountLabel.setText(med.getModifier() + " remaining");

            viewHolder.cvHolder.setCardBackgroundColor(med.getColor());

            //TODO add listeners here
            boolean isExpanded = id == expandedPositionId;
            viewHolder.parentView.setTag(id);
            viewHolder.linExpandedInformation.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            viewHolder.parentView.setActivated(isExpanded);
            viewHolder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        int value = (int)v.getTag();
                        onItemClickListener.onItemClick(value);
                    }
                }
            });

            boolean isEditing = id == editingPositionId;
            viewHolder.linEditMedicine.setTag(id);
            setMode(viewHolder, isEditing ? MODE_EDIT : MODE_VIEW);
            viewHolder.linEditMedicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        int value = (int)v.getTag();
                        onItemClickListener.onItemEditClick(value);
                    }
                }
            });

            boolean isDone = !(id == editingPositionId);
            setMode(viewHolder, isDone ? MODE_VIEW : MODE_EDIT);
            viewHolder.linCancelMedicine.setTag(id);
            viewHolder.linCancelMedicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        int value = (int)v.getTag();
                        if(value != -1){
                            onItemClickListener.onItemCancelClick(value);
                        }
                    }
                }
            });

            viewHolder.linSaveMedicine.setTag(R.string.MEDICINE_TYPE, med.getClass());
            viewHolder.linSaveMedicine.setTag(R.string.MEDICINE_ID, id);
            viewHolder.linSaveMedicine.setTag(R.string.MEDICINE_HOLDER, viewHolder);
            viewHolder.linSaveMedicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.wtf("action", "SAVING EDITS");
                    if(onItemClickListener != null){
                        MedicineViewHolder holder = (MedicineViewHolder) v.getTag(R.string.MEDICINE_HOLDER);
                        Class clazz = (Class)v.getTag(R.string.MEDICINE_TYPE);
                        int id = (int)v.getTag(R.string.MEDICINE_ID);

                        Medicine updatedMedicine = null;

                        updatedMedicine = MedicineInstantiatorUtil.createMedicineInstanceFromString(clazz.getSimpleName());
                        String updatedBrandName = holder.etMedicineBrandName.getText().toString();
                        String updatedGenericName = holder.etMedicineGenericName.getText().toString();
                        Double updatedAmount = Double.valueOf(holder.etMedicineAmount.getText().toString());
                        String updatedMedicineFor = holder.etMedicineFor.getText().toString();

                        updatedMedicine.setSqlId(id);
                        updatedMedicine.setBrandName(updatedBrandName);
                        updatedMedicine.setGenericName(updatedGenericName);
                        updatedMedicine.setAmount(updatedAmount);
                        updatedMedicine.setMedicineFor(updatedMedicineFor);

                        Log.wtf("before the checking", "BRAND NAME: " + updatedMedicine.getBrandName());
                        Log.wtf("before the checking", "GENERIC NM: " + updatedMedicine.getGenericName());
                        Log.wtf("before the checking", "MEDICN  FOR: " + updatedMedicine.getMedicineFor());
                        Log.wtf("before the checking", "MED AMOUNT: " + updatedMedicine.getAmount()+"");

                        onItemClickListener.onItemSaveClick(updatedMedicine);
                    }
                }
            });

            viewHolder.linDeleteMedicine.setTag(id);
            viewHolder.linDeleteMedicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        int value = (int)v.getTag();
                        if(value != -1){
                            onItemClickListener.onItemDeleteClick(value);
                        }
                    }
                }
            });


        }
    }

    public boolean isExpanded(int id){
        return (id == expandedPositionId) ? true : false;
    }

    public boolean isEditing(int id){
        return (id == editingPositionId) ? true: false;
    }

    public void setExpandedPositionId(int expandedPositionId) {
        this.expandedPositionId = expandedPositionId;
    }

    public void setEditingPositionId(int editingPositionId) {
        this.editingPositionId = editingPositionId;
    }

    public void setMode(MedicineViewHolder viewHolder, int mode){
        switch(mode){
            case MODE_EDIT: viewHolder.setToEditMode();
                break;
            case MODE_VIEW:
            default:        viewHolder.setToViewMode();
        }
    }

    @Override
    public MedicineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_medicine, parent, false);
        return new MedicineViewHolder(v);
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder{

        View parentView;
        @BindView(R.id.tv_medicine_generic_name)
        TextView tvMedicineGenericName;
        @BindView(R.id.tv_medicine_brand_name)
        TextView tvMedicineBrandName;
        @BindView(R.id.tv_medicine_for)
        TextView tvMedicineFor;
        @BindView(R.id.tv_medicine_amount)
        TextView tvMedicineAmount;

        @BindView(R.id.et_medicine_generic_name)
        EditText etMedicineGenericName;
        @BindView(R.id.et_medicine_brand_name)
        EditText etMedicineBrandName;
        @BindView(R.id.et_medicine_for)
        EditText etMedicineFor;
        @BindView(R.id.et_medicine_amount)
        EditText etMedicineAmount;
        @BindView(R.id.tv_medicine_amount_label)
        TextView tvMedicineAmountLabel;

        @BindView(R.id.lin_expanded_information)
        LinearLayout linExpandedInformation;
        @BindView(R.id.lin_edit_amount)
        LinearLayout linEditAmount;
        @BindView(R.id.lin_action_editing_medicine)
        LinearLayout linActionsEditing;
        @BindView(R.id.lin_action_viewing_medicine)
        LinearLayout linActionsViewinmg;

        @BindView(R.id.lin_edit_medicine)
        LinearLayout linEditMedicine;
        @BindView(R.id.lin_delete_medicine)
        LinearLayout linDeleteMedicine;

        @BindView(R.id.lin_save_medicine)
        LinearLayout linSaveMedicine;
        @BindView(R.id.lin_cancel_medicine)
        LinearLayout linCancelMedicine;

        @BindView(R.id.line_divider)
        View lineDivider;

        @BindView(R.id.cv_holder)
        CardView cvHolder;

        public MedicineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.parentView = itemView;

            setToViewMode();
        }

        public void setToViewMode(){
            Log.wtf("action", "NOW VIEWING");
            tvMedicineGenericName.setVisibility(View.VISIBLE);
            tvMedicineBrandName.setVisibility(View.VISIBLE);
            tvMedicineFor.setVisibility(View.VISIBLE);
            tvMedicineAmount.setVisibility(View.VISIBLE);
            lineDivider.setVisibility(View.VISIBLE);
            linActionsViewinmg.setVisibility(View.VISIBLE);

            etMedicineGenericName.setVisibility(View.GONE);
            etMedicineBrandName.setVisibility(View.GONE);
            etMedicineFor.setVisibility(View.GONE);
            linEditAmount.setVisibility(View.GONE);
            etMedicineAmount.setVisibility(View.GONE);
            tvMedicineAmountLabel.setVisibility(View.GONE);
            linActionsEditing.setVisibility(View.GONE);
        }

        public void setToEditMode(){
            Log.wtf("action", "NOW EDITING");
            tvMedicineGenericName.setVisibility(View.GONE);
            tvMedicineBrandName.setVisibility(View.GONE);
            tvMedicineFor.setVisibility(View.GONE);
            tvMedicineAmount.setVisibility(View.GONE);
            lineDivider.setVisibility(View.GONE);
            linActionsViewinmg.setVisibility(View.GONE);

            etMedicineGenericName.setVisibility(View.VISIBLE);
            etMedicineBrandName.setVisibility(View.VISIBLE);
            etMedicineFor.setVisibility(View.VISIBLE);
            linEditAmount.setVisibility(View.VISIBLE);
            etMedicineAmount.setVisibility(View.VISIBLE);
            tvMedicineAmountLabel.setVisibility(View.VISIBLE);
            linActionsEditing.setVisibility(View.VISIBLE);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void onItemClick(int id);
        public void onItemDeleteClick(int id);
        public void onItemEditClick(int id);
        public void onItemSaveClick(Medicine medicine);
        public void onItemCancelClick(int id);
    }
    /*
    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            mDataset[position] = charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
    */
}
