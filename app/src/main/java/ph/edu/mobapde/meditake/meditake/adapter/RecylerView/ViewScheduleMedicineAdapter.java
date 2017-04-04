package ph.edu.mobapde.meditake.meditake.adapter.RecylerView;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;

/**
 * Created by Winfred Villaluna on 4/4/2017.
 */

public class ViewScheduleMedicineAdapter extends CursorRecyclerViewAdapter{

    private Context contextHolder;
    private RecyclerView mRecyclerView;

    public ViewScheduleMedicineAdapter(Context context, Cursor cursor, String column) {
        super(context, cursor, column);
        this.contextHolder = context;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
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
}
