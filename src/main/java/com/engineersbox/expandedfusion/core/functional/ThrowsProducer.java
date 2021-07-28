package com.engineersbox.expandedfusion.core.functional;

@FunctionalInterface
public interface ThrowsProducer<R, E extends Exception> {
    R apply() throws E;
}
