package ph.edu.mobapde.meditake.meditake.adapter.RecylerView;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.util.instantiator.MedicineInstantiatorUtil;

/**
 * Created by Winfred Villaluna on 4/4/2017.
 */

public class ViewScheduleMedicineAdapter extends CursorRecyclerViewAdapter<ViewScheduleMedicineAdapter.ScheduleMedicineViewHolder>{

    private Context contextHolder;
    private RecyclerView mRecyclerView;

    private OnViewScheduleMedicineClickListener onViewScheduleMedicineClickListener;

    public ViewScheduleMedicineAdapter(Context context, Cursor cursor, String column) {
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
    public void onBindViewHolder(ScheduleMedicineViewHolder viewHolder, Cursor cursor) {
        Log.wtf("IN ON BIND", "HI");
        int id = cursor.getInt(cursor.getColumnIndex(Medicine.COLUMN_ID));
        Log.wtf("VIEWSCHEDULEMEDICINEADAPTER", "GOT AN ID WITH VALUE " + id);
        if(id != -1){
            Medicine medicine = MedicineInstantiatorUtil.createBeanFromCursor(cursor);
            viewHolder.tvName.setText(medicine.getName());
            viewHolder.tvDosage.setText(String.valueOf(medicine.getDosage() + medicine.getModifier()));
            viewHolder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewScheduleMedicineClickListener.onItemClick();
                }
            });
        }
    }

    @Override
    public ScheduleMedicineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(contextHolder)
                .inflate(R.layout.row_medicine_view, parent, false);
        return new ScheduleMedicineViewHolder(v);
    }

    public class ScheduleMedicineViewHolder extends RecyclerView.ViewHolder {

        View parentView;

        @BindView(R.id.tv_view_medicine_name)
        TextView tvName;
        @BindView(R.id.tv_view_medicine_dosage)
        TextView tvDosage;

        public ScheduleMedicineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parentView = itemView;
        }
    }

    public OnViewScheduleMedicineClickListener getOnViewScheduleMedicineClickListener() {
        return onViewScheduleMedicineClickListener;
    }

    public void setOnViewScheduleMedicineClickListener(OnViewScheduleMedicineClickListener onViewScheduleMedicineClickListener) {
        this.onViewScheduleMedicineClickListener = onViewScheduleMedicineClickListener;
    }

    public interface OnViewScheduleMedicineClickListener{
        void onItemClick();
    }
}
