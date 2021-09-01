package com.engineersbox.expandedfusion.core.registration.provider.data.recipe;

import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.RegistryProvider;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting.CraftingRecipeImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting.CraftingRecipeImplGrouping;
import com.google.inject.Inject;

public class CraftingRecipeRegistrationResolver extends RegistrationResolver {

    private final CraftingRecipeImplClassGrouping implClassGroupings;
    private final RegistryProvider registryProvider;

    @Inject
    public CraftingRecipeRegistrationResolver(final RegistryProvider registryProvider,
                                              final ImplClassGroupings<CraftingRecipeImplGrouping> implClassGroupings) {
        this.registryProvider = registryProvider;
        this.implClassGroupings = (CraftingRecipeImplClassGrouping) implClassGroupings;
        this.implClassGroupings.collectAnnotatedResources();
    }

    @Override
    public void registerAll() {
        this.registryProvider.craftingRecipesToBeRegistered.putAll(this.implClassGroupings.getClassGroupings());
    }
}
