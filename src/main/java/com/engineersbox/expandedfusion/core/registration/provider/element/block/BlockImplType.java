package com.engineersbox.expandedfusion.core.registration.provider.element.block;

import com.engineersbox.expandedfusion.core.registration.annotation.element.block.*;

import java.lang.annotation.Annotation;

public enum BlockImplType {
    STATIC(
            new Requirement[]{Requirement.BLOCK},
            new Requirement[]{}
    ),
    INTERACTIVE_TILE_ENTITY(
            new Requirement[]{
                    Requirement.BLOCK,
                    Requirement.TILE_ENTITY,
                    Requirement.CONTAINER,
                    Requirement.SCREEN
            },
            new Requirement[]{
                    Requirement.RENDERER
            }
    ),
    RENDERED_TILE_ENTITY(
            new Requirement[]{
                    Requirement.BLOCK,
                    Requirement.TILE_ENTITY,
                    Requirement.RENDERER
            },
            new Requirement[]{}
    );

    public enum Requirement {
        BLOCK,
        TILE_ENTITY,
        CONTAINER,
        SCREEN,
        RENDERER;

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
                case RENDERER:
                    return RendererProvider.class;
            }
            return null;
        }
    }

    public final Requirement[] required;
    public final Requirement[] optional;

    BlockImplType(final Requirement[] required, final Requirement[] optional) {
        this.required = required;
        this.optional = optional;
    }

}
