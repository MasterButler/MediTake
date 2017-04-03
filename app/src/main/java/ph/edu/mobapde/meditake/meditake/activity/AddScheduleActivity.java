package ph.edu.mobapde.meditake.meditake.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.fragment.AddSchedule.AddScheduleDetailsFragment;
import ph.edu.mobapde.meditake.meditake.fragment.AddSchedule.AddScheduleMedicineFragment;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class AddScheduleActivity extends AppCompatActivity{

    private SectionsPagerAdapter mSectionsPagerAdapter;

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    public AddScheduleActivity(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.fragment_add_schedule);
        ButterKnife.bind(this);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.add(AddScheduleDetailsFragment.newInstance(1));
        mSectionsPagerAdapter.add(AddScheduleMedicineFragment.newInstance(2));

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_add_schedule, container, false);
//        ButterKnife.bind(this, v);
//
//        setHasOptionsMenu(true);
//
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//        tabLayout.setupWithViewPager(mViewPager);
//
//         Create the adapter that will return a fragment for each of the three
//         primary sections of the activity.
//
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        mSectionsPagerAdapter.add(AddScheduleDetailsFragment.newInstance(1));
//        mSectionsPagerAdapter.add(AddScheduleMedicineFragment.newInstance(2));
//
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//
//        return v;
//    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menu.clear();
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add Schedule");
//        menuInflater.inflate(R.menu.menu_add_schedule_details, menu);
//        super.onCreateOptionsMenu(menu, menuInflater);
//    }

    @Override
    public void onResume() {
        super.onResume();

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof AddScheduleDetailsFragment) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(fragment);
                ft.attach(fragment);
                ft.commit();
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
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
}
