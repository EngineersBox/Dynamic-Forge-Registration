package com.engineersbox.expandedfusion.register.registry.provider;

import com.engineersbox.expandedfusion.register.registry.annotation.block.BlockProvider;
import com.engineersbox.expandedfusion.register.registry.annotation.block.BlockContainerProvider;
import com.engineersbox.expandedfusion.register.registry.annotation.block.BlockScreenProvider;
import com.engineersbox.expandedfusion.register.registry.annotation.block.BlockTileEntityProvider;

import java.lang.annotation.Annotation;

public enum BlockImplType {
    BASE(new Requirement[]{Requirement.BLOCK}),
    TILE_ENTITY(new Requirement[]{
        Requirement.BLOCK,
        Requirement.TILE_ENTITY,
        Requirement.CONTAINER,
        Requirement.SCREEN
    });

    public enum Requirement {
        BLOCK,
        TILE_ENTITY,
        CONTAINER,
        SCREEN;

        public Class<? extends Annotation> toAnnotationEquivalent() {
            switch (this) {
                case BLOCK:
                    return BlockProvider.class;
                case TILE_ENTITY:
                    return BlockTileEntityProvider.class;
                case CONTAINER:
                    return BlockContainerProvider.class;
                case SCREEN:
                    return BlockScreenProvider.class;
            }
            return null;
        }
    }

    public final Requirement[] requirements;

    BlockImplType(final Requirement[] requirements) {
        this.requirements = requirements;
    }

}
