package com.engineersbox.expandedfusion.register.registry.contexts.block;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.register.registry.BlockRegistryObject;
import com.engineersbox.expandedfusion.register.registry.contexts.ProviderModule;
import com.engineersbox.expandedfusion.register.registry.provider.BlockImplGrouping;
import com.google.inject.Guice;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.Map;

public abstract class BlockInjectionContext {

    private static final ExpandedFusion.RegistryProvider registryProvider = Guice.createInjector(new ProviderModule()).getInstance(ExpandedFusion.RegistryProvider.class);


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

}
