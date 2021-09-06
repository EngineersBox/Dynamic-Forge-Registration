package com.engineersbox.expandedfusion.core.registration.provider.grouping;

import java.util.HashMap;
import java.util.Map;

public abstract class ImplClassGroupings<T extends ImplGrouping> {

    public final Map<String, T> classGroupings = new HashMap<>();

    public abstract void collectAnnotatedResources();

    public abstract void addIfNotExists(final String name, final Class<?> toAdd);

    public Map<String, T> getClassGroupings() {
        return this.classGroupings;
    }
}
