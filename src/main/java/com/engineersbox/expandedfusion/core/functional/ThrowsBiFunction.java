package com.engineersbox.expandedfusion.core.functional;

@FunctionalInterface
public interface ThrowsBiFunction<T, U, R> {
    R apply(T t, U u) throws Exception;
}
