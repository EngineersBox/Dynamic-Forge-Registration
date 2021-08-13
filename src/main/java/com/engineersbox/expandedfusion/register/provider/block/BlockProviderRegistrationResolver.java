package com.engineersbox.expandedfusion.register.provider.block;

import com.engineersbox.expandedfusion.core.common.machine.AbstractMachineBlock;
import com.engineersbox.expandedfusion.register.ModBlocks;
import com.engineersbox.expandedfusion.register.ModContainers;
import com.engineersbox.expandedfusion.register.ModTileEntities;
import com.engineersbox.expandedfusion.register.Registration;
import com.engineersbox.expandedfusion.register.registry.BlockRegistryObject;
import com.engineersbox.expandedfusion.register.annotation.block.*;
import com.engineersbox.expandedfusion.register.contexts.RegistryInjectionContext;
import com.engineersbox.expandedfusion.register.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.register.provider.RegistryProvider;
import com.engineersbox.expandedfusion.register.provider.grouping.block.BlockImplGrouping;
import com.engineersbox.expandedfusion.register.provider.grouping.ImplClassGroupings;
import com.google.inject.Inject;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.reflections.ReflectionUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

public class BlockProviderRegistrationResolver extends RegistrationResolver {

    private final ImplClassGroupings<BlockImplGrouping> implClassGroupings;
    final RegistryProvider registryProvider;

    @Inject
    public BlockProviderRegistrationResolver(final RegistryProvider registryProvider,
                                             final ImplClassGroupings<BlockImplGrouping> implClassGroupings) {
        this.registryProvider = registryProvider;
        this.implClassGroupings = implClassGroupings;
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
            throw new RuntimeException(); // TODO: Implement an exception for this
        }
        final Class<? extends Block> blockImpl = group.getBlock();
        if (blockImpl == null) {
            throw new RuntimeException(); // TODO: Implement an exception for this
        }
        switch (blockProvider.type()) {
            case BASE:
                registerBlock(name, blockProvider, blockImpl);
                return;
            case TILE_ENTITY:
                registerTileEntity(name, group);
        }
    }

    private void registerTileEntity(@Nonnull final String name,
                                    @Nonnull final BlockImplGrouping group) {
        final BlockProvider blockProvider = group.getBlockProviderAnnotation();
        if (blockProvider == null) {
            throw new RuntimeException(); // TODO: Implement an exception for this
        }
        final Class<? extends Block> blockImpl = group.getBlock();
        if (blockImpl == null) {
            throw new RuntimeException(); // TODO: Implement an exception for this
        }
        registerBlock(name, blockProvider, blockImpl);
        final TileEntityProvider tileEntityProvider = group.getTileEntityProviderAnnotation();
        if (tileEntityProvider == null) {
            throw new RuntimeException(); // TODO: Implement an exception for this
        }
        final Class<? extends TileEntity> tileEntityImpl = group.getTileEntity();
        if (tileEntityImpl == null) {
            throw new RuntimeException(); // TODO: Implement an exception for this
        }
        registerTileEntity(name, tileEntityImpl);
        final ContainerProvider containerProvider = group.getContainerProviderAnnotation();
        if (containerProvider == null) {
            throw new RuntimeException(); // TODO: Implement an exception for this
        }
        final Class<? extends Container> containerImpl = group.getContainer();
        if (containerImpl == null) {
            throw new RuntimeException(); // TODO: Implement an exception for this
        }
        registerContainer(name, containerImpl);
        this.registryProvider.screensToBeRegistered.put(name, group);
    }

    @SuppressWarnings("unchecked")
    @OnlyIn(Dist.CLIENT)
    public static <T extends Container, U extends Screen & IHasContainer<T>> void registerScreens(final FMLClientSetupEvent event) {
        RegistryInjectionContext.getScreensToBeRegistered().forEach((final String name, final BlockImplGrouping group) -> {
            final ContainerType<T> containerType = (ContainerType<T>) RegistryInjectionContext.getContainerType(name);
            final Class<? extends ContainerScreen<? extends T>> screen = (Class<? extends ContainerScreen<? extends T>>) group.getScreen();
            if (screen == null) {
                throw new RuntimeException(); // TODO: Implement an exception for this
            }
            final ScreenManager.IScreenFactory<T, U> factory = (final T container,
                                                                final PlayerInventory playerInventory,
                                                                final ITextComponent titleIn) ->
                (U) instantiateScreenWithIScreenFactoryParams(container, playerInventory, titleIn, screen);
            ScreenManager.registerFactory(containerType, factory);
        });
    }

    private void registerContainer(@Nonnull final String name,
                                   @Nonnull final Class<? extends Container> containerImpl) {
        final ContainerType.IFactory<? extends Container> containerFactory = (final int id, final PlayerInventory playerInventory) ->
                this.instantiateContainerWithIFactoryParams(id, playerInventory, containerImpl);
        this.registryProvider.containers.put(
            name,
            ModContainers.register(
                name,
                containerFactory
            )
        );
    }

    @SuppressWarnings("unchecked")
    private static <T extends Container> ContainerScreen<T>
    instantiateScreenWithIScreenFactoryParams(final T container,
                                              final PlayerInventory playerInventory,
                                              final ITextComponent titleIn,
                                              final Class<? extends ContainerScreen<? extends T>> screen) {
        final Set<Constructor> constructors = ReflectionUtils.getConstructors(
                screen,
                (c) -> {
                    final Class<?>[] paramTypes = c.getParameterTypes();
                    if (paramTypes.length != 3) {
                        return false;
                    }
                    return Container.class.isAssignableFrom(paramTypes[0])
                            && PlayerInventory.class.isAssignableFrom(paramTypes[1])
                            && ITextComponent.class.isAssignableFrom(paramTypes[2]);
                }
        );
        if (constructors.size() < 1) {
            throw new RuntimeException(); // TODO: Implement an exception for this
        }
        try {
            return (ContainerScreen<T>) new ArrayList<>(constructors).get(0).newInstance(container, playerInventory, titleIn);
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e); // TODO: Implement an exception for this
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Container> T instantiateContainerWithIFactoryParams(final int id,
                                                                           final PlayerInventory playerInventory,
                                                                           @Nonnull final Class<? extends Container> containerImpl) {
        final Set<Constructor> constructors = ReflectionUtils.getConstructors(
                containerImpl,
                (c) -> {
                    final Class<?>[] paramTypes = c.getParameterTypes();
                    if (paramTypes.length != 2) {
                        return false;
                    }
                    return int.class.isAssignableFrom(paramTypes[0])
                            && PlayerInventory.class.isAssignableFrom(paramTypes[1]);
                }
        );
        if (constructors.size() < 1) {
            throw new RuntimeException(); // TODO: Implement an exception for this
        }
        try {
            return (T) new ArrayList<>(constructors).get(0).newInstance(id, playerInventory);
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e); // TODO: Implement an exception for this
        }
    }

    private void registerTileEntity(@Nonnull final String name,
                                    @Nonnull final Class<? extends TileEntity> tileEntityImpl) {
        final BlockRegistryObject<? extends Block> blockRegistryObject = this.registryProvider.blocks.get(name);
        if (blockRegistryObject == null) {
            throw new RuntimeException(); // TODO: Implement an exception for this
        }
        this.registryProvider.tileEntities.put(
                name,
                ModTileEntities.register(name, () -> this.<TileEntity>instantiateWithDefaultConstructor(tileEntityImpl))
        );
    }

    private void registerBlock(@Nonnull final String name,
                               @Nonnull final BlockProvider blockProvider,
                               @Nonnull final Class<? extends Block> blockImpl) {
        final BaseBlockProperties[] properties = blockProvider.properties();
        Supplier<Block> blockSupplier;
        if (properties.length < 1) {
            blockSupplier = () -> this.<Block>instantiateWithDefaultConstructor(blockImpl);
        } else {
            blockSupplier = () -> new Block(createBlockProperties(properties[0]));
        }
        this.registryProvider.blocks.put(name, ModBlocks.register(name, blockSupplier));
    }

    @SuppressWarnings("unchecked")
    private <T> T instantiateWithDefaultConstructor(@Nonnull final Class<? extends T> impl) {
        final Set<Constructor> constructors = ReflectionUtils.getConstructors(
                impl,
                (c) -> c.getParameterCount() < 1
        );
        if (constructors.size() < 1) {
            throw new RuntimeException(); // TODO: Implement an exception for this
        }
        try {
            return (T) new ArrayList<>(constructors).get(0).newInstance();
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e); // TODO: Implement an exception for this
        }
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
                throw new RuntimeException(e); // TODO: Implement exception for this
            }
        }
        return Material.AIR;
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderTypes(final FMLClientSetupEvent event) {
        Registration.getBlocks(AbstractMachineBlock.class).forEach(block ->
                RenderTypeLookup.setRenderLayer(block, RenderType.getTranslucent()));
    }
}
