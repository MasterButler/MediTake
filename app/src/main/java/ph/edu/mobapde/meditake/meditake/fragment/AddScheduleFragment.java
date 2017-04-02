package ph.edu.mobapde.meditake.meditake.fragment;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;

public class AddScheduleFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    public AddScheduleFragment(){

    }

    public static AddScheduleFragment newInstance() {
        AddScheduleFragment fragment = new AddScheduleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_add_schedule_fragment, container, false);
        ButterKnife.bind(this, v);

        setHasOptionsMenu(true);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mSectionsPagerAdapter.add(AddScheduleDetailsFragment.newInstance(1));
        mSectionsPagerAdapter.add(AddScheduleMedicineFragment.newInstance(2));

        mViewPager.setAdapter(mSectionsPagerAdapter);

        return v;
    }

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

        for (Fragment fragment : getFragmentManager().getFragments()) {
            if (fragment instanceof AddScheduleDetailsFragment) {
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
