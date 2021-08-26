package com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.implementation;

import com.engineersbox.expandedfusion.core.registration.annotation.recipe.implementation.RecipeImplementation;
import com.engineersbox.expandedfusion.core.registration.annotation.recipe.implementation.RecipeSerializer;
import com.engineersbox.expandedfusion.core.registration.exception.grouping.data.recipe.DuplicateRecipeSerializerBindingException;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplGrouping;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class RecipeSerializerImplGrouping implements ImplGrouping {

    private Class<? extends IRecipe<?>> recipeImplementation;
    private Class<? extends IRecipeSerializer<?>> serializer;

    public Class<? extends IRecipe<?>> getRecipeImplementation() {
        return this.recipeImplementation;
    }

    public RecipeImplementation getRecipeImplementationAnnotation() {
        if (this.recipeImplementation == null) {
            return null;
        }
        return this.recipeImplementation.getAnnotation(RecipeImplementation.class);
    }

    public void setRecipeImplementation(final Class<? extends IRecipe<?>> recipeImplementation) {
        if (this.recipeImplementation != null) {
            throw new DuplicateRecipeSerializerBindingException(this.recipeImplementation, recipeImplementation);
        }
        this.recipeImplementation = recipeImplementation;
    }

    @SuppressWarnings("unchecked")
    public <T extends ForgeRegistryEntry<IRecipeSerializer<?>> & IRecipeSerializer<?>> Class<? extends T> getSerializer() {
        return (Class<T>) this.serializer;
    }

    public RecipeSerializer getRecipeSerializerAnnotation() {
        if (this.serializer == null) {
            return null;
        }
        return this.serializer.getAnnotation(RecipeSerializer.class);
    }

    public void setSerializer(final Class<? extends IRecipeSerializer<?>> serializer) {
        if (this.serializer != null) {
            throw new DuplicateRecipeSerializerBindingException(this.serializer, serializer);
        }
        this.serializer = serializer;
    }

}
