package com.engineersbox.expandedfusion.register.registry.provider;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class RegistrationResolver {

    @SuppressWarnings("unchecked")
    <T> Set<Class<? extends T>> filterClassesBySuperType(final Class<T> superType, final Set<Class<?>> classes) {
        return classes.stream()
                .filter(superType::isAssignableFrom)
                .map(c -> (Class<? extends T>) c)
                .collect(Collectors.toSet());
    }

    abstract void registerAll();

}
