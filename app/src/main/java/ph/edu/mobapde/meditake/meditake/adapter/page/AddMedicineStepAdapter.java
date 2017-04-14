package ph.edu.mobapde.meditake.meditake.adapter.page;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import java.util.ArrayList;

import ph.edu.mobapde.meditake.meditake.fragment.medicine.add.AddMedicineDetailsFragment;
import ph.edu.mobapde.meditake.meditake.fragment.medicine.add.AddMedicineTypeFragment;
import ph.edu.mobapde.meditake.meditake.fragment.schedule.add.AddScheduleDetailsFragment;
import ph.edu.mobapde.meditake.meditake.fragment.schedule.add.AddScheduleMedicineFragment;

/**
 * Created by Winfred Villaluna on 4/13/2017.
 */

public class AddMedicineStepAdapter extends AbstractFragmentStepAdapter {

    ArrayList<Fragment> fragments;
    Context contextHolder;

    public AddMedicineStepAdapter(FragmentManager fm, Context context){
        super(fm, context);
        this.contextHolder = context;
        this.fragments = new ArrayList<>();
    }

    @Override
    public Step createStep(@IntRange(from = 0L) int position) {
        return (Step) fragments.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    public void add(Fragment fragment){
        fragments.add(fragment);
    }

    public Fragment getFragment(int position){
        return fragments.get(position);
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0L) int position) {
        String title = "";
        String back = "BACK";
        if (position == 0) {
            title = AddMedicineTypeFragment.TITLE;
            back = "DISCARD";
        }else if(position == 1){
            title = AddMedicineDetailsFragment.TITLE;
        }
        Log.wtf("STEP TILE", "TITLE IS" + title);
        return new StepViewModel.Builder(contextHolder)
                .setTitle(title)
                .setBackButtonLabel(back)
                .create();
    }
}
