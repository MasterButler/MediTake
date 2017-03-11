package ph.edu.mobapde.meditake.meditake.beans;

import android.os.Parcel;
import android.os.Parcelable;

import ph.edu.mobapde.meditake.meditake.R;

/**
 * Created by Winfred Villaluna on 2/13/2017.
 */

public class Capsule extends Medicine{

    public Capsule(){
        super();
        this.modifier = " capsules";
        this.icon = R.drawable.pill_colored;

    }

    private Capsule(Parcel in){
        super(in);
        this.modifier = " capsules";
        this.icon = R.drawable.pill_colored;
    }

    public static final Parcelable.Creator<Capsule> CREATOR = new Parcelable.Creator<Capsule>(){
        @Override
        public Capsule createFromParcel(Parcel in){
            return new Capsule(in);
        }

        @Override
        public Capsule[] newArray(int size) {
            return new Capsule[size];
        }
    };
}
