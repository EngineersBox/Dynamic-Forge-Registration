package com.engineersbox.expandedfusion.crafting.recipe.fusion;

import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.implementation.RecipeSerializer;
import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

@RecipeSerializer(name = FusionRecipe.PROVIDER_NAME)
public class FusionRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<FusionRecipe> {
    @Override
    public FusionRecipe read(ResourceLocation recipeId, JsonObject json) {
        return null;
    }

    @Nullable
    @Override
    public FusionRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        return null;
    }

    @Override
    public void write(PacketBuffer buffer, FusionRecipe recipe) {

    }
}
