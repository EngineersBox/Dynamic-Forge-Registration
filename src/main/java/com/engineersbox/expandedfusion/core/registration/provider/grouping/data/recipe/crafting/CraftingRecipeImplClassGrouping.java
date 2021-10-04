package com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting;

import com.engineersbox.expandedfusion.core.reflection.ReflectionClassFilter;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.item.ItemProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.crafting.CraftingRecipe;
import com.engineersbox.expandedfusion.core.registration.exception.grouping.element.block.DuplicateBlockComponentBinding;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.reflections.Reflections;

import java.util.Set;

public class CraftingRecipeImplClassGrouping extends ImplClassGroupings<CraftingRecipeImplGrouping> {

    private final Reflections reflections;

    @Inject
    public CraftingRecipeImplClassGrouping(@Named("packageReflections") final Reflections reflections) {
        this.reflections = reflections;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void collectAnnotatedResources() {
        final Set<Class<? extends ForgeRegistryEntry>> elementProviderAnnotatedClasses = ReflectionClassFilter.filterClassesBySuperType(
                ForgeRegistryEntry.class,
                this.reflections.getTypesAnnotatedWith(CraftingRecipe.class)
        );
        for (final Class<? extends ForgeRegistryEntry> c : elementProviderAnnotatedClasses) {
            final ItemProvider itemProvider = c.getAnnotation(ItemProvider.class);
            final BlockProvider blockProvider = c.getAnnotation(BlockProvider.class);
            if ((itemProvider == null) == (blockProvider == null)) {
                continue;
            }
            final CraftingRecipe[] recipeProviders = c.getAnnotationsByType(CraftingRecipe.class);
            if (recipeProviders.length == 0) {
                continue;
            }
            addIfNotExists(itemProvider != null ? itemProvider.name() : blockProvider.name(), c);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addIfNotExists(final String name, final Class<?> toAdd) throws DuplicateBlockComponentBinding {
        CraftingRecipeImplGrouping recipeImplGrouping = this.classGroupings.get(name);
        if (recipeImplGrouping == null) {
            recipeImplGrouping = new CraftingRecipeImplGrouping();
        }
        if (ForgeRegistryEntry.class.isAssignableFrom(toAdd)) {
            recipeImplGrouping.setRegistryEntry((Class<? extends ForgeRegistryEntry<?>>) toAdd);
        }
        this.classGroupings.put(name, recipeImplGrouping);
    }

}
