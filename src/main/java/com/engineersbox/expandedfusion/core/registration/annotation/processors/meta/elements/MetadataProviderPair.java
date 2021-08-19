package com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.elements;

import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.provider.block.BlockProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.provider.block.ContainerProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.provider.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.provider.item.ItemProvider;
import com.engineersbox.expandedfusion.core.registration.exception.annotation.processors.meta.elements.InvalidMetadataDeclaration;

import java.lang.annotation.Annotation;

public class MetadataProviderPair<T extends Annotation> {

    private final LangMetadata langMetadata;
    private final T provider;

    public MetadataProviderPair(final LangMetadata langMetadata,
                                final T provider) {
        this.langMetadata = langMetadata;
        this.provider = provider;
    }

    public T getAnnotation() {
        return this.provider;
    }

    public String getProviderName() {
        if (BlockProvider.class.isAssignableFrom(this.provider.getClass())) {
            return ((BlockProvider) this.provider).name();
        } else if (ItemProvider.class.isAssignableFrom(this.provider.getClass())) {
            return ((ItemProvider) this.provider).name();
        } else if (FluidProvider.class.isAssignableFrom(this.provider.getClass())) {
            return ((FluidProvider) this.provider).name();
        } else if (ContainerProvider.class.isAssignableFrom(this.provider.getClass())) {
            return ((ContainerProvider) this.provider).name();
        }
        throw new InvalidMetadataDeclaration(String.format(
                "Invalid provider annotation %s, not annotated with @ProvidesElement",
                this.provider.getClass().getName()
        ));
    }

    public String getNameMapping() {
        return this.langMetadata.nameMapping();
    }

    public String getTypeName() {
        if (BlockProvider.class.isAssignableFrom(this.provider.getClass())) {
            return "block";
        } else if (ItemProvider.class.isAssignableFrom(this.provider.getClass())) {
            return "item";
        } else if (FluidProvider.class.isAssignableFrom(this.provider.getClass())) {
            return "fluid";
        } else if (ContainerProvider.class.isAssignableFrom(this.provider.getClass())) {
            return "container";
        }
        throw new InvalidMetadataDeclaration(String.format(
                "Invalid provider annotation %s, not annotated with @ProvidesElement",
                this.provider.getClass().getName()
        )); // TODO: Implement exception for this
    }
}
