package ph.edu.mobapde.meditake.meditake.adapter.page;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Winfred Villaluna on 4/3/2017.
 */
public class AddSchedulePagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments;

    public AddSchedulePagerAdapter(FragmentManager fm) {
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