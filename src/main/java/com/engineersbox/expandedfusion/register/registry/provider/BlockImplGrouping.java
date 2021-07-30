package com.engineersbox.expandedfusion.register.registry.provider;

import com.engineersbox.expandedfusion.register.registry.annotation.block.BlockProvider;
import com.engineersbox.expandedfusion.register.registry.annotation.block.BlockContainerProvider;
import com.engineersbox.expandedfusion.register.registry.annotation.block.BlockTileEntityProvider;
import com.engineersbox.expandedfusion.register.registry.exception.DuplicateBlockComponentBinding;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class BlockImplGrouping {
    private Class<? extends TileEntity> tileEntity;
    private Class<? extends Block> block;
    private Class<? extends Container> container;

    public Class<? extends TileEntity> getTileEntity() {
        return tileEntity;
    }

    public BlockTileEntityProvider getTileEntityProviderAnnotation() {
        if (this.tileEntity == null) {
            return null;
        }
        return this.tileEntity.getAnnotation(BlockTileEntityProvider.class);
    }

    public void setTileEntity(final Class<? extends TileEntity> tileEntity) throws DuplicateBlockComponentBinding {
        if (this.tileEntity != null) {
            throw new DuplicateBlockComponentBinding(this.tileEntity, tileEntity);
        }
        this.tileEntity = tileEntity;
    }

    public Class<? extends Block> getBlock() {
        return block;
    }

    public BlockProvider getBlockProviderAnnotation() {
        if (this.block == null) {
            return null;
        }
        return this.block.getAnnotation(BlockProvider.class);
    }

    public void setBlock(final Class<? extends Block> block) throws DuplicateBlockComponentBinding {
        if (this.block != null) {
            throw new DuplicateBlockComponentBinding(this.block, block);
        }
        this.block = block;
    }

    public Class<? extends Container> getContainer() {
        return container;
    }

    public BlockContainerProvider getContainerProviderAnnotation() {
        if (this.container == null) {
            return null;
        }
        return this.container.getAnnotation(BlockContainerProvider.class);
    }

    public void setContainer(final Class<? extends Container> container) throws DuplicateBlockComponentBinding {
        if (this.container != null) {
            throw new DuplicateBlockComponentBinding(this.container, container);
        }
        this.container = container;
    }

    public List<Class<? extends Annotation>> hasRequirements(final BlockImplType blockImplType) {
        final List<Class<? extends Annotation>> missing = new ArrayList<>();
        for (final BlockImplType.Requirement r : blockImplType.requirements) {
            switch (r) {
                case BLOCK:
                    if (this.block == null) {
                        missing.add(r.toAnnotationEquivalent());
                    }
                    continue;
                case TILE_ENTITY:
                    if (this.tileEntity == null) {
                        missing.add(r.toAnnotationEquivalent());
                    }
                    continue;
                case CONTAINER:
                    if (this.container == null) {
                        missing.add(r.toAnnotationEquivalent());
                    }
            }
        }
        return missing;
    }
}
