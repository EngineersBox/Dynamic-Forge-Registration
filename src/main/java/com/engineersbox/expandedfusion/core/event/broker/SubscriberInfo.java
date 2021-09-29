package com.engineersbox.expandedfusion.core.event.broker;

import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import com.engineersbox.expandedfusion.core.registration.exception.resolver.event.broker.SubscriberMethodInvocationException;
import net.minecraftforge.eventbus.api.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SubscriberInfo<T> {
    private final Method method;
    private final int priority;
    private final T object;

    public SubscriberInfo(final Method method,
                          final T object) {
        this.method = method;
        this.priority = getMethodPriority(method);
        this.object = object;
    }

    private int getMethodPriority(final Method method) {
        final Subscriber annotation = method.getAnnotation(Subscriber.class);
        return annotation != null ? annotation.priority() : Integer.MAX_VALUE;
    }

    public T getObject() {
        return this.object;
    }

    public int getPriority() {
        return this.priority;
    }

    public <E extends Event> void invoke(final E event) {
        try {
            this.method.invoke(this.object, event);
        } catch (final  IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new SubscriberMethodInvocationException(this.method, e);
        }
    }
}
