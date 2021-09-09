package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public class AttributedSupplier<T, E> {

    public Supplier<T> elementSupplier;
    public String elementTabGroup;
    public ResourceLocation elementTagResource;
    public ITag.INamedTag<E> elementTag;
    public ResourceLocation elementMirroredTagResource;
    public ITag.INamedTag<Item> elementMirroredTag;

    public AttributedSupplier() {}

    public AttributedSupplier<T,E> supplier(final Supplier<T> supplier) {
        this.elementSupplier = supplier;
        return this;
    }

    public AttributedSupplier<T,E> tabGroup(final String tabGroup) {
        this.elementTabGroup = tabGroup;
        return this;
    }

    public AttributedSupplier<T,E> tagResource(final ResourceLocation tagResource) {
        this.elementTagResource = tagResource;
        return this;
    }

    public AttributedSupplier<T,E> tag(final ITag.INamedTag<E> tag) {
        this.elementTag = tag;
        return this;
    }

    public AttributedSupplier<T,E> mirroredTagResource(final ResourceLocation mirroredTagResource) {
        this.elementMirroredTagResource = mirroredTagResource;
        return this;
    }

    public AttributedSupplier<T,E> mirroredTag(final ITag.INamedTag<Item> mirroredTag) {
        this.elementMirroredTag = mirroredTag;
        return this;
    }
}
