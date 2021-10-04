package com.engineersbox.expandedfusion.core.registration.contexts.provider;

import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting.CraftingRecipeImplGrouping;
import com.engineersbox.expandedfusion.core.registration.registryObject.data.recipe.RecipeSerializerRegistryObject;
import com.google.inject.Singleton;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Singleton
public final class RecipeRegistryProvider {
    public final Map<String, IRecipeType<? extends IRecipe<?>>> recipeTypes = new HashMap<>();
    public final Map<String, RecipeSerializerRegistryObject<? extends IRecipe<?>>> recipeSerializers = new HashMap<>();
    public final Map<String, CraftingRecipeImplGrouping> craftingRecipesToBeRegistered = new HashMap<>();
    public final List<Consumer<Consumer<IFinishedRecipe>>> anonymousCraftingRecipesToBeRegistered = new ArrayList<>();
}
