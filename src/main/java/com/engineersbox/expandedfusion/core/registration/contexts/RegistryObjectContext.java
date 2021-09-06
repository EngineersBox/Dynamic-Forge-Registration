package com.engineersbox.expandedfusion.core.registration.contexts;

import com.engineersbox.expandedfusion.core.registration.exception.contexts.RegistryObjectRetrievalException;
import com.engineersbox.expandedfusion.core.registration.exception.grouping.data.tag.DuplicateTagComponentBinding;
import com.engineersbox.expandedfusion.core.registration.provider.RegistryProvider;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting.CraftingRecipeImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplGrouping;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.*;
import com.google.inject.Guice;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

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

    public static ITag.INamedTag<Block> getBlockTag(final String provider_name) {
        return REGISTRY_PROVIDER.blockTags.get(provider_name);
    }

    public static ITag.INamedTag<Item> getItemTag(final String provider_name) {
        return REGISTRY_PROVIDER.itemTags.get(provider_name);
    }

    public static void registerBlockTag(final String providerName, final ITag.INamedTag<Block> tag) {
        if (REGISTRY_PROVIDER.blockTags.containsKey(providerName)) {
            throw new DuplicateTagComponentBinding(REGISTRY_PROVIDER.blockTags.get(providerName), tag);
        }
        REGISTRY_PROVIDER.blockTags.put(providerName, tag);
    }

    public static void registerBlockTag(final String providerName, final ResourceLocation tag) {
        registerBlockTag(providerName, BlockTags.makeWrapperTag(tag.toString()));
    }

    public static void registerBlockTag(final String providerName, final String base, final String tag) {
        registerBlockTag(providerName, new ResourceLocation(base, tag));
    }

    public static void registerBlockTag(final String providerName, final String tag) {
        registerBlockTag(providerName, "forge", tag);
    }

    public static void registerItemTag(final String providerName, final ITag.INamedTag<Item> tag) {
        if (REGISTRY_PROVIDER.itemTags.containsKey(providerName)) {
            throw new DuplicateTagComponentBinding(REGISTRY_PROVIDER.itemTags.get(providerName), tag);
        }
        REGISTRY_PROVIDER.itemTags.put(providerName, tag);
    }

    public static void registerItemTag(final String providerName, final ResourceLocation tag) {
        registerItemTag(providerName, ItemTags.makeWrapperTag(tag.toString()));
    }

    public static void registerItemTag(final String providerName, final String base, final String tag) {
        registerItemTag(providerName, new ResourceLocation(base, tag));
    }

    public static void registerItemTag(final String providerName, final String tag) {
        registerItemTag(providerName, "forge", tag);
    }
}
