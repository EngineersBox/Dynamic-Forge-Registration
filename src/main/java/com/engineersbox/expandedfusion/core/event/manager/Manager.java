package com.engineersbox.expandedfusion.core.event.manager;

import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.broker.EventBroker;
import net.minecraftforge.eventbus.api.Event;

public interface Manager<T extends Enum<T>> {

    void putMapping(final T key, final EventBroker broker);
    EventBroker get(final T key);

    <E extends EventSubscriptionHandler> void addConsumer(final T key, final E eventConsumer);
    <E extends EventSubscriptionHandler> void removeConsumer(final T key,  final E eventConsumer);
    <E extends Event> void publishEvent(final T key, final E event);

}
