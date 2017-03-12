package ph.edu.mobapde.meditake.meditake.beans;

import android.os.Parcel;
import android.os.Parcelable;

import ph.edu.mobapde.meditake.meditake.R;

/**
 * Created by Winfred Villaluna on 2/13/2017.
 */

public class Tablet extends Medicine{

    public Tablet(){
        super();
    }

    public Tablet(String brandName, String genericName, String medicineFor, double amount){
        super(brandName, genericName, medicineFor, amount);
        setTabletSpecificInfo();
    }


    public void setTabletSpecificInfo(){
        this.modifier = " tablets";
        this.icon = R.drawable.selection_lozenge_colored;
    }
}
