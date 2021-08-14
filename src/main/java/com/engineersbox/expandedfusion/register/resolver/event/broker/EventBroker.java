package com.engineersbox.expandedfusion.register.resolver.event.broker;

import com.engineersbox.expandedfusion.register.resolver.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.register.resolver.event.annotation.Subscriber;
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
                LOGGER.debug(String.format(
                    "Missing subscription binding annotation for method [%s] in class [%s], skipping",
                    method.getName(),
                    consumerClass.getName()
                ));
                continue;
            }
            if (parameterTypes.length != 1 || !(Event.class.isAssignableFrom(parameterTypes[0]))) {
                LOGGER.error(String.format(
                    "Invalid EventSubscriptionHandler [%s] in class [%s]. Handlers annotated with @Subscriber must take 1 argument extending the base class %s",
                    method.getName(),
                    consumerClass.getName(),
                    ModLifecycleEvent.class.getName()
                ));
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
            LOGGER.trace(String.format(
                "No subscribers available to accept event [%s]",
                    event.getClass().getName()
            ));
            return;
        }
        subscriberInfos.forEach((final SubscriberInfo<? super EventSubscriptionHandler> info) -> info.invoke(event));
        LOGGER.trace(String.format(
            "Published event to %d subscribers",
            subscriberInfos.size()
        ));
    }
}
