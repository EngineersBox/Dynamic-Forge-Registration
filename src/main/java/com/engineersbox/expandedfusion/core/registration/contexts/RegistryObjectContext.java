package com.engineersbox.expandedfusion.core.registration.contexts;

import com.engineersbox.expandedfusion.core.registration.exception.contexts.RegistryObjectRetrievalException;
import com.engineersbox.expandedfusion.core.registration.registryObject.*;
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
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Map;

public abstract class RegistryObjectContext {

    private RegistryObjectContext() {
        throw new IllegalStateException("Utility Class");
    }

    private static final RegistryProvider registryProvider = Guice.createInjector(new ProviderModule()).getInstance(RegistryProvider.class);

    private static <T extends IForgeRegistryEntry<? super T>, E extends RegistryObjectWrapper<T>> E getRegistryObject(final Class<E> clazz,
                                                                                                                      final String provider_name,
                                                                                                                      final Map<String, ? extends E> registryMapper) {
        final E registryEntry = registryMapper.get(provider_name);
        if (registryEntry == null) {
            throw new RegistryObjectRetrievalException(String.format(
                    "%s could not be found for provider name: %s",
                    clazz.getName(),
                    provider_name
            ));
        }
        return registryEntry;
    }

    @SuppressWarnings("unchecked, raw")
    public static TileEntityRegistryObject<? extends TileEntity> getTileEntityRegistryObject(final String provider_name) {
        return RegistryObjectContext.<TileEntityType, TileEntityRegistryObject>getRegistryObject(TileEntityRegistryObject.class, provider_name, registryProvider.tileEntities);
    }

    @SuppressWarnings("unchecked, raw")
    public static BlockRegistryObject<? extends Block> getBlockRegistryObject(final String provider_name) {
        return RegistryObjectContext.<Block, BlockRegistryObject>getRegistryObject(BlockRegistryObject.class, provider_name, registryProvider.blocks);
    }

    @SuppressWarnings("unchecked, raw")
    public static ContainerRegistryObject<? extends Container> getContainerRegistryObject(final String provider_name) {
        return RegistryObjectContext.<ContainerType, ContainerRegistryObject>getRegistryObject(ContainerRegistryObject.class, provider_name, registryProvider.containers);
    }

    public static Map<String, BlockImplGrouping> getScreensToBeRegistered() {
        return registryProvider.screensToBeRegistered;
    }

    @SuppressWarnings("unchecked, raw")
    public static ItemRegistryObject<? extends Item> getItemRegistryObject(final String provider_name) {
        return RegistryObjectContext.<Item, ItemRegistryObject>getRegistryObject(ItemRegistryObject.class, provider_name, registryProvider.items);
    }

    @SuppressWarnings("unchecked, raw")
    public static FluidRegistryObject<? extends FlowingFluid> getFlowingFluidRegistryObject(final String provider_name) {
        return RegistryObjectContext.<FlowingFluid, FluidRegistryObject>getRegistryObject(FluidRegistryObject.class, provider_name, registryProvider.flowingFluids);
    }

    @SuppressWarnings("unchecked, raw")
    public static FluidRegistryObject<? extends Fluid> getSourceFluidRegistryObject(final String provider_name) {
        return RegistryObjectContext.<Fluid, FluidRegistryObject>getRegistryObject(FluidRegistryObject.class, provider_name, registryProvider.sourceFluids);
    }

}