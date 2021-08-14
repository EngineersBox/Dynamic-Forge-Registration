package com.engineersbox.expandedfusion.register.resolver;

import com.engineersbox.expandedfusion.register.provider.RegistrationResolver;
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
