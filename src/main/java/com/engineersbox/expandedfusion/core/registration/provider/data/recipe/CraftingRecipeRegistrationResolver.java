package com.engineersbox.expandedfusion.core.registration.provider.data.recipe;

import com.engineersbox.expandedfusion.core.registration.annotation.resolver.RegistrationPhaseHandler;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryProvider;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting.CraftingRecipeImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting.CraftingRecipeImplGrouping;
import com.engineersbox.expandedfusion.core.registration.resolver.ResolverPhase;
import com.google.inject.Inject;

@RegistrationPhaseHandler(ResolverPhase.RECIPE_INLINE_DECLARATION)
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
