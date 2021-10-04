package com.engineersbox.expandedfusion.core.event.broker;

import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.ClientEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.CommonEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.DataEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.ServerEventHandler;
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
        this.priority = getMethodPriority(method) + getClassPriority(object);
        this.object = object;
    }

    private int getMethodPriority(final Method method) {
        final Subscriber annotation = method.getAnnotation(Subscriber.class);
        return annotation != null ? priorityRangeCapped(annotation.priority()) : EventBroker.MAX_PRIORITY;
    }

    private int getClassPriority(final T object) {
        final ClientEventHandler clientEventHandler = object.getClass().getAnnotation(ClientEventHandler.class);
        if (clientEventHandler != null) {
            return priorityRangeCapped(clientEventHandler.priority());
        }
        final CommonEventHandler commonEventHandler = object.getClass().getAnnotation(CommonEventHandler.class);
        if (commonEventHandler != null) {
            return priorityRangeCapped(commonEventHandler.priority());
        }
        final DataEventHandler dataEventHandler = object.getClass().getAnnotation(DataEventHandler.class);
        if (dataEventHandler != null) {
            return priorityRangeCapped(dataEventHandler.priority());
        }
        final ServerEventHandler serverEventHandler = object.getClass().getAnnotation(ServerEventHandler.class);
        if (serverEventHandler != null) {
            return priorityRangeCapped(serverEventHandler.priority());
        }
        return EventBroker.MAX_PRIORITY;
    }

    private int priorityRangeCapped(final int priority) {
        return Math.max(Math.min(priority, EventBroker.MAX_PRIORITY), EventBroker.MIN_PRIORITY);
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
