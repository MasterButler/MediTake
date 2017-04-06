package ph.edu.mobapde.meditake.meditake.activity;
;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.adapter.RecylerView.ScheduleAdapter;
import ph.edu.mobapde.meditake.meditake.beans.Schedule;
import ph.edu.mobapde.meditake.meditake.util.MedicineUtil;
import ph.edu.mobapde.meditake.meditake.util.ScheduleUtil;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class SettingsActivity extends AppCompatActivity{

    @BindView(R.id.toolbar_settings)
    Toolbar toolbar_settings;

    @BindView(R.id.settings_color_theme)
    LinearLayout settingsColorTheme;

    ScheduleUtil scheduleUtil;
    ScheduleAdapter scheduleAdapter;

    MedicineUtil medicineUtil;

    int selectedTheme;

    public void setUpActionBar(){
        setSupportActionBar(toolbar_settings);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);
        setUpActionBar();

        scheduleUtil = new ScheduleUtil(getBaseContext());
        medicineUtil = new MedicineUtil(getBaseContext());
    }

    @OnClick(R.id.settings_clear_all_schedule)
    public void deleteAllSchedule(){
        scheduleUtil.deleteAllSchedule();
        scheduleUtil.deleteAllSchedulePlan();
    }

    @OnClick(R.id.settings_clear_all_data)
    public void deleteAllMedicine(){
        scheduleUtil.deleteAllSchedule();
        scheduleUtil.deleteAllMedicine();
        scheduleUtil.deleteAllSchedulePlan();
        scheduleUtil.deleteAllMedicinePlan();
    }

    @OnClick(R.id.settings_color_theme)
    public void changeColorTheme(){
        showColorThemeSelection();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                finish();
                break;
        }

        return false;
    }

    public void showColorThemeSelection(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
        View colorChangeSelection = this.getLayoutInflater().inflate(R.layout.activity_color_change, null);

        final ArrayList<ImageView> themeSelection = new ArrayList<>();
        final ArrayList<Integer> unSelectedTheme = new ArrayList<>();
        final ArrayList<Integer> selectedTheme = new ArrayList<>();

        Log.wtf("ACTION", "OPENING THE SELCTOR");

        unSelectedTheme.add(R.drawable.theme_1);
        unSelectedTheme.add(R.drawable.theme_2);
        unSelectedTheme.add(R.drawable.theme_3);
        unSelectedTheme.add(R.drawable.theme_4);

        selectedTheme.add(R.drawable.selected_theme_1);
        selectedTheme.add(R.drawable.selected_theme_2);
        selectedTheme.add(R.drawable.selected_theme_3);
        selectedTheme.add(R.drawable.selected_theme_4);

        ImageView ivTheme1 = (ImageView) colorChangeSelection.findViewById(R.id.iv_theme1);
        ImageView ivTheme2 = (ImageView) colorChangeSelection.findViewById(R.id.iv_theme2);
        ImageView ivTheme3 = (ImageView) colorChangeSelection.findViewById(R.id.iv_theme3);
        ImageView ivTheme4 = (ImageView) colorChangeSelection.findViewById(R.id.iv_theme4);

        themeSelection.add(ivTheme1);
        themeSelection.add(ivTheme2);
        themeSelection.add(ivTheme3);
        themeSelection.add(ivTheme4);

        for(int i = 0; i < themeSelection.size(); i++){
            Log.wtf("ITERATION", "AT " + i + ": GETTING VALUE");
            themeSelection.get(i).setTag(i);
            themeSelection.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.wtf("ACTION", "CLICKED");
                    int value = (int) v.getTag();
                    setSelectedTheme(value);
                    for(int i = 0; i < themeSelection.size(); i++){
                        if(i == value){
                            themeSelection.get(i).setImageResource(selectedTheme.get(i));
                        }else{
                            themeSelection.get(i).setImageResource(unSelectedTheme.get(i));
                        }
                    }
                }
            });
        }

        alert.setView(colorChangeSelection);
        alert.setTitle("Label");
        alert.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                changeTheme();
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = alert.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                int[] attrs = {android.R.attr.colorAccent};
                TypedArray typedArray = SettingsActivity.this.obtainStyledAttributes(attrs);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(typedArray.getColor(0, Color.BLACK));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(typedArray.getColor(0, Color.BLACK));
            }
        });
        dialog.show();
    }

    public int getSelectedTheme() {
        return selectedTheme;
    }

    public void setSelectedTheme(int selectedTheme) {
        this.selectedTheme = selectedTheme;
    }

    public void changeTheme(){
        ThemeUtil.changeToTheme(this, selectedTheme+1);
    }

}
