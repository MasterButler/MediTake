package ph.edu.mobapde.meditake.meditake.adapter.RecylerView;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;

/**
 * Created by DELL-PC on 04/04/2017.
 */

public class AlarmMedicineAdapter extends CursorRecyclerViewAdapter<MedicineViewHolder> {
    private ArrayList<Medicine> listMedicine;
    Context contextHolder;
    public AlarmMedicineAdapter(Context context, Cursor cursor, String column) {
        super(context, cursor, column);
        this.contextHolder = context;
        setHasStableIds(true);
    }

    @Override
    public void onBindViewHolder(final MedicineViewHolder viewHolder, Cursor cursor) {

    }
        @Override
    public MedicineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_medicine, parent, false);
        return new MedicineViewHolder(v);
    }
}
