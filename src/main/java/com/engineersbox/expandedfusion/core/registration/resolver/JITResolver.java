package com.engineersbox.expandedfusion.core.registration.resolver;

import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

public interface JITResolver {

    void instantiateResolvers();

    void registerBlocks();
    void registerItems();
    void registerFluids();

    void registerAll();

    RegistrationResolver getBlockRegistrationResolver();
    RegistrationResolver getItemRegistrationResolver();
    RegistrationResolver getFluidRegistrationResolver();

    void publishEvent(final ModLifecycleEvent eventConsumer);

}
