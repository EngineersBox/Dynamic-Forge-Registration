package com.engineersbox.expandedfusion.core.registration.contexts;

import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting.CraftingRecipeImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplGrouping;
import com.engineersbox.expandedfusion.core.registration.registryObject.data.recipe.RecipeSerializerRegistryObject;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.*;
import com.google.inject.Singleton;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.*;

@Singleton
public class RegistryProvider {
    // Elements
    public final Map<String, BlockRegistryObject<? extends Block>> blocks = new HashMap<>();
    public final Map<String, TileEntityRegistryObject<? extends TileEntity>> tileEntities = new HashMap<>();
    public final Map<String, ContainerRegistryObject<? extends Container>> containers = new HashMap<>();
    public final Map<String, BlockImplGrouping> screensToBeRegistered = new HashMap<>();
    public final Map<String, BlockImplGrouping> renderersToBeRegistered = new HashMap<>();
    public final Map<String, FluidRegistryObject<? extends Fluid>> sourceFluids = new HashMap<>();
    public final Map<String, FluidRegistryObject<? extends FlowingFluid>> flowingFluids = new HashMap<>();
    public final Map<String, ItemRegistryObject<? extends Item>> items = new HashMap<>();

    // Recipes
    public final Map<String, IRecipeType<? extends IRecipe<?>>> recipeTypes = new HashMap<>();
    public final Map<String, RecipeSerializerRegistryObject<? extends IRecipe<?>>> recipeSerializers = new HashMap<>();
    public final Map<String, CraftingRecipeImplGrouping> craftingRecipesToBeRegistered = new HashMap<>();

    // Tags
    public final Map<ResourceLocation, ITag.INamedTag<Block>> blockTags = new HashMap<>();
    public final Map<ResourceLocation, ITag.INamedTag<Item>> itemTags = new HashMap<>();
    public final Map<ResourceLocation, ITag.INamedTag<Fluid>> fluidTags = new HashMap<>();
    public final Map<ITag.INamedTag<Block>, Set<String>> blockTagsToBeRegistered = new HashMap<>();
    public final Map<ITag.INamedTag<Block>, ITag.INamedTag<Item>> blockTagsToBeRegisteredAsItemTags = new HashMap<>();
    public final Map<ITag.INamedTag<Item>, Set<String>> itemTagsToBeRegistered = new HashMap<>();
    public final Map<ITag.INamedTag<Fluid>, Set<String>> sourceFluidTagsToBeRegistered = new HashMap<>();
    public final Map<ITag.INamedTag<Fluid>, Set<String>> flowingFluidTagsToBeRegistered = new HashMap<>();
}
