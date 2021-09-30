package com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang;

import com.engineersbox.expandedfusion.core.registration.annotation.anonymous.AnonymousElementRegistrant;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.ContainerProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidBucket;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.item.ItemProvider;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;

public enum ElementProvider {
    BLOCK(BlockProvider.class, null),
    ITEM(ItemProvider.class, null),
    CONTAINER(ContainerProvider.class, null),
    FLUID(FluidProvider.class, null),
    FLUID_BUCKET(FluidBucket.class, "lang"),
    ANONYMOUS(AnonymousElementRegistrant.class, null);

    public final Class<? extends Annotation> providerClass;
    public final String nestedLangField;

    ElementProvider(final Class<? extends Annotation> providerClass,
                    final String nestedLangField) {
        this.providerClass = providerClass;
        this.nestedLangField = nestedLangField;
    }

    public static Optional<ElementProvider> fromSupplierClass(final Class<?> supplierElementClass) {
        return Arrays.stream(ElementProvider.values())
                .filter((final ElementProvider provider) -> supplierElementClass.isAnnotationPresent(provider.providerClass))
                .findFirst();
    }
}
