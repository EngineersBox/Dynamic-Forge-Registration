package com.engineersbox.expandedfusion.core.registration.contexts;

import com.google.inject.AbstractModule;

public final class ProviderModule extends AbstractModule {

    private static final RegistryProvider registryProvider = new RegistryProvider();

    @Override
    protected void configure() {
        bind(RegistryProvider.class).toInstance(registryProvider);
    }
}
