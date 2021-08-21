package com.engineersbox.expandedfusion.core.registration.contexts;

import com.engineersbox.expandedfusion.core.registration.exception.contexts.RegistryInjectionException;
import com.engineersbox.expandedfusion.core.registration.registryObject.BlockRegistryObject;
import com.engineersbox.expandedfusion.core.registration.registryObject.ItemRegistryObject;
import com.engineersbox.expandedfusion.core.registration.provider.RegistryProvider;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.block.BlockImplGrouping;
import com.google.inject.Guice;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.Map;

public abstract class RegistryInjectionContext {

    private RegistryInjectionContext() {
        throw new IllegalStateException("Utility Class");
    }

    private static final RegistryProvider registryProvider = Guice.createInjector(new ProviderModule()).getInstance(RegistryProvider.class);

    public static TileEntityType<? extends TileEntity> getTileEntityType(final String provider_name) {
        final TileEntityType<? extends TileEntity> tileEntityType = registryProvider.tileEntities.get(provider_name).asTileEntityType();
        if (tileEntityType == null) {
            throw new RegistryInjectionException(String.format(
                    "Tile entity could not be found for provider name: %s",
                    provider_name
            ));
        }
        return tileEntityType;
    }

    public static BlockRegistryObject<? extends Block> getBlockRegistryObject(final String provider_name) {
        final BlockRegistryObject<? extends Block> registryObject = registryProvider.blocks.get(provider_name);
        if (registryObject == null) {
            throw new RegistryInjectionException(String.format(
                    "Block could not be found for provider name: %s",
                    provider_name
            ));
        }
        return registryObject;
    }

    public static ContainerType<? extends Container> getContainerType(final String provider_name) {
        final ContainerType<? extends Container> containerType = registryProvider.containers.get(provider_name).asContainerType();
        if (containerType == null) {
            throw new RegistryInjectionException(String.format(
                    "Container could not be found for provider name: %s",
                    provider_name
            ));
        }
        return containerType;
    }

    public static Map<String, BlockImplGrouping> getScreensToBeRegistered() {
        return registryProvider.screensToBeRegistered;
    }

    public static ItemRegistryObject<? extends Item> getItemRegistryObject(final String provider_name) {
        final ItemRegistryObject<? extends Item> registryObject = registryProvider.items.get(provider_name);
        if (registryObject == null) {
            throw new RegistryInjectionException(String.format(
                    "Item could not be found for provider name: %s",
                    provider_name
            ));
        }
        return registryObject;
    }

    public static FlowingFluid getFlowingFluid(final String provider_name) {
        final FlowingFluid flowingFluid = registryProvider.flowingFluids.get(provider_name).asFluid();
        if (flowingFluid == null) {
            throw new RegistryInjectionException(String.format(
                    "Flowing fluid could not be found for provider name: %s",
                    provider_name
            ));
        }
        return flowingFluid;
    }

    public static Fluid getSourceFluid(final String provider_name) {
        final Fluid sourceFluid = registryProvider.sourceFluids.get(provider_name).asFluid();
        if (sourceFluid == null) {
            throw new RegistryInjectionException(String.format(
                    "Fluid source could not be found for provider name: %s",
                    provider_name
            ));
        }
        return sourceFluid;
    }

}
