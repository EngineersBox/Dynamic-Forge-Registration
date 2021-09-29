package com.engineersbox.expandedfusion.core.registration.provider.element;

import com.engineersbox.expandedfusion.core.reflection.CheckedInstantiator;
import com.engineersbox.expandedfusion.core.registration.annotation.resolver.RegistrationPhaseHandler;
import com.engineersbox.expandedfusion.core.registration.contexts.provider.ElementRegistryProvider;
import com.engineersbox.expandedfusion.core.registration.exception.provider.element.ProviderElementRegistrationException;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.service.element.BlockDeferredRegistryService;
import com.engineersbox.expandedfusion.core.registration.provider.service.element.ContainerDeferredRegistryService;
import com.engineersbox.expandedfusion.core.registration.provider.service.RegistryService;
import com.engineersbox.expandedfusion.core.registration.provider.service.element.TileEntityDeferredRegistryService;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.BlockRegistryObject;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.*;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.engineersbox.expandedfusion.core.registration.resolver.ResolverPhase;
import com.google.inject.Inject;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

@RegistrationPhaseHandler(ResolverPhase.BLOCK)
public class BlockProviderRegistrationResolver implements RegistrationResolver {

    private final BlockImplClassGrouping implClassGroupings;
    private final BlockDeferredRegistryService blockDeferredRegistryService;
    private final ContainerDeferredRegistryService containerDeferredRegistryService;
    private final TileEntityDeferredRegistryService tileEntityDeferredRegistryService;
    private final ElementRegistryProvider elementRegistryProvider;

    @Inject
    public BlockProviderRegistrationResolver(final ElementRegistryProvider elementRegistryProvider,
                                             final ImplClassGroupings<BlockImplGrouping> implClassGroupings,
                                             final RegistryService<Block> blockDeferredRegistryService,
                                             final RegistryService<Container> containerDeferredRegistryService,
                                             final RegistryService<TileEntity> tileEntityDeferredRegistryService) {
        this.elementRegistryProvider = elementRegistryProvider;
        this.blockDeferredRegistryService = (BlockDeferredRegistryService) blockDeferredRegistryService;
        this.containerDeferredRegistryService = (ContainerDeferredRegistryService) containerDeferredRegistryService;
        this.tileEntityDeferredRegistryService = (TileEntityDeferredRegistryService) tileEntityDeferredRegistryService;
        this.implClassGroupings = (BlockImplClassGrouping) implClassGroupings;
        this.implClassGroupings.collectAnnotatedResources();
    }

    @Override
    public void registerAll() {
        this.implClassGroupings.getClassGroupings().forEach(this::registerBlockProvider);
    }

    private void registerBlockProvider(@Nonnull final String name,
                                       @Nonnull final BlockImplGrouping group) {
        switch (group.getClassification()) {
            case STATIC:
                checkedBlockRegistration(name, group);
                return;
            case INTERACTIVE_TILE_ENTITY:
                registerTileEntity(name, group);
                return;
            case RENDERED_TILE_ENTITY:
                registerRenderedTileEntity(name, group);
        }
    }

    private void registerRenderedTileEntity(@Nonnull final String name,
                                            @Nonnull final BlockImplGrouping group) {
        checkedBlockRegistration(name, group);
        checkedTileEntityRegistration(name, group);
        final Class<? extends TileEntityRenderer<? extends TileEntity>> rendererImpl = group.getRenderer();
        if (rendererImpl == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "No tile entity renderer implementation could be found with associated annotation: %s",
                    name
            ));
        }
        this.elementRegistryProvider.renderersToBeRegistered.put(name, group);
    }

    private void checkedBlockRegistration(@Nonnull final String name,
                                          @Nonnull final BlockImplGrouping group) {
        final BlockProvider blockProvider = group.getBlockProviderAnnotation();
        final Class<? extends Block> blockImpl = group.getBlock();
        if (blockImpl == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "No block implementation could be found with associated annotation: %s",
                    name
            ));
        }
        registerBlock(name, blockProvider, blockImpl);
    }

    private void checkedTileEntityRegistration(@Nonnull final String name,
                                               @Nonnull final BlockImplGrouping group) {
        final Class<? extends TileEntity> tileEntityImpl = group.getTileEntity();
        if (tileEntityImpl == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "No tile entity implementation could be found with associated annotation: %s",
                    name
            ));
        }
        registerTileEntityProvider(name, tileEntityImpl);
    }

    private void registerTileEntity(@Nonnull final String name,
                                    @Nonnull final BlockImplGrouping group) {
        checkedBlockRegistration(name, group);
        checkedTileEntityRegistration(name, group);
        final Class<? extends Container> containerImpl = group.getContainer();
        if (containerImpl == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "No container implementation could be found with associated annotation: %s",
                    name
            ));
        }
        registerContainer(name, containerImpl);
        this.elementRegistryProvider.screensToBeRegistered.put(name, group);
        final RendererProvider rendererProvider = group.getRendererProviderAnnotation();
        if (rendererProvider != null) {
            this.elementRegistryProvider.renderersToBeRegistered.put(name, group);
        }
    }

    private void registerContainer(@Nonnull final String name,
                                   @Nonnull final Class<? extends Container> containerImpl) {
        final ContainerType.IFactory<? extends Container> containerFactory = (final int id, final PlayerInventory playerInventory) ->
                this.instantiateContainerWithIFactoryParams(id, playerInventory, containerImpl);
        this.elementRegistryProvider.containers.put(
            name,
            this.containerDeferredRegistryService.register(
                name,
                containerFactory
            )
        );
    }

    private <T extends Container> T instantiateContainerWithIFactoryParams(final int id,
                                                                           final PlayerInventory playerInventory,
                                                                           @Nonnull final Class<? extends T> containerImpl) {
        try {
            return new CheckedInstantiator<T>()
                    .withImplementation(containerImpl)
                    .withParameterTypes(int.class, PlayerInventory.class)
                    .withParameters(id, playerInventory)
                    .newInstance();
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ProviderElementRegistrationException(e);
        }
    }

    private void registerTileEntityProvider(@Nonnull final String name,
                                            @Nonnull final Class<? extends TileEntity> tileEntityImpl) {
        final BlockRegistryObject<? extends Block> blockRegistryObject = this.elementRegistryProvider.blocks.get(name);
        if (blockRegistryObject == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "Block registry has no entry for element: %s",
                    name
            ));
        }
        this.elementRegistryProvider.tileEntities.put(
                name,
                this.tileEntityDeferredRegistryService.register(name, () -> this.<TileEntity>instantiateWithDefaultConstructor(tileEntityImpl))
        );
    }

    private void registerBlock(@Nonnull final String name,
                               @Nonnull final BlockProvider blockProvider,
                               @Nonnull final Class<? extends Block> blockImpl) {
        final BlockProperties[] properties = blockProvider.properties();
        Supplier<Block> blockSupplier;
        if (properties.length < 1) {
            blockSupplier = () -> this.<Block>instantiateWithDefaultConstructor(blockImpl);
        } else {
            blockSupplier = () -> new Block(this.blockDeferredRegistryService.assemblePropertiesFromAnnotation(properties[0]));
        }
        BlockRegistryObject<Block> blockRegistryObject;
        if (blockProvider.noItem()) {
            blockRegistryObject = this.blockDeferredRegistryService.registerNoItem(
                    name,
                    blockSupplier
            );
        } else {
            blockRegistryObject = this.blockDeferredRegistryService.register(
                    name,
                    blockSupplier,
                    blockProvider.tabGroup()
            );
        }
        this.elementRegistryProvider.blocks.put(
                name,
                blockRegistryObject
        );
    }
}
