package com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block;

import com.engineersbox.expandedfusion.core.registration.annotation.element.block.*;
import com.engineersbox.expandedfusion.core.registration.exception.grouping.element.DuplicateBlockComponentBinding;
import com.engineersbox.expandedfusion.core.registration.exception.grouping.element.ImplementationGroupingException;
import com.engineersbox.expandedfusion.core.registration.provider.element.block.BlockImplType;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplGrouping;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class BlockImplGrouping implements ImplGrouping {

    public static final Logger LOGGER = LogManager.getLogger(BlockImplGrouping.class);

    private Class<? extends TileEntity> tileEntity;
    private Class<? extends Block> block;
    private Class<? extends Container> container;
    private Class<? extends ContainerScreen<? extends Container>> screen;
    private Class<? extends TileEntityRenderer<? extends TileEntity>> renderer;

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

    private void logMissingOptional(final Class<?> component,
                                    final BlockImplType.Requirement requirement) {
        LOGGER.debug(
                "Optional provider component {} not present for requirement {}",
                component.getName(),
                requirement.name()
        );
    }

    public List<Class<? extends Annotation>> hasRequirements(final BlockImplType blockImplType) {
        final List<Class<? extends Annotation>> missing = new ArrayList<>();
        for (final BlockImplType.Requirement r : blockImplType.required) {
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
                    continue;
                case SCREEN:
                    if (FMLEnvironment.dist == Dist.CLIENT && this.screen == null) {
                        missing.add(r.toAnnotationEquivalent());
                    }
                    continue;
                case RENDERER:
                    if (FMLEnvironment.dist == Dist.CLIENT && this.renderer == null) {
                        missing.add(r.toAnnotationEquivalent());
                    }
                    continue;
                default:
                    throw new ImplementationGroupingException(String.format(
                            "Unknown requirement type: %s",
                            r
                    ));
            }
        }
        for (final BlockImplType.Requirement r : blockImplType.optional) {
            switch (r) {
                case BLOCK:
                    if (this.block == null) {
                        logMissingOptional(Block.class, r);
                    }
                    continue;
                case TILE_ENTITY:
                    if (this.tileEntity == null) {
                        logMissingOptional(TileEntity.class, r);
                    }
                    continue;
                case CONTAINER:
                    if (this.container == null) {
                        logMissingOptional(Container.class, r);
                    }
                    continue;
                case SCREEN:
                    if (FMLEnvironment.dist == Dist.CLIENT && this.screen == null) {
                        logMissingOptional(Screen.class, r);
                    }
                    continue;
                case RENDERER:
                    if (FMLEnvironment.dist == Dist.CLIENT && this.renderer == null) {
                        logMissingOptional(TileEntityRenderer.class, r);
                    }
                    continue;
                default:
                    throw new ImplementationGroupingException(String.format(
                            "Unknown requirement type: %s",
                            r
                    ));
            }
        }
        return missing;
    }
}
