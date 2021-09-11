package com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.implementation;

import com.engineersbox.expandedfusion.core.reflection.ReflectionClassFilter;
import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.implementation.RecipeImplementation;
import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.implementation.RecipeSerializer;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import org.reflections.Reflections;

import java.util.Set;

public class RecipeSerializerImplClassGrouping extends ImplClassGroupings<RecipeSerializerImplGrouping> {

    private final Reflections reflections;

    @Inject
    public RecipeSerializerImplClassGrouping(@Named("packageReflections") final Reflections reflections) {
        this.reflections = reflections;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void collectAnnotatedResources() {
        final Set<Class<? extends IRecipe>> recipeImplementationAnnotatedClasses = ReflectionClassFilter.filterClassesBySuperType(
                IRecipe.class,
                this.reflections.getTypesAnnotatedWith(RecipeImplementation.class)
        );
        for (final Class<? extends IRecipe> c : recipeImplementationAnnotatedClasses) {
            final RecipeImplementation recipeImplementation = c.getAnnotation(RecipeImplementation.class);
            if (recipeImplementation == null) {
                continue;
            }
            addIfNotExists(recipeImplementation.name(), c);
        }
        final Set<Class<? extends IRecipeSerializer>> recipeSerializerAnnotatedClasses = ReflectionClassFilter.filterClassesBySuperType(
                IRecipeSerializer.class,
                this.reflections.getTypesAnnotatedWith(RecipeSerializer.class)
        );
        for (final Class<? extends IRecipeSerializer> c : recipeSerializerAnnotatedClasses) {
            final RecipeSerializer recipeSerializer = c.getAnnotation(RecipeSerializer.class);
            if (recipeSerializer == null) {
                continue;
            }
            addIfNotExists(recipeSerializer.name(), c);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addIfNotExists(String name, Class<?> toAdd) {
        RecipeSerializerImplGrouping recipeSerializerImplGrouping = this.classGroupings.get(name);
        if (recipeSerializerImplGrouping == null) {
            recipeSerializerImplGrouping = new RecipeSerializerImplGrouping();
        }
        if (IRecipe.class.isAssignableFrom(toAdd)) {
            recipeSerializerImplGrouping.setRecipeImplementation((Class<? extends IRecipe<?>>) toAdd);
        } else if (IRecipeSerializer.class.isAssignableFrom(toAdd)) {
            recipeSerializerImplGrouping.setSerializer((Class<? extends IRecipeSerializer<?>>) toAdd);
        }
        this.classGroupings.put(name, recipeSerializerImplGrouping);
    }
}
