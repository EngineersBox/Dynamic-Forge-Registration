package com.engineersbox.expandedfusion.core.functional;

@FunctionalInterface
public interface ThrowsTriConsumer<T, U> {
    void apply(T t, U u, Object v) throws Exception;
}
