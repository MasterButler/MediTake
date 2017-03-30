package ph.edu.mobapde.meditake.meditake.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.listener.OnMedicineClickListener;
import ph.edu.mobapde.meditake.meditake.util.MedicineInstantiatorUtil;

/**
 * Created by Winfred Villaluna on 3/12/2017.
 */

public class MedicineAdapter extends CursorRecyclerViewAdapter<MedicineViewHolder> {

    public static final String NO_BRAND_NAME = "No Brand Name entered";
    public static final String NO_MEDICINE_FOR = "Not specified";
    public static final String NO_AMOUNT = "Out of stock";
    public static final String NO_DOSAGE = "No default dosage entered";

    public static final int MODE_EDIT = 0;
    public static final int MODE_VIEW = 1;

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

            String displayAmount = (Math.ceil(med.getAmount()) == Math.floor(med.getAmount())) ? String.valueOf(med.getAmount()).split("\\.")[0] +"" : med.getAmount()+"";

            viewHolder.tvMedicineBrandName.setText(med.getBrandName().isEmpty() ? NO_BRAND_NAME : med.getBrandName());
            viewHolder.tvMedicineGenericName.setText(med.getGenericName());
            viewHolder.tvMedicineFor.setText(med.getMedicineFor().isEmpty() ? NO_MEDICINE_FOR : med.getMedicineFor());
            viewHolder.tvMedicineAmount.setText(med.getAmount() <= 0 ? NO_AMOUNT : " " + displayAmount + med.getModifier() + " remaining");
            viewHolder.ivMedicineType.setImageResource(med.getIcon());
            viewHolder.tvMedicineDosage.setText(med.getDosage() <= 0 ? NO_DOSAGE : " " + med.getDosage() + " " + med.getModifier() + " per dosage");

            viewHolder.etMedicineGenericName.setText(med.getGenericName());
            viewHolder.etMedicineBrandName.setText(med.getBrandName());
            viewHolder.etMedicineFor.setText(med.getMedicineFor());
            viewHolder.etMedicineAmount.setText(med.getAmount() + "");
            viewHolder.tvMedicineAmountLabel.setText(med.getModifier() + " remaining");
            viewHolder.etMedicineDosage.setText(med.getDosage() + "");
            viewHolder.tvMedicineDosageLabel.setText(med.getModifier() + " per dosage");


            boolean isExpanded = id == expandedPositionId;
            viewHolder.cvHolder.setTag(id);
            viewHolder.linExpandedInformation.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            viewHolder.cvHolder.setActivated(isExpanded);
            viewHolder.cvHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onMedicineClickListener != null){
                        int value = (int)v.getTag();
                        onMedicineClickListener.onItemClick(value);
                    }
                }
            });

            boolean isEditing = id == editingPositionId;
            viewHolder.linEditMedicine.setTag(id);
            setMode(viewHolder, isEditing ? MedicineViewHolder.EDIT_MODE : MedicineViewHolder.VIEW_MODE);
            viewHolder.linEditMedicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onMedicineClickListener != null){
                        int value = (int)v.getTag();
                        onMedicineClickListener.onItemEditClick(value);
                    }
                }
            });

            boolean isDone = !(id == editingPositionId);
            setMode(viewHolder, isDone ? MedicineViewHolder.VIEW_MODE : MedicineViewHolder.EDIT_MODE);
            viewHolder.linCancelMedicine.setTag(id);
            viewHolder.linCancelMedicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onMedicineClickListener != null){
                        int value = (int)v.getTag();
                        if(value != -1){
                            onMedicineClickListener.onItemCancelClick(value);
                        }
                    }
                }
            });

            viewHolder.linSaveMedicine.setTag(R.string.MEDICINE_TYPE, med.getClass());
            viewHolder.linSaveMedicine.setTag(R.string.MEDICINE_ID, id);
            viewHolder.linSaveMedicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.wtf("action", "SAVING EDITS");
                    if(onMedicineClickListener != null){
                        boolean isGenericNameEmpty = viewHolder.etMedicineGenericName.getText().toString().trim().isEmpty();
                        boolean isAmountEmpty = viewHolder.etMedicineAmount.getText().toString().trim().isEmpty();

                        if(isGenericNameEmpty || isAmountEmpty){
                            viewHolder.etMedicineGenericName.setError(isGenericNameEmpty ? "Generic name must not be empty" : null);
                            viewHolder.etMedicineAmount.setError(isAmountEmpty ? "Amount must not be empty" : null);
                        }else {
                            Class clazz = (Class) v.getTag(R.string.MEDICINE_TYPE);
                            int id = (int) v.getTag(R.string.MEDICINE_ID);

                            Medicine updatedMedicine = null;

                            updatedMedicine = MedicineInstantiatorUtil.createMedicineInstanceFromString(clazz.getSimpleName());
                            String updatedBrandName = viewHolder.etMedicineBrandName.getText().toString();
                            String updatedGenericName = viewHolder.etMedicineGenericName.getText().toString();
                            Double updatedAmount = Double.valueOf(viewHolder.etMedicineAmount.getText().toString());
                            String updatedMedicineFor = viewHolder.etMedicineFor.getText().toString();
                            int updatedDosage = Integer.valueOf(viewHolder.etMedicineDosage.getText().toString());

                            updatedMedicine.setSqlId(id);
                            updatedMedicine.setBrandName(updatedBrandName);
                            updatedMedicine.setGenericName(updatedGenericName);
                            updatedMedicine.setAmount(updatedAmount);
                            updatedMedicine.setMedicineFor(updatedMedicineFor);
                            updatedMedicine.setDosage(updatedDosage);

                            Log.wtf("before the checking", "BRAND NAME: " + updatedMedicine.getBrandName());
                            Log.wtf("before the checking", "GENERIC NM: " + updatedMedicine.getGenericName());
                            Log.wtf("before the checking", "MEDICN  FOR: " + updatedMedicine.getMedicineFor());
                            Log.wtf("before the checking", "MED AMOUNT: " + updatedMedicine.getAmount() + "");
                            Log.wtf("before the checking", "MED DOSAGE: " + updatedMedicine.getDosage() + "");

                            onMedicineClickListener.onItemSaveClick(updatedMedicine);
                        }
                    }
                }
            });

            viewHolder.linDeleteMedicine.setTag(id);
            viewHolder.linDeleteMedicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onMedicineClickListener != null){
                        int value = (int)v.getTag();
                        if(value != -1){
                            onMedicineClickListener.onItemDeleteClick(value);
                        }
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

    public void setMode(MedicineViewHolder viewHolder, int mode){
        viewHolder.setMode(mode);
    }

    public void setOnMedicineClickListener(OnMedicineClickListener onMedicineClickListener) {
        this.onMedicineClickListener = onMedicineClickListener;
    }

}
