package ph.edu.mobapde.meditake.meditake.beans;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import ph.edu.mobapde.meditake.meditake.R;

/**
 * Created by Winfred Villaluna on 2/13/2017.
 */

public class Capsule extends Medicine{

    public Capsule(){
        super();
        setCapsuleSpecificInfo();
    }

    public Capsule(String brandName, String genericName, String medicineFor, double amount){
        super(brandName, genericName, medicineFor, amount);
        setCapsuleSpecificInfo();
    }

    private void setCapsuleSpecificInfo(){
        this.modifier = " capsules";
        this.icon = R.drawable.selection_capsule_colored;
        this.color = Color.parseColor("#C5CAE9");
//        this.color = Color.parseColor("#304FFE");
    }
}
