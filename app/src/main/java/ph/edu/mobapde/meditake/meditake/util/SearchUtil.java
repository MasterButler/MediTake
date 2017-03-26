package ph.edu.mobapde.meditake.meditake.util;

import ph.edu.mobapde.meditake.meditake.beans.Medicine;

/**
 * Created by Winfred Villaluna on 3/26/2017.
 */

public class SearchUtil {

    public static String[] searchWith(String toSearchWith){
        toSearchWith = toSearchWith.trim().toLowerCase();
        switch (toSearchWith){
            case "":
                return null;
            case "mL":
            case "liquid":
                return new String[]{Medicine.SYRUP};
            case "tablet":
            case "tablets":
                return new String[]{Medicine.TABLET};
            case "capsule":
            case "capsules":
                return new String[]{Medicine.CAPSULE};
            case "solid":
                return new String[]{Medicine.CAPSULE, Medicine.TABLET};
            default:
                return new String[]{toSearchWith};
        }
    }
}
