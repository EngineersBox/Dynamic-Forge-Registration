package com.engineersbox.expandedfusion.core.registration.resolver;

import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

public abstract class JITResolver extends RegistrationResolver {

    public abstract void instantiateResolvers();

    abstract <T extends RegistrationResolver> void registerHandledElementsOfResolver(final ResolverType resolverType);

    abstract <T extends RegistrationResolver> T getRegistrationResolver(final ResolverType resolverType);

    abstract void publishEvent(final ModLifecycleEvent eventConsumer);

}
