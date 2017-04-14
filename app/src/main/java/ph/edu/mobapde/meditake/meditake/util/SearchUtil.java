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
            case "syrup":
            case "syrups":
                return new String[]{Medicine.SYRUP};
            case "lozenges":
            case "aspirin":
            case "tablet":
            case "tablets":
                return new String[]{Medicine.TABLET};
            case "pill":
            case "capsule":
            case "capsules":
                return new String[]{Medicine.CAPSULE};
            case "solid":
            case "non-liquid":
                return new String[]{Medicine.CAPSULE, Medicine.TABLET};
            default:
                return new String[]{toSearchWith};
        }
    }
}
