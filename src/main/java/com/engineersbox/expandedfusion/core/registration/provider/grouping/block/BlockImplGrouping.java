package com.engineersbox.expandedfusion.core.registration.provider.grouping.block;

import com.engineersbox.expandedfusion.core.registration.annotation.block.BlockProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.block.ContainerProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.block.ScreenProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.block.TileEntityProvider;
import com.engineersbox.expandedfusion.core.registration.exception.grouping.DuplicateBlockComponentBinding;
import com.engineersbox.expandedfusion.core.registration.provider.elements.block.BlockImplType;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplGrouping;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class BlockImplGrouping implements ImplGrouping {
    private Class<? extends TileEntity> tileEntity;
    private Class<? extends Block> block;
    private Class<? extends Container> container;
    private Class<? extends ContainerScreen<? extends Container>> screen;

    public Class<? extends TileEntity> getTileEntity() {
        return this.tileEntity;
    }

    public TileEntityProvider getTileEntityProviderAnnotation() {
        if (this.tileEntity == null) {
            return null;
        }
        return this.tileEntity.getAnnotation(TileEntityProvider.class);
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

    public ContainerProvider getContainerProviderAnnotation() {
        if (this.container == null) {
            return null;
        }
        return this.container.getAnnotation(ContainerProvider.class);
    }

    public void setContainer(final Class<? extends Container> container) throws DuplicateBlockComponentBinding {
        if (this.container != null) {
            throw new DuplicateBlockComponentBinding(this.container, container);
        }
        this.container = container;
    }

    public Class<? extends ContainerScreen<? extends Container>> getScreen() {
        return screen;
    }

    public ScreenProvider getScreenProviderAnnotation() {
        if (this.screen == null) {
            return null;
        }
        return this.screen.getAnnotation(ScreenProvider.class);
    }

    public void setScreen(final Class<? extends ContainerScreen<? extends Container>> screen) throws DuplicateBlockComponentBinding {
        if (this.screen != null) {
            throw new DuplicateBlockComponentBinding(this.screen, screen);
        }
        this.screen = screen;
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
