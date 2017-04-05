package ph.edu.mobapde.meditake.meditake.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.RecylerView.ScheduleAdapter;
import ph.edu.mobapde.meditake.meditake.util.DrawerManager;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class SettingsActivity extends AppCompatActivity{
    ScheduleUtil scheduleUtil;
    ScheduleAdapter scheduleAdapter;

    MedicineUtil medicineUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        scheduleUtil = new ScheduleUtil(getBaseContext());
        medicineUtil = new MedicineUtil(getBaseContext());
    }

    @OnClick(R.id.settings_delete_all_schedule)
    public void deleteAllSchedule(){
        scheduleUtil.deleteAllSchedule();
        finish();
    }

    @OnClick(R.id.settings_color_theme)
    public void changeColorTheme(){

    }
}
