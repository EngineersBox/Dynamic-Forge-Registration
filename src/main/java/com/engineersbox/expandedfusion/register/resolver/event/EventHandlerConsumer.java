package com.engineersbox.expandedfusion.register.resolver.event;

import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

@FunctionalInterface
public interface EventHandlerConsumer<T extends ModLifecycleEvent> {

    void accept(final T t);

}
