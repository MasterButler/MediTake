package ph.edu.mobapde.meditake.meditake.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class ViewMedicineActivity extends AppCompatActivity {

    @BindView(R.id.random)
    Button button;

    @BindView(R.id.toolbar_view_medicine)
    Toolbar view_medicine_toolbar;

    private void setUpActionBar(){
        setSupportActionBar(view_medicine_toolbar);
        getSupportActionBar().setTitle("View Medicine Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_view_medicine);
        ButterKnife.bind(this);

        setUpActionBar();
    }

    @OnClick(R.id.random)
    public void onClick(){
        Intent i = new Intent();
        i.setClass(getBaseContext(), AddMedicineActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_view_medicine, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_delete_medicine:
                deleteMedicine();
                break;
            case R.id.action_edit_medicine:
                editMedicine();
                break;
        }
        return true;
    }

    public void editMedicine(){
        Intent i = new Intent();
        i.setClass(getBaseContext(), EditMedicineActivity.class);
        startActivity(i);
        //TODO add algorithm for adding the previous information of the medicine to EditMedicineActivity.java, though it can be done through this or directly to EditMedicineActivity.java
    }

    public void deleteMedicine(){
        Toast.makeText(getBaseContext(), "Deleted medicine", Toast.LENGTH_SHORT);
        //TODO add algorithm for deleting medicine, via safechecking if there are other schedules that will be using it, etc etc.
    }
}
