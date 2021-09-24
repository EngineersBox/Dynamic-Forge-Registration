package com.engineersbox.expandedfusion.core.reflection;

import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionClassFilter {

    private ReflectionClassFilter() {
        throw new IllegalStateException("Utility Class");
    }

    @SuppressWarnings("unchecked")
    public static <E> Set<Class<? extends E>> filterClassesBySuperType(final Class<E> superType, final Set<Class<?>> classes) {
        return classes.stream()
                .filter(superType::isAssignableFrom)
                .map(c -> (Class<? extends E>) c)
                .collect(Collectors.toSet());
    }

}
