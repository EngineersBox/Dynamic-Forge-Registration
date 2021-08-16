package com.engineersbox.expandedfusion.core.elements;

public enum DataField {
    ENERGY_STORED_LOWER(0),
    ENERGY_STORED_HIGHER(1),
    MAX_ENERGY_STORED_LOWER(2),
    MAX_ENERGY_STORED_HIGHER(3),
    PROGRESS(5),
    PROCESS_TIME(6);

    public final int index;

    DataField(final int index) {
        this.index = index;
    }

    public static DataField fromInt(final int idx) {
        switch (idx) {
            case 1:
                return ENERGY_STORED_HIGHER;
            case 2:
                return MAX_ENERGY_STORED_LOWER;
            case 3:
                return MAX_ENERGY_STORED_HIGHER;
            case 5:
                return PROGRESS;
            case 6:
                return PROCESS_TIME;
            default:
                return ENERGY_STORED_LOWER;
        }
    }
}
