package com.engineersbox.expandedfusion.core.dist.exception;

public class DistRestrictionException extends RuntimeException {

    public DistRestrictionException(final String dist1, final String dist2) {
        super(String.format(
                "Invoked @DistBound annotated method for unsupported dist: %s != %s",
                dist1,
                dist2
        ));
    }

}
