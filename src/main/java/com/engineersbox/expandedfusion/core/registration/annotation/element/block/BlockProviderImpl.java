package com.engineersbox.expandedfusion.core.registration.annotation.element.block;

import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplType;

import java.lang.annotation.Annotation;

public class BlockProviderImpl implements BlockProvider {

    private final String name;
    private final BlockImplType type;
    private final boolean noItem;
    private final BlockProperties[] properties;
    private final String tabGroup;

    public BlockProviderImpl(final String name,
                             final BlockImplType type,
                             final boolean noItem,
                             final BlockProperties[] properties,
                             final String tabGroup) {
        this.name = name;
        this.type = type;
        this.noItem = noItem;
        this.properties = properties;
        this.tabGroup = tabGroup;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public BlockImplType type() {
        return this.type;
    }

    @Override
    public boolean noItem() {
        return this.noItem;
    }

    @Override
    public BlockProperties[] properties() {
        return this.properties;
    }

    @Override
    public String tabGroup() {
        return this.tabGroup;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return BlockProvider.class;
    }
}
