package com.engineersbox.expandedfusion.core.registration.contexts;

import com.google.inject.AbstractModule;

public class RegistrationModule extends AbstractModule {
    private final Registration registration;

    public RegistrationModule(final String modID) {
        this.registration = new Registration(modID);
    }

    @Override
    protected void configure() {
        bind(Registration.class).toInstance(this.registration);
    }
}
