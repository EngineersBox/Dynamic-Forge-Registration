package com.engineersbox.expandedfusion.core.registration.exception.resolver.event.broker;

import java.lang.reflect.Method;

public class SubscriberMethodInvocationException extends RuntimeException {

    public SubscriberMethodInvocationException(final Method method, final Throwable cause) {
        super(String.format(
                "Encountered an exception when trying to invoke bound handler method %s",
                method.getName()
        ), cause);
    }

}
