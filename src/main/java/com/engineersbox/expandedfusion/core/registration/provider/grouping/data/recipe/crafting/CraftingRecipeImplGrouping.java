package com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting;

import com.engineersbox.expandedfusion.core.registration.annotation.recipe.crafting.CraftingRecipe;
import com.engineersbox.expandedfusion.core.registration.exception.grouping.data.recipe.DuplicateCraftingRecipeBindingException;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplGrouping;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CraftingRecipeImplGrouping implements ImplGrouping {

    private Class<? extends ForgeRegistryEntry<?>> registryEntry;

    public Class<? extends ForgeRegistryEntry<?>> getRegistryEntry() {
        return this.registryEntry;
    }

    public CraftingRecipe[] getCraftingRecipeAnnotations() {
        if (this.registryEntry == null) {
            return null;
        }
        return this.registryEntry.getAnnotationsByType(CraftingRecipe.class);
    }

    public void setRegistryEntry(final Class<? extends ForgeRegistryEntry<?>> registryEntry) {
        if (this.registryEntry != null) {
            throw new DuplicateCraftingRecipeBindingException(this.registryEntry, registryEntry);
        }
        this.registryEntry = registryEntry;
    }

}
