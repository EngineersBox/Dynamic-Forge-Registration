package com.engineersbox.expandedfusion.core.registration.resolver.event.broker;

import com.engineersbox.expandedfusion.core.registration.exception.resolver.event.broker.SubscriberMethodInvocationException;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SubscriberInfo<T> {
    final Method method;
    final T object;

    public SubscriberInfo(final Method method,
                          final T object) {
        this.method = method;
        this.object = object;
    }

    public <E extends ModLifecycleEvent> void invoke(final E instance) {
        try {
            this.method.invoke(this.object, instance);
        } catch (final  IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new SubscriberMethodInvocationException(this.method, e);
        }
    }
}
