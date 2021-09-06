package com.engineersbox.expandedfusion.core.registration.exception.grouping.anonymous;

import com.engineersbox.expandedfusion.core.registration.exception.grouping.DuplicateComponentBinding;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class DuplicateAnonymousElementProviderBinding extends DuplicateComponentBinding {

    private static final List<Class<?>> ASSIGNABLE_TO_CHECK = ImmutableList.of(
            Enum.class,
            Object.class
    );

    public <T> DuplicateAnonymousElementProviderBinding(final Class<? extends T> current,
                                                        final Class<? extends T> duplicate) {
        super("anonymous", current, duplicate, ASSIGNABLE_TO_CHECK);
    }
}