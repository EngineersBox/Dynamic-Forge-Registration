package com.engineersbox.expandedfusion.core.registration.provider.grouping.anonymous;

import com.engineersbox.expandedfusion.core.registration.annotation.anonymous.AnonymousElementRegistrant;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.reflections.Reflections;

import java.util.Set;

public class AnonymousElementImplClassGrouping extends ImplClassGroupings<AnonymousElementImplGrouping> {

    private final Reflections reflections;

    @Inject
    public AnonymousElementImplClassGrouping(@Named("packageReflections") final Reflections reflections) {
        this.reflections = reflections;
    }

    @Override
    public void collectAnnotatedResources() {
        final Set<Class<?>> blockProviderAnnotatedClasses = this.reflections.getTypesAnnotatedWith(AnonymousElementRegistrant.class);
        for (final Class<?> c : blockProviderAnnotatedClasses) {
            addIfNotExists(c.getCanonicalName(), c);
        }
    }

    @Override
    public void addIfNotExists(String name, Class<?> toAdd) {
        AnonymousElementImplGrouping anonymousElementImplGrouping = this.classGroupings.get(name);
        if (anonymousElementImplGrouping == null) {
            anonymousElementImplGrouping = new AnonymousElementImplGrouping();
        }
        anonymousElementImplGrouping.setRegistrant(toAdd);
        this.classGroupings.put(name, anonymousElementImplGrouping);
    }
}
