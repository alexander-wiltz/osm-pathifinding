package de.hskl.itanalyst.alwi.utilities;

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
