package com.engineersbox.expandedfusion.core.registration.annotation.element.fluid;

import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProperties;

import java.lang.annotation.Annotation;

public class FluidProviderImpl implements FluidProvider {

    private final String name;
    private final boolean gaseous;
    private final BlockProperties[] blockProperties;

    public FluidProviderImpl(final String name,
                             final boolean gaseous,
                             final BlockProperties[] blockProperties) {
        this.name = name;
        this.gaseous = gaseous;
        this.blockProperties = blockProperties;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public boolean gaseous() {
        return this.gaseous;
    }

    @Override
    public BlockProperties[] blockProperties() {
        return this.blockProperties;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return FluidProvider.class;
    }
}
