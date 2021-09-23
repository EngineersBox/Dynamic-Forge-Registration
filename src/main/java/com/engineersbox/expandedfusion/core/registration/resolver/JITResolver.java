package com.engineersbox.expandedfusion.core.registration.resolver;

import com.engineersbox.expandedfusion.core.dist.annotation.DistBound;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;

public abstract class JITResolver implements RegistrationResolver {

    public abstract void instantiateResolvers();

    abstract <T extends RegistrationResolver> void registerHandledElementsOfResolver(final ResolverPhase resolverPhase);

    abstract <T extends RegistrationResolver> T getRegistrationResolver(final ResolverPhase resolverPhase);

    abstract <E extends Event> void publishCommonEvent(final E eventConsumer);
    public void publishBoundedCommonEvent(final ModLifecycleEvent eventConsumer) {
        this.publishCommonEvent(eventConsumer);
    }
    abstract <E extends Event> void publishGatherDataEvent(final E eventConsumer);
    public void publishBoundedGatherDataEvent(final GatherDataEvent eventConsumer) {
        this.publishGatherDataEvent(eventConsumer);
    }
    abstract <E extends Event> void publishClientEvent(final E eventConsumer);
    @DistBound(Dist.CLIENT)
    public void publishBoundedClientEvent(final ModLifecycleEvent eventConsumer) {
        this.publishClientEvent(eventConsumer);
    }
    abstract <E extends Event> void publishServerEvent(final E eventConsumer);
    @DistBound(Dist.DEDICATED_SERVER)
    public void publishBoundedServerEvent(final ServerLifecycleEvent eventConsumer) {
        this.publishServerEvent(eventConsumer);
    }

}
