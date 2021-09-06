package com.engineersbox.expandedfusion.core.reflection.exception;

public class ClassMethodNotPresentException extends RuntimeException {

    public ClassMethodNotPresentException(final Class<?> clazz,
                                          final String methodName) {
        super(String.format(
                "No such method %s with @Inject annotation could be found on class %s",
                methodName,
                clazz.getName()
        ));
    }

    public ClassMethodNotPresentException(final String message) {
        super(message);
    }

}
