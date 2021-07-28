package com.engineersbox.expandedfusion.core.functional;

@FunctionalInterface
public interface ThrowsConsumer<T, E extends Exception> {
    void apply(T t) throws E;
}
