package com.engineersbox.expandedfusion.core.registration.provider;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class RegistrationResolver {

    @SuppressWarnings("unchecked")
    public <T> Set<Class<? extends T>> filterClassesBySuperType(final Class<T> superType, final Set<Class<?>> classes) {
        return classes.stream()
                .filter(superType::isAssignableFrom)
                .map(c -> (Class<? extends T>) c)
                .collect(Collectors.toSet());
    }

    public abstract void registerAll();

}
