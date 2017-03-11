package ph.edu.mobapde.meditake.meditake.beans;

import android.os.Parcel;
import android.os.Parcelable;

import ph.edu.mobapde.meditake.meditake.R;

/**
 * Created by Winfred Villaluna on 2/13/2017.
 */

public class Syrup extends Medicine{

    public Syrup(){
        super();
        this.modifier = "mL";
        this.icon = R.drawable.syrup_colored;
    }

    private Syrup(Parcel in){
        super(in);
        this.modifier = " mL";
        this.icon = R.drawable.syrup_colored;
    }

    public static final Parcelable.Creator<Syrup> CREATOR = new Parcelable.Creator<Syrup>(){
        @Override
        public Syrup createFromParcel(Parcel in){
            return new Syrup(in);
        }

        @Override
        public Syrup[] newArray(int size) {
            return new Syrup[size];
        }
    };
}
