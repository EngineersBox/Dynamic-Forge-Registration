package com.engineersbox.expandedfusion.core.event.broker;

import com.engineersbox.expandedfusion.core.registration.exception.resolver.event.broker.SubscriberMethodInvocationException;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
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

    public <E extends Event> void invoke(final E event) {
        try {
            this.method.invoke(this.object, event);
        } catch (final  IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new SubscriberMethodInvocationException(this.method, e);
        }
    }
}
