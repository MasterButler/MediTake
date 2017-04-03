package ph.edu.mobapde.meditake.meditake.adapter.RecylerView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;

/**
 * Created by Winfred Villaluna on 3/18/2017.
 */

public class MedicineViewHolder extends RecyclerView.ViewHolder{

    View parentView;
    @BindView(R.id.lin_holder)
    LinearLayout linHolder;
    @BindView(R.id.tv_medicine_generic_name)
    TextView tvMedicineGenericName;
    @BindView(R.id.tv_medicine_brand_name)
    TextView tvMedicineBrandName;
    @BindView(R.id.tv_medicine_for)
    TextView tvMedicineFor;
    @BindView(R.id.tv_medicine_amount)
    TextView tvMedicineAmount;
    @BindView(R.id.tv_medicine_dosage)
    TextView tvMedicineDosage;
    @BindView(R.id.iv_medicine_type)
    ImageView ivMedicineType;


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
    @BindView(R.id.et_medicine_dosage)
    EditText etMedicineDosage;
    @BindView(R.id.tv_medicine_dosage_label)
    TextView tvMedicineDosageLabel;

    @BindView(R.id.lin_expanded_information)
    LinearLayout linExpandedInformation;
    @BindView(R.id.lin_expanded_edit_information)
    LinearLayout linExpandedEditInformation;
    @BindView(R.id.lin_edit_amount)
    LinearLayout linEditAmount;
    @BindView(R.id.lin_action_editing_medicine)
    LinearLayout linActionsEditing;
    @BindView(R.id.lin_action_viewing_medicine)
    LinearLayout linActionsViewing;

    @BindView(R.id.lin_edit_medicine)
    LinearLayout linEditMedicine;
    @BindView(R.id.lin_delete_medicine)
    LinearLayout linDeleteMedicine;

    @BindView(R.id.lin_save_medicine)
    LinearLayout linSaveMedicine;
    @BindView(R.id.lin_cancel_medicine)
    LinearLayout linCancelMedicine;

    @BindView(R.id.cv_holder)
    CardView cvHolder;

    @BindView(R.id.card_view_medicine)
    LinearLayout viewLayout;
    @BindView(R.id.card_edit_medicine)
    LinearLayout editLayout;
    public static final int VIEW_MODE = 1;
    public static final int EDIT_MODE = 2;

    public MedicineViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.parentView = itemView;

        setMode(VIEW_MODE);
    }

    public void setMode(int mode){
        int view_mode = mode == VIEW_MODE ? View.VISIBLE : View.GONE;
        int edit_mode = mode == EDIT_MODE ? View.VISIBLE : View.GONE;
        viewLayout.setVisibility(view_mode);
        editLayout.setVisibility(edit_mode);
    }
}
