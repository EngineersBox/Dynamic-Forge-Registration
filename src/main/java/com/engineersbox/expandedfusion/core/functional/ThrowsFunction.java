package com.engineersbox.expandedfusion.core.functional;

@FunctionalInterface
public interface ThrowsFunction<T, R, E extends Exception> {
    R apply(T t) throws E;
}
