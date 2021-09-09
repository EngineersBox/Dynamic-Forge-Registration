package com.engineersbox.expandedfusion.core.registration.contexts;

import com.engineersbox.expandedfusion.core.registration.exception.contexts.RegistryObjectRetrievalException;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting.CraftingRecipeImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplGrouping;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.*;
import com.google.inject.Guice;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Set;

public abstract class RegistryObjectContext {

    private RegistryObjectContext() {
        throw new IllegalStateException("Utility Class");
    }

    private static final RegistryProvider REGISTRY_PROVIDER = Guice.createInjector(new ProviderModule()).getInstance(RegistryProvider.class);

    private static <T> T getRegistryObject(final Class<?> clazz,
                                           final String provider_name,
                                           final Map<String, T> registryMapper) {
        final T registryEntry = registryMapper.get(provider_name);
        if (registryEntry == null) {
            throw new RegistryObjectRetrievalException(String.format(
                    "%s could not be found for provider name: %s",
                    clazz.getName(),
                    provider_name
            ));
        }
        return registryEntry;
    }

    public static TileEntityRegistryObject<? extends TileEntity> getTileEntityRegistryObject(final String provider_name) {
        return RegistryObjectContext.getRegistryObject(TileEntityRegistryObject.class, provider_name, REGISTRY_PROVIDER.tileEntities);
    }

    public static BlockRegistryObject<? extends Block> getBlockRegistryObject(final String provider_name) {
        return RegistryObjectContext.getRegistryObject(BlockRegistryObject.class, provider_name, REGISTRY_PROVIDER.blocks);
    }

    public static ContainerRegistryObject<? extends Container> getContainerRegistryObject(final String provider_name) {
        return RegistryObjectContext.getRegistryObject(ContainerRegistryObject.class, provider_name, REGISTRY_PROVIDER.containers);
    }

    public static Map<String, BlockImplGrouping> getScreensToBeRegistered() {
        return REGISTRY_PROVIDER.screensToBeRegistered;
    }

    public static Map<String, BlockImplGrouping> getRenderersToBeRegistered() {
        return REGISTRY_PROVIDER.renderersToBeRegistered;
    }

    public static ItemRegistryObject<? extends Item> getItemRegistryObject(final String provider_name) {
        return RegistryObjectContext.getRegistryObject(ItemRegistryObject.class, provider_name, REGISTRY_PROVIDER.items);
    }

    public static FluidRegistryObject<? extends FlowingFluid> getFlowingFluidRegistryObject(final String provider_name) {
        return RegistryObjectContext.getRegistryObject(FluidRegistryObject.class, provider_name, REGISTRY_PROVIDER.flowingFluids);
    }

    public static FluidRegistryObject<? extends Fluid> getSourceFluidRegistryObject(final String provider_name) {
        return RegistryObjectContext.getRegistryObject(FluidRegistryObject.class, provider_name, REGISTRY_PROVIDER.sourceFluids);
    }

    public static Map<String, CraftingRecipeImplGrouping> getCraftingRecipesToBeRegistered() {
        return REGISTRY_PROVIDER.craftingRecipesToBeRegistered;
    }

    public static Map<ITag.INamedTag<Block>, Set<String>> getBlockTagsToBeRegistered() {
        return REGISTRY_PROVIDER.blockTagsToBeRegistered;
    }

    public static Map<ITag.INamedTag<Block>, ITag.INamedTag<Item>> getBlockTagsToBeRegisteredAsItemTags() {
        return REGISTRY_PROVIDER.blockTagsToBeRegisteredAsItemTags;
    }

    public static Map<ITag.INamedTag<Item>, Set<String>> getItemTagsToBeRegistered() {
        return REGISTRY_PROVIDER.itemTagsToBeRegistered;
    }

    public static Map<ITag.INamedTag<Fluid>, Set<String>> getSourceFluidTagsToBeRegistered() {
        return REGISTRY_PROVIDER.sourceFluidTagsToBeRegistered;
    }

    public static Map<ITag.INamedTag<Fluid>, Set<String>> getFlowingFluidTagsToBeRegistered() {
        return REGISTRY_PROVIDER.flowingFluidTagsToBeRegistered;
    }
}
