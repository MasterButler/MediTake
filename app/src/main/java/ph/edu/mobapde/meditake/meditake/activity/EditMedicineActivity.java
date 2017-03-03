package ph.edu.mobapde.meditake.meditake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class EditMedicineActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_edit_medicine)
    Toolbar edit_medicine_toolbar;

    private void setUpActionBar(){
        setSupportActionBar(edit_medicine_toolbar);
        getSupportActionBar().setTitle("Edit Medicine Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_edit_medicine);
        ButterKnife.bind(this);

        setUpActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_edit_medicine, menu);
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
            case R.id.action_confirm_edit_medicine:
                confirmEditMedicine();
                break;
        }
        return true;
    }

    public void confirmEditMedicine(){
        Intent i = new Intent();
        i.setClass(getBaseContext(), ViewMedicineActivity.class);
        startActivity(i);
        //TODO add algorithm for editing medicine

    }
}
