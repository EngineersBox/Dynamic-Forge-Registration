package com.engineersbox.expandedfusion.core.reflection.exception;

public class AnnotationFieldValueUpdateException extends RuntimeException {

    public AnnotationFieldValueUpdateException(final String message) {
        super(message);
    }

    public AnnotationFieldValueUpdateException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
