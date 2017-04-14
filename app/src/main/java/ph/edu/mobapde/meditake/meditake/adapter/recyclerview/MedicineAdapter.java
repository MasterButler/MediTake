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
        medicineDrawable.add(R.drawable.pill_capsule_white);
        medicineDrawable.add(R.drawable.medicine_bottle_white);
        medicineDrawable.add(R.drawable.aspirins_white);

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

//            String displayAmount = (Math.ceil(med.getAmount()) == Math.floor(med.getAmount())) ? String.valueOf(med.getAmount()).split("\\.")[0] +"" : med.getAmount()+"";
//            viewHolder.tvMedicineAmount.setText(med.getAmount() <= 0 ? NO_AMOUNT : " " + displayAmount + med.getModifier() + " remaining");
//            viewHolder.ivMedicineType.setImageResource(med.getIcon());
//            viewHolder.tvMedicineDosage.setText(med.getDosage() <= 0 ? NO_DOSAGE : " " + med.getDosage() + " " + med.getModifier() + " per dosage");
//
//            viewHolder.etMedicineGenericName.setText(med.getGenericName());
//            viewHolder.etMedicineBrandName.setText(med.getBrandName());
//            viewHolder.etMedicineFor.setText(med.getMedicineFor());
//            viewHolder.etMedicineAmount.setText(med.getAmount() + "");
//            viewHolder.tvMedicineAmountLabel.setText(med.getModifier() + " remaining");
//            viewHolder.etMedicineDosage.setText(med.getDosage() + "");
//            viewHolder.tvMedicineDosageLabel.setText(med.getModifier() + " per dosage");
//
//
//            final boolean isExpanded = id == expandedPositionId;
//            viewHolder.linExpandedInformation.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
//            viewHolder.cvHolder.setActivated(isExpanded);
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
//
//            boolean isEditing = id == editingPositionId;
//            viewHolder.linEditMedicine.setTag(id);
//            setMode(viewHolder, isEditing ? MedicineViewHolder.EDIT_MODE : MedicineViewHolder.VIEW_MODE);
//            viewHolder.linEditMedicine.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(onMedicineClickListener != null){
//                        int value = (int)v.getTag();
//                        onMedicineClickListener.onItemEditClick(value);
//                    }
//                }
//            });
//
//            boolean isDone = !(id == editingPositionId);
//            setMode(viewHolder, isDone ? MedicineViewHolder.VIEW_MODE : MedicineViewHolder.EDIT_MODE);
//            viewHolder.linCancelMedicine.setTag(id);
//            viewHolder.linCancelMedicine.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(onMedicineClickListener != null){
//                        int value = (int)v.getTag();
//                        if(value != -1){
//                            onMedicineClickListener.onItemCancelClick(value);
//                        }
//                    }
//                }
//            });
//
//            viewHolder.linSaveMedicine.setTag(R.string.MEDICINE_TYPE, med.getClass());
//            viewHolder.linSaveMedicine.setTag(R.string.MEDICINE_ID, id);
//            viewHolder.linSaveMedicine.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.wtf("action", "SAVING EDITS");
//                    if(onMedicineClickListener != null){
//                        boolean isGenericNameEmpty = viewHolder.etMedicineGenericName.getText().toString().trim().isEmpty();
//                        boolean isAmountEmpty = viewHolder.etMedicineAmount.getText().toString().trim().isEmpty();
//
//                        if(isGenericNameEmpty || isAmountEmpty){
//                            viewHolder.etMedicineGenericName.setError(isGenericNameEmpty ? "Generic name must not be empty" : null);
//                            viewHolder.etMedicineAmount.setError(isAmountEmpty ? "Amount must not be empty" : null);
//                        }else {
//                            Class clazz = (Class) v.getTag(R.string.MEDICINE_TYPE);
//                            int id = (int) v.getTag(R.string.MEDICINE_ID);
//
//                            Medicine updatedMedicine = null;
//
//                            updatedMedicine = MedicineInstantiatorUtil.createMedicineInstanceFromString(clazz.getSimpleName());
//                            String updatedBrandName = viewHolder.etMedicineBrandName.getText().toString();
//                            String updatedGenericName = viewHolder.etMedicineGenericName.getText().toString();
//                            long updatedAmount = Long.valueOf(viewHolder.etMedicineAmount.getText().toString());
//                            String updatedMedicineFor = viewHolder.etMedicineFor.getText().toString();
//                            int updatedDosage = Integer.valueOf(viewHolder.etMedicineDosage.getText().toString());
//
//                            updatedMedicine.setSqlId(id);
//                            updatedMedicine.setBrandName(updatedBrandName);
//                            updatedMedicine.setGenericName(updatedGenericName);
//                            updatedMedicine.setAmount(updatedAmount);
//                            updatedMedicine.setMedicineFor(updatedMedicineFor);
//                            updatedMedicine.setDosage(updatedDosage);
//
//                            Log.wtf("before the checking", "BRAND NAME: " + updatedMedicine.getBrandName());
//                            Log.wtf("before the checking", "GENERIC NM: " + updatedMedicine.getGenericName());
//                            Log.wtf("before the checking", "MEDICN  FOR: " + updatedMedicine.getMedicineFor());
//                            Log.wtf("before the checking", "MED AMOUNT: " + updatedMedicine.getAmount() + "");
//                            Log.wtf("before the checking", "MED DOSAGE: " + updatedMedicine.getDosage() + "");
//
//                            onMedicineClickListener.onItemSaveClick(updatedMedicine);
//                        }
//                    }
//                }
//            });
//
//            viewHolder.linDeleteMedicine.setTag(id);
//            viewHolder.linDeleteMedicine.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(onMedicineClickListener != null){
//                        int value = (int)v.getTag();
//                        if(value != -1){
//                            onMedicineClickListener.onItemDeleteClick(value);
//                        }
//                    }
//                }
//            });
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

//    public void setMode(MedicineViewHolder viewHolder, int mode){
//        viewHolder.setMode(mode);
//    }

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

//        @BindView(R.id.lin_holder)
//        LinearLayout linHolder;
//        @BindView(R.id.tv_medicine_amount)
//        TextView tvMedicineAmount;
//        @BindView(R.id.tv_medicine_dosage)
//        TextView tvMedicineDosage;
//        @BindView(R.id.iv_medicine_type)
//        ImageView ivMedicineType;
//
//
//        @BindView(R.id.et_medicine_generic_name)
//        EditText etMedicineGenericName;
//        @BindView(R.id.et_medicine_brand_name)
//        EditText etMedicineBrandName;
//        @BindView(R.id.et_medicine_for)
//        EditText etMedicineFor;
//        @BindView(R.id.et_medicine_amount)
//        EditText etMedicineAmount;
//        @BindView(R.id.tv_medicine_amount_label)
//        TextView tvMedicineAmountLabel;
//        @BindView(R.id.et_medicine_dosage)
//        EditText etMedicineDosage;
//        @BindView(R.id.tv_medicine_dosage_label)
//        TextView tvMedicineDosageLabel;
//
//        @BindView(R.id.lin_expanded_information)
//        LinearLayout linExpandedInformation;
//        @BindView(R.id.lin_expanded_edit_information)
//        LinearLayout linExpandedEditInformation;
//        @BindView(R.id.lin_edit_amount)
//        LinearLayout linEditAmount;
//        @BindView(R.id.lin_action_editing_medicine)
//        LinearLayout linActionsEditing;
//        @BindView(R.id.lin_action_viewing_medicine)
//        LinearLayout linActionsViewing;
//
//        @BindView(R.id.lin_edit_medicine)
//        LinearLayout linEditMedicine;
//        @BindView(R.id.lin_delete_medicine)
//        LinearLayout linDeleteMedicine;
//
//        @BindView(R.id.lin_save_medicine)
//        LinearLayout linSaveMedicine;
//        @BindView(R.id.lin_cancel_medicine)
//        LinearLayout linCancelMedicine;
//
//
//        @BindView(R.id.card_view_medicine)
//        LinearLayout viewLayout;
//        @BindView(R.id.card_edit_medicine)
//        LinearLayout editLayout;
//        public static final int VIEW_MODE = 1;
//        public static final int EDIT_MODE = 2;
//
//        private int originalHeight = 0;
//        private boolean isViewExpanded = false;


        public MedicineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.parentView = itemView;
//            itemView.setOnClickListener(this);
//
//            if (isViewExpanded == false) {
//                linExpandedInformation.setVisibility(View.GONE);
//                linExpandedInformation.setEnabled(false);
//            }
        }
//
//        public void setMode(int mode){
//            int view_mode = mode == VIEW_MODE ? View.VISIBLE : View.GONE;
//            int edit_mode = mode == EDIT_MODE ? View.VISIBLE : View.GONE;
//            viewLayout.setVisibility(view_mode);
//            viewLayout.setEnabled(view_mode == View.VISIBLE);
//            editLayout.setVisibility(edit_mode);
//            editLayout.setEnabled(edit_mode == View.VISIBLE);
//        }
    }


}
