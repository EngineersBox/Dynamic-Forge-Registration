package com.engineersbox.expandedfusion.core.functional;

@FunctionalInterface
public interface TriFunction<R, T, F, E> {

    R apply(final T t, final F f, final E e);

}
