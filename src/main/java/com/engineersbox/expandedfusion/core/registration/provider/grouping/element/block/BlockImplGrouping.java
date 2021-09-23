package com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block;

import com.engineersbox.expandedfusion.core.registration.annotation.element.block.*;
import com.engineersbox.expandedfusion.core.registration.exception.grouping.element.block.DuplicateBlockComponentBinding;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplGrouping;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockImplGrouping implements ImplGrouping {

    private static final Logger LOGGER = LogManager.getLogger(BlockImplGrouping.class);

    private Class<? extends TileEntity> tileEntity;
    private Class<? extends Block> block;
    private Class<? extends Container> container;
    private Class<? extends ContainerScreen<? extends Container>> screen;
    private Class<? extends TileEntityRenderer<? extends TileEntity>> renderer;

    private BlockImplType classification;

    public BlockImplType getClassification() {
        return this.classification;
    }

    public void setClassification(final BlockImplType classification) {
        this.classification = classification;
    }

    @Override
    public List<Class<?>> getAllClasses() {
        return Stream.of(
            this.tileEntity,
            this.block,
            this.container,
            this.screen,
            this.renderer
        ).filter(Objects::nonNull)
        .collect(Collectors.toList());
    }

    @Override
    public String getCommonIdentifier() {
        return this.getBlockProviderAnnotation().name();
    }

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

    public Class<? extends TileEntityRenderer<? extends TileEntity>> getRenderer() {
        return renderer;
    }

    public RendererProvider getRendererProviderAnnotation() {
        if (this.renderer == null) {
            return null;
        }
        return this.renderer.getAnnotation(RendererProvider.class);
    }

    public void setRenderer(final Class<? extends TileEntityRenderer<? extends TileEntity>> renderer) throws DuplicateBlockComponentBinding {
        if (this.renderer != null) {
            throw new DuplicateBlockComponentBinding(this.renderer, renderer);
        }
        this.renderer = renderer;
    }
}
