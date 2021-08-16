package com.engineersbox.expandedfusion.core.registration.contexts;

import com.engineersbox.expandedfusion.core.registration.registryObject.BlockRegistryObject;
import com.engineersbox.expandedfusion.core.registration.registryObject.ItemRegistryObject;
import com.engineersbox.expandedfusion.core.registration.provider.RegistryProvider;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.block.BlockImplGrouping;
import com.google.inject.Guice;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.Map;

public abstract class RegistryInjectionContext {

    private static final RegistryProvider registryProvider = Guice.createInjector(new ProviderModule()).getInstance(RegistryProvider.class);

    public static TileEntityType<? extends TileEntity> getTileEntityType(final String provider_name) {
        final TileEntityType<? extends TileEntity> tileEntityType = registryProvider.tileEntities.get(provider_name);
        if (tileEntityType == null) {
            throw new RuntimeException(); // TODO: Implement this
        }
        return tileEntityType;
    }

    public static BlockRegistryObject<? extends Block> getBlockRegistryObject(final String provider_name) {
        final BlockRegistryObject<? extends Block> registryObject = registryProvider.blocks.get(provider_name);
        if (registryObject == null) {
            throw new RuntimeException(); // TODO: Implement this
        }
        return registryObject;
    }

    public static ContainerType<? extends Container> getContainerType(final String provider_name) {
        final ContainerType<? extends Container> containerType = registryProvider.containers.get(provider_name);
        if (containerType == null) {
            throw new RuntimeException(); // TODO: Implement this
        }
        return containerType;
    }

    public static Map<String, BlockImplGrouping> getScreensToBeRegistered() {
        return registryProvider.screensToBeRegistered;
    }

    public static ItemRegistryObject<? extends Item> getItemRegistryObject(final String provider_name) {
        final ItemRegistryObject<? extends Item> registryObject = registryProvider.items.get(provider_name);
        if (registryObject == null) {
            throw new RuntimeException(); // TODO: Implement this
        }
        return registryObject;
    }

    public static FlowingFluid getFlowingFluid(final String provider_name) {
        final FlowingFluid flowingFluid = registryProvider.fluids.get(provider_name);
        if (flowingFluid == null) {
            throw new RuntimeException(); // TODO: Implement this
        }
        return flowingFluid;
    }

}
