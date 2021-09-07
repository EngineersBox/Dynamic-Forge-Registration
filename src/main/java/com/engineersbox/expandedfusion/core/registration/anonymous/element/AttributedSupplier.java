package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public class AttributedSupplier<T> {

    public final Supplier<T> supplier;
    public final String tabGroup;
    public final ResourceLocation tagResource;
    public boolean registerBlockTagAsItemTag = false;

    public AttributedSupplier(final Supplier<T> supplier,
                              final String tabGroup,
                              final String tagNamespace,
                              final String tagPath) {
        this.supplier = supplier;
        this.tabGroup = tabGroup;
        this.tagResource = new ResourceLocation(tagNamespace, tagPath);
    }

    public AttributedSupplier(final Supplier<T> supplier,
                              final String tabGroup,
                              final String tagPath) {
        this(supplier, tabGroup, "forge", tagPath);
    }

    public AttributedSupplier(final Supplier<T> supplier,
                              final String tabGroup) {
        this.supplier = supplier;
        this.tabGroup = tabGroup;
        this.tagResource = null;
    }

    public AttributedSupplier(final Supplier<T> supplier,
                              final String tabGroup,
                              final ITag.INamedTag<T> tag) {
        this.supplier = supplier;
        this.tabGroup = tabGroup;
        this.tagResource = tag.getName();
    }

    public AttributedSupplier<T> blockTagAsItemTag(final boolean registerBlockTagAsItemTag) {
        this.registerBlockTagAsItemTag = registerBlockTagAsItemTag;
        return this;
    }
}
