package ph.edu.mobapde.meditake.meditake.adapter;

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
    @BindView(R.id.tv_medicine_generic_name)
    TextView tvMedicineGenericName;
    @BindView(R.id.tv_medicine_brand_name)
    TextView tvMedicineBrandName;
    @BindView(R.id.tv_medicine_for)
    TextView tvMedicineFor;
    @BindView(R.id.tv_medicine_amount)
    TextView tvMedicineAmount;
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
        ivMedicineType.setVisibility(View.VISIBLE);

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
        ivMedicineType.setVisibility(View.GONE);

        etMedicineGenericName.setVisibility(View.VISIBLE);
        etMedicineBrandName.setVisibility(View.VISIBLE);
        etMedicineFor.setVisibility(View.VISIBLE);
        linEditAmount.setVisibility(View.VISIBLE);
        etMedicineAmount.setVisibility(View.VISIBLE);
        tvMedicineAmountLabel.setVisibility(View.VISIBLE);
        linActionsEditing.setVisibility(View.VISIBLE);
    }
}
