package ph.edu.mobapde.meditake.meditake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class MedicineListActivity extends AppCompatActivity {

    @BindView(R.id.fab_add_medicine)
    FloatingActionButton fab_add_medicine;

    @BindView(R.id.changeTheme)
    Button changeThemeButton;
    @BindView(R.id.randomMedicine)
    Button medicineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_medicine_list);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.fab_add_medicine)
    public void addMedicine(){
        Intent i = new Intent(getBaseContext(), AddMedicineActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.changeTheme)
    public void changeTheme(){
        int newTheme = ThemeUtil.getSelectedTheme()+1;
        ThemeUtil.changeToTheme(this, newTheme%2);
        Toast.makeText(getBaseContext(), "Changing theme", Toast.LENGTH_SHORT);
    }

    @OnClick(R.id.randomMedicine)
    public void viewMedicineInfo(){
        Intent i = new Intent();
        i.setClass(getBaseContext(), ViewMedicineActivity.class);
        startActivity(i);
    }
}
