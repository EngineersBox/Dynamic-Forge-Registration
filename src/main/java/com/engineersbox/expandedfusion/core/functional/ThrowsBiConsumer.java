package com.engineersbox.expandedfusion.core.functional;

@FunctionalInterface
public interface ThrowsBiConsumer<T, U, E extends Exception> {
    void apply(T t, U u) throws E;
}