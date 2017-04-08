package ph.edu.mobapde.meditake.meditake.activity;
;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    @BindView(R.id.tv_color_theme)
    TextView tvColorTheme;

    ScheduleUtil scheduleUtil;
    ScheduleAdapter scheduleAdapter;

    MedicineUtil medicineUtil;

    ArrayList<String> themeNameDisplay;
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
        themeNameDisplay = new ArrayList<>();
        themeNameDisplay.add("Purple Theme");
        themeNameDisplay.add("Indigo Theme");
        themeNameDisplay.add("Default Theme");
        themeNameDisplay.add("Blue Theme");

        tvColorTheme.setText("Currently set to " + themeNameDisplay.get(ThemeUtil.getSelectedTheme(getBaseContext())-1));
    }

    @OnClick(R.id.settings_clear_all_schedule)
    public void deleteAllSchedule(){
        showClearSchedulesPrompt();
    }

    @OnClick(R.id.settings_clear_all_data)
    public void onClearAllDataPress(){
        showClearAllConfirmationPrompt();
    }

    public void clearSchedules(){
        scheduleUtil.deleteAllSchedule();
        scheduleUtil.deleteAllSchedulePlan();
        scheduleUtil.deleteAllMedicinePlan();
    }

    public void clearAll(){
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

    public void setSelectedTheme(int selectedTheme) {
        this.selectedTheme = selectedTheme;
    }

    public void changeTheme(){
        ThemeUtil.changeToTheme(this, selectedTheme+1);
        tvColorTheme.setText("Currently set to " + themeNameDisplay.get(ThemeUtil.getSelectedTheme(getBaseContext())-1));
    }

    @SuppressWarnings("ResourceType")
    public void showClearAllConfirmationPrompt(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
        final TextView guide = new TextView(SettingsActivity.this);
        final TextView confirmation = new TextView(SettingsActivity.this);
        final EditText input = new EditText(SettingsActivity.this);

        LinearLayout container = new LinearLayout(SettingsActivity.this);

        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.setOrientation(LinearLayout.VERTICAL);

        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_left_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_right_margin);

        LinearLayout.LayoutParams tvParams = new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.setOrientation(LinearLayout.VERTICAL);

        tvParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_tv_left_margin);
        tvParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_tv_right_margin);
        tvParams.topMargin = 12;

        String warning = "This action CANNOT be undone. This will permanently delete ALL medicine and schedules in the application. This action cannot be undone.";
        String warning2 = "Please type 'Delete All' to confirm your action.";
        guide.setText(warning);
        confirmation.setText(warning2);

        guide.setLayoutParams(tvParams);
        confirmation.setLayoutParams(tvParams);

        input.setLayoutParams(params);
        input.setSingleLine();

        container.addView(guide);
        container.addView(confirmation);
        container.addView(input);

        alert.setTitle("Clear all data");
        alert.setView(container);
        alert.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                clearAll();
                finish();
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
                int[] attrs = {android.R.attr.colorAccent, android.R.attr.colorBackground};

                TypedArray typedArray = SettingsActivity.this.obtainStyledAttributes(attrs);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(typedArray.getColor(0, Color.BLACK));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(typedArray.getColor(0, Color.BLACK));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(R.color.colorLogoDarkGray);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(typedArray.getColor(1, Color.WHITE));
            }
        });

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.wtf("CHECK", ">>" + String.valueOf(s) + "<< VALUE --> " + String.valueOf(s).equals("Delete All"));
                if(String.valueOf(s).equals("Delete All")){
                    int[] attrs = {android.R.attr.colorAccent, android.R.attr.colorBackground};

                    TypedArray typedArray = SettingsActivity.this.obtainStyledAttributes(attrs);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(typedArray.getColor(0, Color.BLACK));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }else{
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(R.color.colorLogoDarkGray);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog.show();
    }

    @SuppressWarnings("ResourceType")
    public void showClearSchedulesPrompt(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
        final TextView confirmation = new TextView(SettingsActivity.this);

        LinearLayout container = new LinearLayout(SettingsActivity.this);

        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams tvParams = new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container.setOrientation(LinearLayout.VERTICAL);

        tvParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_tv_left_margin);
        tvParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_tv_right_margin);
        tvParams.topMargin = 12;

        String warning2 = "Are you sure you want to delete all schedules? This action cannot be undone.";
        confirmation.setText(warning2);

        confirmation.setLayoutParams(tvParams);

        container.addView(confirmation);

        alert.setTitle("Clear all data");
        alert.setView(container);
        alert.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                clearSchedules();
                dialog.dismiss();
                finish();
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
                int[] attrs = {android.R.attr.colorAccent, android.R.attr.colorBackground};

                TypedArray typedArray = SettingsActivity.this.obtainStyledAttributes(attrs);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(typedArray.getColor(0, Color.BLACK));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(typedArray.getColor(0, Color.BLACK));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(typedArray.getColor(1, Color.WHITE));
            }
        });

        dialog.show();
    }
}
