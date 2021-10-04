package com.engineersbox.expandedfusion.core.registration.contexts;

import com.engineersbox.expandedfusion.core.registration.contexts.provider.ElementRegistryProvider;
import com.engineersbox.expandedfusion.core.registration.contexts.provider.RecipeRegistryProvider;
import com.engineersbox.expandedfusion.core.registration.contexts.provider.TagRegistryProvider;
import com.engineersbox.expandedfusion.core.registration.exception.contexts.RegistryObjectRetrievalException;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting.CraftingRecipeImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplGrouping;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.minecraft.block.Block;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public abstract class RegistryObjectContext {

    private RegistryObjectContext() {
        throw new IllegalStateException("Static context Class");
    }

    private static final Injector INJECTOR = Guice.createInjector(new ProviderModule());
    private static final ElementRegistryProvider ELEMENT_REGISTRY_PROVIDER = INJECTOR.getInstance(ElementRegistryProvider.class);
    private static final RecipeRegistryProvider RECIPE_REGISTRY_PROVIDER = INJECTOR.getInstance(RecipeRegistryProvider.class);
    private static final TagRegistryProvider TAG_REGISTRY_PROVIDER = INJECTOR.getInstance(TagRegistryProvider.class);

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
        return RegistryObjectContext.getRegistryObject(TileEntityRegistryObject.class, provider_name, ELEMENT_REGISTRY_PROVIDER.tileEntities);
    }

    public static BlockRegistryObject<? extends Block> getBlockRegistryObject(final String provider_name) {
        return RegistryObjectContext.getRegistryObject(BlockRegistryObject.class, provider_name, ELEMENT_REGISTRY_PROVIDER.blocks);
    }

    public static ContainerRegistryObject<? extends Container> getContainerRegistryObject(final String provider_name) {
        return RegistryObjectContext.getRegistryObject(ContainerRegistryObject.class, provider_name, ELEMENT_REGISTRY_PROVIDER.containers);
    }

    public static Map<String, BlockImplGrouping> getScreensToBeRegistered() {
        return ELEMENT_REGISTRY_PROVIDER.screensToBeRegistered;
    }

    public static Map<String, BlockImplGrouping> getRenderersToBeRegistered() {
        return ELEMENT_REGISTRY_PROVIDER.renderersToBeRegistered;
    }

    public static ItemRegistryObject<? extends Item> getItemRegistryObject(final String provider_name) {
        return RegistryObjectContext.getRegistryObject(ItemRegistryObject.class, provider_name, ELEMENT_REGISTRY_PROVIDER.items);
    }

    public static FluidRegistryObject<? extends FlowingFluid> getFlowingFluidRegistryObject(final String provider_name) {
        return RegistryObjectContext.getRegistryObject(FluidRegistryObject.class, provider_name, ELEMENT_REGISTRY_PROVIDER.flowingFluids);
    }

    public static FluidRegistryObject<? extends Fluid> getSourceFluidRegistryObject(final String provider_name) {
        return RegistryObjectContext.getRegistryObject(FluidRegistryObject.class, provider_name, ELEMENT_REGISTRY_PROVIDER.sourceFluids);
    }

    public static Map<String, CraftingRecipeImplGrouping> getCraftingRecipesToBeRegistered() {
        return RECIPE_REGISTRY_PROVIDER.craftingRecipesToBeRegistered;
    }

    public static List<Consumer<Consumer<IFinishedRecipe>>> getAnonymousRecipesToBeRegistered() {
        return RECIPE_REGISTRY_PROVIDER.anonymousCraftingRecipesToBeRegistered;
    }

    public static Map<ITag.INamedTag<Block>, Pair<Set<String>, Boolean>> getBlockTagsToBeRegistered() {
        return TAG_REGISTRY_PROVIDER.blockTagsToBeRegistered;
    }

    public static Map<ITag.INamedTag<Block>, ITag.INamedTag<Item>> getBlockTagsToBeRegisteredAsItemTags() {
        return TAG_REGISTRY_PROVIDER.blockTagsToBeRegisteredAsItemTags;
    }

    public static Map<ITag.INamedTag<Item>, Pair<Set<String>, Boolean>> getItemTagsToBeRegistered() {
        return TAG_REGISTRY_PROVIDER.itemTagsToBeRegistered;
    }

    public static Map<ITag.INamedTag<Fluid>, Pair<Set<String>, Boolean>> getSourceFluidTagsToBeRegistered() {
        return TAG_REGISTRY_PROVIDER.sourceFluidTagsToBeRegistered;
    }

    public static Map<ITag.INamedTag<Fluid>, Pair<Set<String>, Boolean>> getFlowingFluidTagsToBeRegistered() {
        return TAG_REGISTRY_PROVIDER.flowingFluidTagsToBeRegistered;
    }
}
