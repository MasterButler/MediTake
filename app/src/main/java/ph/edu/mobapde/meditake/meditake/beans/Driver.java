package ph.edu.mobapde.meditake.meditake.beans;

/**
 * Created by Winfred Villaluna on 2/13/2017.
 */

public class Driver {
    public static void main(String[] args){
        Medicine syrup = new Syrup();
        syrup.setAmount(100);

        Schedule s = new Schedule();
        s.setMedicineToDrink(syrup);
        s.setDosagePerDrinkingInterval(5);
        s.drinkMedicine();

        double timeToDrinkAgain = s.getLastTimeTaken().getTime() + 3*s.MILLIS_TO_SECONDS;
        System.out.println("PRESENT: " + s.getDrinkingInterval());
        System.out.println("FUTURE : " + timeToDrinkAgain);
        while(System.currentTimeMillis() != timeToDrinkAgain){
            double x = System.currentTimeMillis() - s.getLastTimeTaken().getTime();
            if(x % 500 == 0)
                System.out.println("\t?: " + x);
        }
        System.out.println("DONE!");

    }
}
