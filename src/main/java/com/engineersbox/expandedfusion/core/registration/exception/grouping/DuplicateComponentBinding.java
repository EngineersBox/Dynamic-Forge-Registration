package com.engineersbox.expandedfusion.core.registration.exception.grouping;

import java.util.List;
import java.util.Optional;

public abstract class DuplicateComponentBinding extends RuntimeException {
    protected DuplicateComponentBinding(final String type,
                                        final String baseClassName,
                                        final String current,
                                        final String duplicate) {
        super(String.format(
                "Binding for %s registration component [Base: %s] already exists. [Current: %s] [Duplicate: %s]",
                type,
                baseClassName,
                current,
                duplicate
        ));
    }

    protected <T> DuplicateComponentBinding(final String type,
                                            final Class<? extends T> current,
                                            final Class<? extends T> duplicate,
                                            final List<Class<?>> assignableToCheck) {
        this(type, getBaseClassName(current, assignableToCheck), current.getName(), duplicate.getName());
    }

    private static String getBaseClassName(final Class<?> current,
                                           final List<Class<?>> assignableToCheck) {
        final Optional<String> potentialAssignable = assignableToCheck.stream()
                .filter((final Class<?> clazz) -> clazz.isAssignableFrom(current))
                .map(Class::getName)
                .findFirst();
        return potentialAssignable.orElse("Unknown");
    }
}
