package ph.edu.mobapde.meditake.meditake.beans;

/**
 * Created by Winfred Villaluna on 2/13/2017.
 */

public class Try implements Runnable {
    @Override
    public void run() {
        Schedule a = new Schedule();
        a.drinkMedicine();
    }
}
