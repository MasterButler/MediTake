package ph.edu.mobapde.meditake.meditake.fragment.schedule.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;

public class ViewScheduleFragment extends Fragment {

    private static final String ARG_SCHED = "schedule";
    OnViewScheduleFragmentInteractionListener onViewScheduleFragmentInteractionListener;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Schedule schedule;

    @BindView(R.id.overlay)
    View bg;
    @BindView(R.id.container)
    ViewPager mViewPager;
//    @BindView(R.id.tabs)
//    TabLayout tabLayout;

    public ViewScheduleFragment() {
    }

    public static ViewScheduleFragment newInstance(Schedule schedule) {
        ViewScheduleFragment fragment = new ViewScheduleFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SCHED, schedule);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            schedule = getArguments().getParcelable(ARG_SCHED);
            Log.wtf("VIEWSCHED", "SCHEDULE IS " + schedule);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_schedule, container, false);
        ButterKnife.bind(this, v);

        setHasOptionsMenu(true);

        mSectionsPagerAdapter = new ViewScheduleFragment.SectionsPagerAdapter(getChildFragmentManager());
        mSectionsPagerAdapter.add(ViewScheduleDetailsFragment.newInstance(1, schedule));
//        mSectionsPagerAdapter.add(AddScheduleMedicineFragment.newInstance(2));

        mViewPager.setAdapter(mSectionsPagerAdapter);
        int[] attrs = {android.R.attr.colorBackground};
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs);
        mViewPager.setBackgroundColor(typedArray.getColor(0, Color.WHITE));
//        tabLayout.setupWithViewPager(mViewPager);

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewScheduleDetailsFragment viewScheduleDetailsFragment = (ViewScheduleDetailsFragment) ((SectionsPagerAdapter)mViewPager.getAdapter()).getFragment(0);
                onViewScheduleFragmentInteractionListener.onViewScheduleBackgroundClick(viewScheduleDetailsFragment.updateScheduleFromUserInput(schedule));
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        for (Fragment fragment : getFragmentManager().getFragments()) {
            if (fragment instanceof ViewScheduleDetailsFragment/* || fragment instanceof ViewScheduleMedicineFragment*/) {
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
                case 1:
                    return "Medicine";
            }
            return null;
        }
    }

    public interface OnViewScheduleFragmentInteractionListener {
        void onViewScheduleBackgroundClick(Schedule schedule);
    }

    public OnViewScheduleFragmentInteractionListener getOnViewScheduleFragmentInteractionListener() {
        return onViewScheduleFragmentInteractionListener;
    }

    public void setOnViewScheduleFragmentInteractionListener(OnViewScheduleFragmentInteractionListener onViewScheduleFragmentInteractionListener) {
        this.onViewScheduleFragmentInteractionListener = onViewScheduleFragmentInteractionListener;
    }
}
