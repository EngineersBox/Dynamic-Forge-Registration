package com.engineersbox.expandedfusion.core.event.manager;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;

public enum DistEvent {
    COMMON(
            ModLifecycleEvent.class,
            Dist.CLIENT, Dist.DEDICATED_SERVER
    ),
    DATA(
            GatherDataEvent.class,
            Dist.CLIENT, Dist.DEDICATED_SERVER
    ),
    CLIENT(
            ModLifecycleEvent.class,
            Dist.CLIENT
    ),
    SERVER(
            ServerLifecycleEvent.class,
            Dist.DEDICATED_SERVER
    );

    private final Class<? extends Event> eventClass;
    private final Dist[] applicableDists;

    DistEvent(final Class<? extends Event> eventClass,
              final Dist ...applicableDists) {
        this.eventClass = eventClass;
        this.applicableDists = applicableDists;
    }
}
