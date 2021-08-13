package com.engineersbox.expandedfusion.register.provider.grouping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ImplClassGroupings<T extends ImplGrouping> {

    public final Map<String, T> classGroupings = new HashMap<>();

    public abstract void collectAnnotatedResources();

    @SuppressWarnings("unchecked")
    public <E> Set<Class<? extends E>> filterClassesBySuperType(final Class<E> superType, final Set<Class<?>> classes) {
        return classes.stream()
            .filter(superType::isAssignableFrom)
            .map(c -> (Class<? extends E>) c)
            .collect(Collectors.toSet());
    }

    public abstract void addIfNotExists(final String name, final Class<?> toAdd);

    public Map<String, T> getClassGroupings() {
        return this.classGroupings;
    }
}
