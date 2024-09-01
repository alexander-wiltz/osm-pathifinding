package de.hskl.itanalyst.alwi.utilities;

/**
 * Handling way types from frontend.
 * Just a preparation for a feature.
 *
 * @author Alexander Wiltz
 * @version 0.1.0
 */
public enum TypeEnum {
    FOOT("foot"),
    BIKE("bike"),
    CAR("car");

    public final String label;

    private TypeEnum(String label) {
        this.label = label;
    }

    public static TypeEnum valueOfLabel(String label) {
        for (TypeEnum typeEnum : values()) {
            if (typeEnum.label.equals(label)) {
                return typeEnum;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
