package ph.edu.mobapde.meditake.meditake.activity;

import android.content.Context;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class DrinkMedicineActivity extends AppCompatActivity {

    private PowerManager.WakeLock wl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_drink_medicine);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
