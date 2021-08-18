package com.engineersbox.expandedfusion.core.registration.exception.provider;

public class ResolverComponentInstantiationException extends RuntimeException {

    public ResolverComponentInstantiationException(final String message) {
        super(message);
    }

    public ResolverComponentInstantiationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ResolverComponentInstantiationException(final Throwable cause) {
        super(cause);
    }

}
