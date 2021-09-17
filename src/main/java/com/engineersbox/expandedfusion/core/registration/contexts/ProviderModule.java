package com.engineersbox.expandedfusion.core.registration.contexts;

import com.engineersbox.expandedfusion.core.registration.contexts.provider.ElementRegistryProvider;
import com.engineersbox.expandedfusion.core.registration.contexts.provider.RecipeRegistryProvider;
import com.engineersbox.expandedfusion.core.registration.contexts.provider.TagRegistryProvider;
import com.google.inject.AbstractModule;

public final class ProviderModule extends AbstractModule {

    private static final ElementRegistryProvider elementRegistryProvider = new ElementRegistryProvider();
    private static final RecipeRegistryProvider recipeRegistryProvider = new RecipeRegistryProvider();
    private static final TagRegistryProvider tagRegistryProvider = new TagRegistryProvider();

    @Override
    protected void configure() {
        bind(ElementRegistryProvider.class).toInstance(elementRegistryProvider);
        bind(RecipeRegistryProvider.class).toInstance(recipeRegistryProvider);
        bind(TagRegistryProvider.class).toInstance(tagRegistryProvider);
    }
}
