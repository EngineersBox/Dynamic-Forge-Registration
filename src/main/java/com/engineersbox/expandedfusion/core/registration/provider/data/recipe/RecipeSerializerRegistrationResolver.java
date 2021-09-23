package com.engineersbox.expandedfusion.core.registration.provider.data.recipe;

import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.implementation.RecipeImplementation;
import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.implementation.RecipeSerializer;
import com.engineersbox.expandedfusion.core.registration.annotation.resolver.RegistrationPhaseHandler;
import com.engineersbox.expandedfusion.core.registration.contexts.provider.RecipeRegistryProvider;
import com.engineersbox.expandedfusion.core.registration.exception.provider.data.ProviderDataRegistrationException;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.implementation.RecipeSerializerImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.implementation.RecipeSerializerImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.service.RegistryService;
import com.engineersbox.expandedfusion.core.registration.provider.service.data.recipe.RecipeDeferredRegistryService;
import com.engineersbox.expandedfusion.core.registration.resolver.ResolverPhase;
import com.google.inject.Inject;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;

@RegistrationPhaseHandler(ResolverPhase.RECIPE_SERIALIZER)
public class RecipeSerializerRegistrationResolver implements RegistrationResolver  {

    private final RecipeRegistryProvider recipeRegistryProvider;
    private final RecipeSerializerImplClassGrouping implClassGroupings;
    private final RecipeDeferredRegistryService recipeDeferredRegistryService;

    @Inject
    public RecipeSerializerRegistrationResolver(final RecipeRegistryProvider recipeRegistryProvider,
                                                final ImplClassGroupings<RecipeSerializerImplGrouping> implClassGroupings,
                                                final RegistryService<IRecipeSerializer<?>> recipeDeferredRegistryService) {
        this.recipeRegistryProvider = recipeRegistryProvider;
        this.implClassGroupings = (RecipeSerializerImplClassGrouping) implClassGroupings;
        this.recipeDeferredRegistryService = (RecipeDeferredRegistryService) recipeDeferredRegistryService;
    }

    @Override
    public void registerAll() {
        this.implClassGroupings.getClassGroupings().forEach(this::registerRecipeSerializer);
    }

    private void registerRecipeSerializer(final String name,
                                          final RecipeSerializerImplGrouping group) {
        registerImplementation(name, group);
        registerSerializer(name, group);
    }

    private void registerImplementation(final String name,
                                        final RecipeSerializerImplGrouping group) {
        final RecipeImplementation recipeImplementation = group.getRecipeImplementationAnnotation();
        if (recipeImplementation == null) {
            throw new ProviderDataRegistrationException(String.format(
                    "IRecipe implementation %s has no plausible annotation",
                    name
            ));
        }
        if (!recipeImplementation.name().equals(name)) {
            throw new ProviderDataRegistrationException(String.format(
                    "Mismatched provider element name against annotation: %s != %s",
                    name,
                    recipeImplementation.name()
            ));
        }
        final Class<? extends IRecipe<?>> recipeImplementationImpl = group.getRecipeImplementation();
        if (recipeImplementationImpl == null) {
            throw new ProviderDataRegistrationException(String.format(
                    "No item implementation could be found with associated annotation: %s",
                    name
            ));
        }
        this.recipeRegistryProvider.recipeTypes.put(
                name,
                this.recipeDeferredRegistryService.registerType(name)
        );
    }

    private void registerSerializer(final String name,
                                    final RecipeSerializerImplGrouping group) {
        final RecipeSerializer recipeSerializer = group.getRecipeSerializerAnnotation();
        if (recipeSerializer == null) {
            throw new ProviderDataRegistrationException(String.format(
                    "IRecipeSerializer implementation %s has no plausible annotation",
                    name
            ));
        }
        if (!recipeSerializer.name().equals(name)) {
            throw new ProviderDataRegistrationException(String.format(
                    "Mismatched provider element name against annotation: %s != %s",
                    name,
                    recipeSerializer.name()
            ));
        }
        final Class<? extends IRecipeSerializer<? extends IRecipe<?>>> recipeSerializerImpl = group.getSerializer();
        if (recipeSerializerImpl == null) {
            throw new ProviderDataRegistrationException(String.format(
                    "No item implementation could be found with associated annotation: %s",
                    name
            ));
        }
        this.recipeRegistryProvider.recipeSerializers.put(
                name,
                this.recipeDeferredRegistryService.registerSerializer(
                        name,
                        () -> this.<IRecipeSerializer<? extends IRecipe<?>>>instantiateWithDefaultConstructor(recipeSerializerImpl)
                )
        );
    }
}
