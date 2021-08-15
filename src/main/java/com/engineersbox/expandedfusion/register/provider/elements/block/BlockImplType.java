package com.engineersbox.expandedfusion.register.provider.elements.block;

import com.engineersbox.expandedfusion.register.annotation.block.BlockProvider;
import com.engineersbox.expandedfusion.register.annotation.block.ContainerProvider;
import com.engineersbox.expandedfusion.register.annotation.block.ScreenProvider;
import com.engineersbox.expandedfusion.register.annotation.block.TileEntityProvider;

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
                    return TileEntityProvider.class;
                case CONTAINER:
                    return ContainerProvider.class;
                case SCREEN:
                    return ScreenProvider.class;
            }
            return null;
        }
    }

    public final Requirement[] requirements;

    BlockImplType(final Requirement[] requirements) {
        this.requirements = requirements;
    }

}
