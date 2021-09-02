package com.engineersbox.expandedfusion.core.elements.machine.exception;

import com.engineersbox.expandedfusion.core.elements.DataField;

public class UnknownDataFieldException extends RuntimeException {

    public UnknownDataFieldException(final int index) {
        super(String.format(
                "Unknown data field when attempting to set field: %s [idx: %d]",
                DataField.fromInt(index),
                index
        ));
    }

}
