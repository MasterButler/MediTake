package ph.edu.mobapde.meditake.meditake.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class WelcomeScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        super.setActionBar(null);
        setContentView(R.layout.activity_welcome_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(WelcomeScreenActivity.this, ScheduleListActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, WelcomeScreenActivity.SPLASH_TIME_OUT);
    }
}
