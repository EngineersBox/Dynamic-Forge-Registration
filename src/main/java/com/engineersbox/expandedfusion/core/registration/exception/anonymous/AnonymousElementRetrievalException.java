package com.engineersbox.expandedfusion.core.registration.exception.anonymous;

public class AnonymousElementRetrievalException extends RuntimeException {

    public AnonymousElementRetrievalException(final Throwable e) {
        super("Could not access value for anonymous element provider", e);
    }

    public AnonymousElementRetrievalException(final String message) {
        super(message);
    }

}
