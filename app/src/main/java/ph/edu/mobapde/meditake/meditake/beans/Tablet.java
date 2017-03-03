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
        this.modifier = " tablets";
        this.icon = R.drawable.lozenge_colored;
    }

    private Tablet(Parcel in){
        super(in);
        this.modifier = " tablets";
        this.icon = R.drawable.lozenge_colored;
    }

    public static final Parcelable.Creator<Tablet> CREATOR = new Parcelable.Creator<Tablet>(){
        @Override
        public Tablet createFromParcel(Parcel in){
            return new Tablet(in);
        }

        @Override
        public Tablet[] newArray(int size) {
            return new Tablet[size];
        }
    };
}
