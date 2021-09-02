package com.engineersbox.expandedfusion.core.event.manager;

import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.broker.EventBroker;
import com.engineersbox.expandedfusion.core.event.exception.manager.BrokerEventConsumerException;
import net.minecraftforge.eventbus.api.Event;

import java.util.EnumMap;

public class BrokerManager<T extends Enum<T>> implements Manager<T> {

    private final EnumMap<T, EventBroker> brokerMapping;

    public BrokerManager(final Class<T> enumClass) {
        this.brokerMapping = new EnumMap<>(enumClass);
    }

    @Override
    public void putMapping(final T key, final EventBroker broker) {
        this.brokerMapping.put(key, broker);
    }

    @Override
    public EventBroker get(final T key) {
        return this.brokerMapping.get(key);
    }

    @Override
    public <E extends EventSubscriptionHandler> void addConsumer(final T key,
                                                                 final E eventConsumer) {
        final EventBroker broker = get(key);
        if (broker == null) {
            throw new BrokerEventConsumerException(String.format(
                    "Attempted to add consumer to non-existent broker matching key %s",
                    key.name()
            ));
        }
        broker.addConsumer(eventConsumer);
    }

    @Override
    public <E extends EventSubscriptionHandler> void removeConsumer(final T key,
                                                                    final E eventConsumer) {
        final EventBroker broker = get(key);
        if (broker == null) {
            throw new BrokerEventConsumerException(String.format(
                    "Attempted to remove consumer to non-existent broker matching key %s",
                    key.name()
            ));
        }
        broker.removeConsumer(eventConsumer);
    }

    @Override
    public <E extends Event> void publishEvent(final T key,
                                               final E event) {
        final EventBroker broker = get(key);
        if (broker == null) {
            throw new BrokerEventConsumerException(String.format(
                    "Attempted to publish event to non-existent broker matching key %s",
                    key.name()
            ));
        }
        broker.publishEvent(event);
    }
}
