package ph.edu.mobapde.meditake.meditake.beans;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import ph.edu.mobapde.meditake.meditake.R;

/**
 * Created by Winfred Villaluna on 2/13/2017.
 */

public class Capsule extends Medicine{

    public static final String CLASS_NAME = "capsule";

    public Capsule(){
        super();
        setCapsuleSpecificInfo();
    }

    public Capsule(String brandName, String genericName, String medicineFor, long amount){
        super(brandName, genericName, medicineFor, amount);
        setCapsuleSpecificInfo();
    }

    private void setCapsuleSpecificInfo(){
        this.modifier = " capsules";
        this.icon = R.drawable.pill_capsule_white;
//        this.color = Color.parseColor("#C5CAE9");
//        this.color = Color.parseColor("#304FFE");
//        this.color = Color.parseColor("#0277BD");
        this.color = Color.parseColor("#81D4FA");
    }
}
