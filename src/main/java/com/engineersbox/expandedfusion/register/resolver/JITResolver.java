package com.engineersbox.expandedfusion.register.resolver;

import com.engineersbox.expandedfusion.register.provider.RegistrationResolver;

public interface JITResolver {

    void instantiateResolvers();

    void registerBlocks();
    void registerItems();
    void registerFluids();

    void registerAll();

    RegistrationResolver getBlockRegistrationResolver();
    RegistrationResolver getItemRegistrationResolver();
    RegistrationResolver getFluidRegistrationResolver();

}
