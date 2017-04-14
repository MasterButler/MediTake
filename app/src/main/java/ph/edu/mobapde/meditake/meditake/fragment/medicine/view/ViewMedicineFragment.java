package ph.edu.mobapde.meditake.meditake.fragment.medicine.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Medicine;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;

public class ViewMedicineFragment extends Fragment {

    private static final String ARG_MEDICINE = "medicine";
    OnViewMedicineFragmentInteractionListener onViewMedicineFragmentInteractionListener;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int medId;

    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.black_overlay)
    View bg;
    @BindView(R.id.container)
    ViewPager mViewPager;
//    @BindView(R.id.tabs)
//    TabLayout tabLayout;

    private Bitmap b = null;

    public ViewMedicineFragment() {
    }

    public static ViewMedicineFragment newInstance(int id) {
        ViewMedicineFragment fragment = new ViewMedicineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MEDICINE, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            medId = getArguments().getInt(ARG_MEDICINE);
            Log.wtf("VIEWSCHED", "MEDICINE ID IS " + medId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_medicine, container, false);
        ButterKnife.bind(this, v);

        setHasOptionsMenu(true);

        mSectionsPagerAdapter = new ViewMedicineFragment.SectionsPagerAdapter(getChildFragmentManager());
        mSectionsPagerAdapter.add(ViewMedicineDetailsFragment.newInstance(1, medId));
//        mSectionsPagerAdapter.add(AddScheduleMedicineFragment.newInstance(2));

        mViewPager.setAdapter(mSectionsPagerAdapter);
        int[] attrs = {android.R.attr.colorBackground};
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs);
        mViewPager.setBackgroundColor(typedArray.getColor(0, Color.WHITE));
//        tabLayout.setupWithViewPager(mViewPager);

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewMedicineDetailsFragment viewMedicineDetailsFragment = (ViewMedicineDetailsFragment) mSectionsPagerAdapter.getFragment(0);
                ViewMedicineDetailsFragment viewScheduleDetailsFragment = (ViewMedicineDetailsFragment) ((SectionsPagerAdapter)mViewPager.getAdapter()).getFragment(0);
                onViewMedicineFragmentInteractionListener.onViewMedicineBackgroundClick(viewScheduleDetailsFragment.updateMedicineFromUserInput(viewMedicineDetailsFragment.getMedicine()));
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        for (Fragment fragment : getFragmentManager().getFragments()) {
            if (fragment instanceof ViewMedicineDetailsFragment) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(fragment);
                ft.attach(fragment);
                ft.commit();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
        }

        public void add(Fragment fragment){
            this.fragments.add(fragment);
        }

        public Fragment getFragment(int position){
            return fragments.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Information";
            }
            return null;
        }
    }

    public interface OnViewMedicineFragmentInteractionListener {
        void onViewMedicineBackgroundClick(Medicine medicine);
    }

    public OnViewMedicineFragmentInteractionListener getOnViewMedicineFragmentInteractionListener() {
        return onViewMedicineFragmentInteractionListener;
    }

    public void setOnViewScheduleFragmentInteractionListener(OnViewMedicineFragmentInteractionListener onViewMedicineFragmentInteractionListener) {
        this.onViewMedicineFragmentInteractionListener = onViewMedicineFragmentInteractionListener;
    }
}
