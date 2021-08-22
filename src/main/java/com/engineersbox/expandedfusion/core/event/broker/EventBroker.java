package com.engineersbox.expandedfusion.core.event.broker;

import com.engineersbox.expandedfusion.core.registration.exception.handler.SubscriptionEventHandlerException;
import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EventBroker {
    private static final Logger LOGGER = LogManager.getLogger(EventBroker.class);
    private final Map<Class<?>, List<SubscriberInfo<? super EventSubscriptionHandler>>> consumers = new LinkedHashMap<>();

    public <T extends EventSubscriptionHandler> void addConsumer(final T eventConsumer) {
        final Class<? extends EventSubscriptionHandler> consumerClass = eventConsumer.getClass();
        for (final Method method : consumerClass.getMethods()) {
            final Class<?>[] parameterTypes = method.getParameterTypes();
            final Subscriber annotation = method.getAnnotation(Subscriber.class);
            if (annotation == null) {
                LOGGER.debug(
                    "Missing subscription binding annotation for method [{}] in class [{}], skipping",
                    method.getName(),
                    consumerClass.getName()
                );
                continue;
            }
            if (parameterTypes.length != 1 || !(Event.class.isAssignableFrom(parameterTypes[0]))) {
                LOGGER.error(
                    "Invalid EventSubscriptionHandler [{}] in class [{}]. Handlers annotated with @Subscriber must take 1 argument extending the base class {}",
                    method.getName(),
                    consumerClass.getName(),
                    ModLifecycleEvent.class.getName()
                );
                continue;
            }
            final Class<?> subscribeTo = parameterTypes[0];
            final List<SubscriberInfo<? super EventSubscriptionHandler>> subscriberInfos = consumers.computeIfAbsent(subscribeTo, k -> new ArrayList<>());
            subscriberInfos.add(new SubscriberInfo<>(method, eventConsumer));
        }
    }

    public <T extends EventSubscriptionHandler> void removeConsumer(final T eventConsumer) {
        for (final List<SubscriberInfo<? super EventSubscriptionHandler>> subscriberInfos : consumers.values()) {
            for (int i = subscriberInfos.size() - 1; i >= 0; i--)
                if (subscriberInfos.get(i).object == eventConsumer)
                    subscriberInfos.remove(i);
        }
    }

    public <T extends ModLifecycleEvent> void publishEvent(final T event) {
        List<SubscriberInfo<? super EventSubscriptionHandler>> subscriberInfos = consumers.get(event.getClass());
        if (subscriberInfos == null) {
            LOGGER.trace("No subscribers available to accept event [{}]", event.getClass().getName());
            return;
        }
        subscriberInfos.forEach((final SubscriberInfo<? super EventSubscriptionHandler> info) -> {
            try {
                info.invoke(event);
            } catch (final Exception e) {
                throw new SubscriptionEventHandlerException(e);
            }
        });
        LOGGER.trace("Published event to {} subscribers", subscriberInfos.size());
    }
}