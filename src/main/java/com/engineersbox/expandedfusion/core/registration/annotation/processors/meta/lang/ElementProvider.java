package com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.lang;

import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.ContainerProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.item.ItemProvider;

import java.lang.annotation.Annotation;

public enum ElementProvider {
    BLOCK(BlockProvider.class),
    ITEM(ItemProvider.class),
    CONTAINER(ContainerProvider.class),
    FLUID(FluidProvider.class);

    public final Class<? extends Annotation> providerClass;

    ElementProvider(final Class<? extends Annotation> providerClass) {
        this.providerClass = providerClass;
    }
}
