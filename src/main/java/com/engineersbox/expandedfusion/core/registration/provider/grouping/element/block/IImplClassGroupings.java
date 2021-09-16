package com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block;

import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplGrouping;

import java.util.Map;

public interface IImplClassGroupings<T extends ImplGrouping> {
    void collectAnnotatedResources();

    void addIfNotExists(final String name, final Class<?> toAdd);

    Map<String, T> getClassGroupings();
}
