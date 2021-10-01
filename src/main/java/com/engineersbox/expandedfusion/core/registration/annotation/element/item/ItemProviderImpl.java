package com.engineersbox.expandedfusion.core.registration.annotation.element.item;

import java.lang.annotation.Annotation;

public class ItemProviderImpl implements ItemProvider {

    private final String name;

    public ItemProviderImpl(final String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return ItemProvider.class;
    }
}
