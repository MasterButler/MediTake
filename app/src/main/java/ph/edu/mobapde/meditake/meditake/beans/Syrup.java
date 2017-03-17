package ph.edu.mobapde.meditake.meditake.beans;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import ph.edu.mobapde.meditake.meditake.R;

/**
 * Created by Winfred Villaluna on 2/13/2017.
 */

public class Syrup extends Medicine{

    public static final String CLASS_NAME = "syrup";

    public Syrup(){
        super();
        setSyrupSpecificInfo();
    }

    public Syrup(String brandName, String genericName, String medicineFor, double amount){
        super(brandName, genericName, medicineFor, amount);
        setSyrupSpecificInfo();
    }


    public void setSyrupSpecificInfo(){
        this.modifier = "mL";
        this.icon = R.drawable.selection_syrup_colored;
//        this.color = Color.parseColor("#FFCDD2");
//        this.color = Color.parseColor("#D50000");
//        this.color = Color.parseColor("#EF6C00");
        this.color = Color.parseColor("#FFCC80");
    }
}
