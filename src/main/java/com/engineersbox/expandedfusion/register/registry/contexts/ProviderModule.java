package com.engineersbox.expandedfusion.register.registry.contexts;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.google.inject.AbstractModule;

public class ProviderModule extends AbstractModule {

    private static final ExpandedFusion.RegistryProvider registryProvider = new ExpandedFusion.RegistryProvider();

    @Override
    protected void configure() {
        bind(ExpandedFusion.RegistryProvider.class).toInstance(registryProvider);
    }
}
