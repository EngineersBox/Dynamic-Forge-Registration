package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public class AttributedSupplier<T, E> {

    private Supplier<T> elementSupplier;
    private String elementTabGroup;
    private final TagBinding<E> tagBinding;


    public AttributedSupplier() {
        this.tagBinding = new TagBinding<>();
    }

    public AttributedSupplier<T,E> supplier(final Supplier<T> supplier) {
        this.elementSupplier = supplier;
        return this;
    }

    public AttributedSupplier<T,E> tabGroup(final String tabGroup) {
        this.elementTabGroup = tabGroup;
        return this;
    }

    public AttributedSupplier<T,E> tagResource(final ResourceLocation tagResource) {
        this.tagBinding.setTagResource(tagResource);
        return this;
    }

    public AttributedSupplier<T,E> tag(final ITag.INamedTag<E> tag) {
        this.tagBinding.setTag(tag);
        return this;
    }

    public AttributedSupplier<T,E> mirroredTagResource(final ResourceLocation mirroredTagResource) {
        this.tagBinding.setMirroredTagResource(mirroredTagResource);
        return this;
    }

    public AttributedSupplier<T,E> mirroredTag(final ITag.INamedTag<Item> mirroredTag) {
        this.tagBinding.setMirroredTag(mirroredTag);
        return this;
    }

    public Supplier<T> getSupplier() {
        return elementSupplier;
    }

    public String getTabGroup() {
        return elementTabGroup;
    }

    public TagBinding<E> getTagBinding() {
        return tagBinding;
    }
}
