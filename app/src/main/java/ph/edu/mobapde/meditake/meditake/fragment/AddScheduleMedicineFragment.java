package ph.edu.mobapde.meditake.meditake.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.AddScheduleMedicineAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.MedicineList;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.beans.MedicinePlanList;
import ph.edu.mobapde.meditake.meditake.util.DateUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicinePlanInstantiatorUtil;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;

public class AddScheduleMedicineFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int sectionNumber;

    @BindView(R.id.rv_schedule_medicine_selection)
    RecyclerView rvMedicineSelection;

    @BindView(R.id.lin_back_schedule)
    LinearLayout linBackSchedule;
    @BindView(R.id.lin_save_schedule)
    LinearLayout linSaveSchedule;

    Context contextHolder;

    MedicineList medicineList;
    AddScheduleMedicineAdapter addScheduleMedicineAdapter;
    MedicineUtil medicineUtil;

    private OnAddScheduleMedicineFragmentInteractionListener onAddScheduleMedicineFragmentInteractionListener;

    public AddScheduleMedicineFragment() {
        super();
    }

    public void initializeAdapter(){
        addScheduleMedicineAdapter = new AddScheduleMedicineAdapter(contextHolder, medicineUtil.getAllMedicine(), Medicine.COLUMN_ID);
        addScheduleMedicineAdapter.setOnAddScheduleMedicineClickListener(new AddScheduleMedicineAdapter.OnAddScheduleMedicineClickListener() {
            @Override
            public void onItemCheck(Medicine medicine) {
                medicineList.add(medicine);
            }

            @Override
            public void onItemUncheck(Medicine medicine) {
                medicineList.remove(medicine);
            }
        });
        Log.wtf("ADDSCHEDULEMEDICINEFRAG", "ADAPTER NULL: " + (addScheduleMedicineAdapter == null));
    }

    public void initializeContents(){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(contextHolder);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        rvMedicineSelection.setAdapter(addScheduleMedicineAdapter);
        rvMedicineSelection.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextHolder = this.getActivity();

        medicineUtil = new MedicineUtil(contextHolder);

        if (getArguments() != null) {
            sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    public static AddScheduleMedicineFragment newInstance(int sectionNumber) {
        AddScheduleMedicineFragment fragment = new AddScheduleMedicineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_schedule_medicine, container, false);
        ButterKnife.bind(this, v);

        medicineList = new MedicineList();
        initializeAdapter();
        initializeContents();

        linBackSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAddScheduleMedicineFragmentInteractionListener != null) {
                    ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.container);
                    viewPager.setCurrentItem(0);
                }
            }
        });

        linSaveSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAddScheduleMedicineFragmentInteractionListener != null) {
                    ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.container);
                    AddScheduleDetailsFragment frag = (AddScheduleDetailsFragment) ((AddScheduleFragment.SectionsPagerAdapter) viewPager.getAdapter()).getFragment(0);

                    MedicinePlanList medicinePlanList = MedicinePlanInstantiatorUtil.convertMedicineToMedicinePlan(medicineList);
                    Schedule sched = frag.constructScheduleFromUserInput();

                    onAddScheduleMedicineFragmentInteractionListener.onAddScheduleMedicineFragmentSave(sched);
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddScheduleMedicineFragmentInteractionListener) {
            onAddScheduleMedicineFragmentInteractionListener = (OnAddScheduleMedicineFragmentInteractionListener) context;
            contextHolder = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onAddScheduleMedicineFragmentInteractionListener = null;
    }

    public interface OnAddScheduleMedicineFragmentInteractionListener {
        void onAddScheduleMedicineFragmentSave(Schedule schedule);
    }
}
