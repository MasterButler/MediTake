package ph.edu.mobapde.meditake.meditake;

import android.view.animation.Interpolator;

/**
 * Created by Winfred Villaluna on 4/13/2017.
 */

public class ReverseInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float paramFloat) {
        return Math.abs(paramFloat -1f);
    }
}