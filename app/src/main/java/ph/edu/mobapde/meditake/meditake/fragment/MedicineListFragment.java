package ph.edu.mobapde.meditake.meditake.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.MedicineListFragmentAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.listener.OnMedicineListFragmentClickListener;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;

/**
 * Created by Winfred Villaluna on 3/20/2017.
 */


public class MedicineListFragment extends Fragment {

    @BindView(R.id.rv_medicine_list_fragment)
    RecyclerView rvMedicine;

    Context contextHolder;
    OnDataPass dataPasser;

    MedicineListFragmentAdapter medicineListFragmentAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (MedicineListFragment.OnDataPass) context;
        this.contextHolder = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("action", "INFLATION STARTING");
        View v = inflater.inflate(R.layout.fragment_medicine_list, null);
        ButterKnife.bind(this, v);

        medicineListFragmentAdapter = new MedicineListFragmentAdapter(contextHolder, new MedicineUtil(contextHolder).getAllMedicine());
        medicineListFragmentAdapter.setHasStableIds(true);
        Log.d("action", "CONTINUE INFLATION");
        medicineListFragmentAdapter.setOnMedicineListFragmentClickListener(new OnMedicineListFragmentClickListener() {
            @Override
            public void onItemClick(Medicine med) {
                Log.d("action", "RECIEVED MED FROM RECYCLER VIEW");
                dataPasser.onDataPass(med);
            }
        });


        Log.d("action", "CONTINUE INFLATION P2");
        rvMedicine.setAdapter(medicineListFragmentAdapter);
        rvMedicine.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.VERTICAL, true));

        Log.d("action", "END INFLATION ");
        return v;
    }

    public interface OnDataPass {
        void onDataPass(Medicine medicine);
    }

    public MedicineListFragment.OnDataPass getDataPasser() {
        return dataPasser;
    }

    public void setDataPasser(MedicineListFragment.OnDataPass dataPasser) {
        this.dataPasser = dataPasser;
    }
}