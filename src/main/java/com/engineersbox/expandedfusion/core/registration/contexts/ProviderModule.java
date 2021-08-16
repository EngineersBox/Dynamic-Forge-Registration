package com.engineersbox.expandedfusion.core.registration.contexts;

import com.engineersbox.expandedfusion.core.registration.provider.RegistryProvider;
import com.google.inject.AbstractModule;

public class ProviderModule extends AbstractModule {

    private static final RegistryProvider registryProvider = new RegistryProvider();

    @Override
    protected void configure() {
        bind(RegistryProvider.class).toInstance(registryProvider);
    }
}
