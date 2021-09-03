package com.engineersbox.expandedfusion.core.registration.provider.element.block;

import com.engineersbox.expandedfusion.core.reflection.CheckedInstantiator;
import com.engineersbox.expandedfusion.core.registration.exception.provider.element.ProviderElementRegistrationException;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.shim.element.BlockDeferredRegistryShim;
import com.engineersbox.expandedfusion.core.registration.provider.shim.element.ContainerDeferredRegistryShim;
import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShim;
import com.engineersbox.expandedfusion.core.registration.provider.shim.element.TileEntityDeferredRegistryShim;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.BlockRegistryObject;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.*;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.RegistryProvider;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.google.inject.Inject;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

public class BlockProviderRegistrationResolver extends RegistrationResolver {

    private final BlockImplClassGrouping implClassGroupings;
    private final BlockDeferredRegistryShim blockDeferredRegistryShim;
    private final ContainerDeferredRegistryShim containerDeferredRegistryShim;
    private final TileEntityDeferredRegistryShim tileEntityDeferredRegistryShim;
    private final RegistryProvider registryProvider;

    @Inject
    public BlockProviderRegistrationResolver(final RegistryProvider registryProvider,
                                             final ImplClassGroupings<BlockImplGrouping> implClassGroupings,
                                             final RegistryShim<Block> blockDeferredRegistryShim,
                                             final RegistryShim<Container> containerDeferredRegistryShim,
                                             final RegistryShim<TileEntity> tileEntityDeferredRegistryShim) {
        this.registryProvider = registryProvider;
        this.blockDeferredRegistryShim = (BlockDeferredRegistryShim) blockDeferredRegistryShim;
        this.containerDeferredRegistryShim = (ContainerDeferredRegistryShim) containerDeferredRegistryShim;
        this.tileEntityDeferredRegistryShim = (TileEntityDeferredRegistryShim) tileEntityDeferredRegistryShim;
        this.implClassGroupings = (BlockImplClassGrouping) implClassGroupings;
        this.implClassGroupings.collectAnnotatedResources();
    }

    @Override
    public void registerAll() {
        this.implClassGroupings.getClassGroupings().forEach(this::registerProviderAnnotatedBlock);
    }

    private void registerProviderAnnotatedBlock(@Nonnull final String name,
                                                @Nonnull final BlockImplGrouping group) {
        final BlockProvider blockProvider = group.getBlockProviderAnnotation();
        if (blockProvider == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "Block implementation %s has no plausible annotation",
                    name
            ));
        }
        if (!blockProvider.name().equals(name)) {
            throw new ProviderElementRegistrationException(String.format(
                    "Mismatched provider element name against annotation: %s != %s",
                    name,
                    blockProvider.name()
            ));
        }
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
        final RendererProvider rendererProvider = group.getRendererProviderAnnotation();
        if (rendererProvider == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "Tile entity renderer implementation %s has no plausible annotation",
                    name
            ));
        }
        if (!rendererProvider.name().equals(name)) {
            throw new ProviderElementRegistrationException(String.format(
                    "Mismatched provider element name against annotation: %s != %s",
                    name,
                    rendererProvider.name()
            ));
        }
        final Class<? extends TileEntityRenderer<? extends TileEntity>> rendererImpl = group.getRenderer();
        if (rendererImpl == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "No tile entity renderer implementation could be found with associated annotation: %s",
                    name
            ));
        }
        this.registryProvider.renderersToBeRegistered.put(name, group);
    }

    private void checkedBlockRegistration(@Nonnull final String name,
                                          @Nonnull final BlockImplGrouping group) {
        final BlockProvider blockProvider = group.getBlockProviderAnnotation();
        if (blockProvider == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "Block implementation %s has no plausible annotation",
                    name
            ));
        }
        if (!blockProvider.name().equals(name)) {
            throw new ProviderElementRegistrationException(String.format(
                    "Mismatched provider element name against annotation: %s != %s",
                    name,
                    blockProvider.name()
            ));
        }
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
        final TileEntityProvider tileEntityProvider = group.getTileEntityProviderAnnotation();
        if (tileEntityProvider == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "Tile entity implementation %s has no plausible annotation",
                    name
            ));
        }
        if (!tileEntityProvider.name().equals(name)) {
            throw new ProviderElementRegistrationException(String.format(
                    "Mismatched provider element name against annotation: %s != %s",
                    name,
                    tileEntityProvider.name()
            ));
        }
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
        final ContainerProvider containerProvider = group.getContainerProviderAnnotation();
        if (containerProvider == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "Container implementation %s has no plausible annotation",
                    name
            ));
        }
        if (!containerProvider.name().equals(name)) {
            throw new ProviderElementRegistrationException(String.format(
                    "Mismatched provider element name against annotation: %s != %s",
                    name,
                    containerProvider.name()
            ));
        }
        final Class<? extends Container> containerImpl = group.getContainer();
        if (containerImpl == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "No container implementation could be found with associated annotation: %s",
                    name
            ));
        }
        registerContainer(name, containerImpl);
        this.registryProvider.screensToBeRegistered.put(name, group);
        final RendererProvider rendererProvider = group.getRendererProviderAnnotation();
        if (rendererProvider != null) {
            this.registryProvider.renderersToBeRegistered.put(name, group);
        }
    }

    private void registerContainer(@Nonnull final String name,
                                   @Nonnull final Class<? extends Container> containerImpl) {
        final ContainerType.IFactory<? extends Container> containerFactory = (final int id, final PlayerInventory playerInventory) ->
                this.instantiateContainerWithIFactoryParams(id, playerInventory, containerImpl);
        this.registryProvider.containers.put(
            name,
            this.containerDeferredRegistryShim.register(
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
        final BlockRegistryObject<? extends Block> blockRegistryObject = this.registryProvider.blocks.get(name);
        if (blockRegistryObject == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "Block registry has no entry for element: %s",
                    name
            ));
        }
        this.registryProvider.tileEntities.put(
                name,
                this.tileEntityDeferredRegistryShim.register(name, () -> super.<TileEntity>instantiateWithDefaultConstructor(tileEntityImpl))
        );
    }

    private void registerBlock(@Nonnull final String name,
                               @Nonnull final BlockProvider blockProvider,
                               @Nonnull final Class<? extends Block> blockImpl) {
        final BaseBlockProperties[] properties = blockProvider.properties();
        Supplier<Block> blockSupplier;
        if (properties.length < 1) {
            blockSupplier = () -> super.<Block>instantiateWithDefaultConstructor(blockImpl);
        } else {
            blockSupplier = () -> new Block(createBlockProperties(properties[0]));
        }
        this.registryProvider.blocks.put(
                name,
                this.blockDeferredRegistryShim.register(
                        name,
                        blockSupplier,
                        blockProvider.tabGroup()
                )
        );
    }

    private Block.Properties createBlockProperties(final BaseBlockProperties baseProps) {
        return Block.Properties.create(getMaterialForString(baseProps.material()));
    }

    private Material getMaterialForString(final String material) {
        final Field[] fields = Material.class.getDeclaredFields();
        final Optional<Field> matValue = Arrays.stream(fields)
                .filter(f -> f.getType().isAssignableFrom(Material.class) && f.getName().equals(material))
                .findFirst();
        if (matValue.isPresent()) {
            try {
                return (Material) matValue.get().get(Material.AIR);
            } catch (IllegalAccessException e) {
                throw new ProviderElementRegistrationException(e);
            }
        }
        return Material.AIR;
    }
}
