package com.engineersbox.expandedfusion.core.registration.resolver;

import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;

public abstract class JITResolver extends RegistrationResolver {

    public abstract void instantiateResolvers();

    abstract <T extends RegistrationResolver> void registerHandledElementsOfResolver(final ResolverType resolverType);

    abstract <T extends RegistrationResolver> T getRegistrationResolver(final ResolverType resolverType);

    abstract void publishCommonEvent(final ModLifecycleEvent eventConsumer);
    abstract void publishGatherDataEvent(final GatherDataEvent eventConsumer);
    abstract void publishClientEvent(final ModLifecycleEvent eventConsumer);
    abstract void publishServerEvent(final ServerLifecycleEvent eventConsumer);

}
