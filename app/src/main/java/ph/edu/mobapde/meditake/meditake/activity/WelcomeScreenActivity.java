package ph.edu.mobapde.meditake.meditake.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ph.edu.mobapde.meditake.meditake.R;
import ph.edu.mobapde.meditake.meditake.util.ThemeUtil;

public class WelcomeScreenActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;

    @BindView(R.id.activity_welcome_screen)
    ImageView ivBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        //super.setActionBar(null);
        setContentView(R.layout.activity_welcome_screen);

        ButterKnife.bind(this);

        int theme = ThemeUtil.getSelectedTheme(getBaseContext());
        ArrayList<Integer> splashColors = new ArrayList<>();
        splashColors.add(R.drawable.splash_theme_1);
        splashColors.add(R.drawable.splash_theme_2);
        splashColors.add(R.drawable.splash_theme_3);
        splashColors.add(R.drawable.splash_theme_4);

        ivBg.setImageResource(splashColors.get(theme-1));

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
