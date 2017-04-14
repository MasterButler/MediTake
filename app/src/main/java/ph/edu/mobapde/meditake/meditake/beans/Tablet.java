package ph.edu.mobapde.meditake.meditake.beans;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import ph.edu.mobapde.meditake.meditake.R;

/**
 * Created by Winfred Villaluna on 2/13/2017.
 */

public class Tablet extends Medicine{

    public static final String CLASS_NAME = "tablet";

    public Tablet(){
        super();
        setTabletSpecificInfo();
    }

    public Tablet(String brandName, String genericName, String medicineFor, long amount){
        super(brandName, genericName, medicineFor, amount);
        setTabletSpecificInfo();
    }


    public void setTabletSpecificInfo(){
        this.modifier = " tablets";
        this.icon = R.drawable.selection_lozenge_colored;
//        this.color = Color.parseColor("#DCEDC8");
//        this.color = Color.parseColor("#00C853");
//        this.color = Color.parseColor("#283593");
        this.color = Color.parseColor("#9FA8DA");
    }
}
