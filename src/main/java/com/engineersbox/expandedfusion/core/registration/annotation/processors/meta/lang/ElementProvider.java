package com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.lang;

import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.ContainerProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidBucket;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.item.ItemProvider;

import java.lang.annotation.Annotation;

public enum ElementProvider {
    BLOCK(BlockProvider.class, null),
    ITEM(ItemProvider.class, null),
    CONTAINER(ContainerProvider.class, null),
    FLUID(FluidProvider.class, null),
    FLUID_BUCKET(FluidBucket.class, "lang");

    public final Class<? extends Annotation> providerClass;
    public final String nestedLangField;

    ElementProvider(final Class<? extends Annotation> providerClass,
                    final String nestedLangField) {
        this.providerClass = providerClass;
        this.nestedLangField = nestedLangField;
    }
}
