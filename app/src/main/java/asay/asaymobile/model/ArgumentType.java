package asay.asaymobile.model;

/**
 * Created by s123725 on 15/12/2017.
 */


public enum ArgumentType{
    FOR("For", 0),
    AGAINST("Againt",1),
    NEUTRAL("Neutral", 2);

    private String stringValue;

    ArgumentType(String toString, int value) {
        stringValue = toString;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}


