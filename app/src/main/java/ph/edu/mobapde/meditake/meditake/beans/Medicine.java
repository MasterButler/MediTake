package ph.edu.mobapde.meditake.meditake.beans;

/**
 * Created by Winfred Villaluna on 2/13/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Bean class storing the medicine. This will be the super class that will be used to extend the
 * different types of medicine that a user can drink.
 *
 * @author Winfred Villaluna
 */
abstract public class Medicine {
    public static final String TABLE = "medicine";
    public static final String COLUMN_ID = "medicineId";
    public static final String COLUMN_MEDICINE_ID = "_medicine_id";
    public static final String COLUMN_BRAND_NAME = "brandName";
    public static final String COLUMN_GENERIC_NAME = "genericName";
    public static final String COLUMN_MEDICINE_FOR = "medicineFor";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DOSAGE = "dosage";
    //public static final String COLUMN_ICON = "icon";
    //public static final String COLUMN_MODIFIER = "modifier";
    public static final String COLUMN_MEDICINE_TYPE = "medicineType";

    public static final String CAPSULE = "capsule";
    public static final String SYRUP = "syrup";
    public static final String TABLET = "tablet";

    public static int NOT_SET = -999;
    public static int NOT_ENOUGH = -1;

    //id of the medicine in the server for easier retrieval/storage when it is already present in the cache
    protected int sqlId;
    //brand of the Medicine
    protected String brandName;
    //generic name of the medicine
    protected String genericName;
    //what illness is it used for?
    protected String medicineFor;
    //how much of the medicine do you have?
    protected double amount;
    //image of the id;
    protected int icon;
    //for the display, e.g. 5mL or 5 tablets where 'mL' and ' tablets' are the extensions
    protected String modifier;
    //default value that the user can use when the medicine will be drank by user.
    protected int dosage;
    //for the color to be used in the recyclerview
    protected int color;

    protected Medicine(){
        modifier = " units";
    }

    protected Medicine(String brandName, String genericName, String medicineFor, double amount){
        setBrandName(brandName);
        setGenericName(genericName);
        setMedicineFor(medicineFor);
        setAmount(amount);
    }

    /**
     * @return brandName(genericName) if available, otherwise returns genericName
     */
    public String getName(){
        String output = this.genericName;
        if(!(brandName == null || brandName.isEmpty())){
            output = this.brandName + "(" + output + ")";
        }
        return output;
    }

    /**
     * @returns brandName if available, otherwise returns genericName
     */
    public String getShortName(){
        return (brandName == null || brandName.isEmpty()) ? brandName : genericName;
    }

    /**
     * Not yet for implementation. Should the devs choose to implement when the user will be
     * running out of the said medicine, add this function
     * @param toDrink
     * @return remainingAmount
     */
    public double drink(double toDrink) {
        return this.amount - toDrink > NOT_ENOUGH ? this.amount - toDrink : NOT_ENOUGH;
    }

    public int getSqlId() {
        return sqlId;
    }

    public void setSqlId(int sqlid) {
        this.sqlId = sqlid;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getMedicineFor() {
        return medicineFor;
    }

    public void setMedicineFor(String medicineFor) {
        this.medicineFor = medicineFor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
