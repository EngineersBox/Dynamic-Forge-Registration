package com.engineersbox.expandedfusion.core.registration.provider.data.recipe;

import com.engineersbox.expandedfusion.core.registration.annotation.recipe.implementation.RecipeImplementation;
import com.engineersbox.expandedfusion.core.registration.annotation.recipe.implementation.RecipeSerializer;
import com.engineersbox.expandedfusion.core.registration.exception.provider.data.ProviderDataRegistrationException;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.RegistryProvider;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.implementation.RecipeSerializerImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.implementation.RecipeSerializerImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShim;
import com.engineersbox.expandedfusion.core.registration.provider.shim.data.recipe.RecipeDeferredRegistryShim;
import com.google.inject.Inject;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;

public class RecipeSerializerProviderRegistrationResolver extends RegistrationResolver  {

    private final RegistryProvider registryProvider;
    private final RecipeSerializerImplClassGrouping implClassGroupings;
    private final RecipeDeferredRegistryShim recipeDeferredRegistryShim;

    @Inject
    public RecipeSerializerProviderRegistrationResolver(final RegistryProvider registryProvider,
                                                        final ImplClassGroupings<RecipeSerializerImplGrouping> implClassGroupings,
                                                        final RegistryShim<IRecipeSerializer<?>> recipeDeferredRegistryShim) {
        this.registryProvider = registryProvider;
        this.implClassGroupings = (RecipeSerializerImplClassGrouping) implClassGroupings;
        this.recipeDeferredRegistryShim = (RecipeDeferredRegistryShim) recipeDeferredRegistryShim;
    }

    @Override
    public void registerAll() {
        this.implClassGroupings.getClassGroupings().forEach(this::registerRecipeSerializer);
    }

    private void registerRecipeSerializer(final String name,
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
        this.registryProvider.recipeTypes.put(
                name,
                this.recipeDeferredRegistryShim.registerType(name)
        );
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
        this.registryProvider.recipeSerializers.put(
                name,
                this.recipeDeferredRegistryShim.registerSerializer(
                        name,
                        () -> super.<IRecipeSerializer<? extends IRecipe<?>>>instantiateWithDefaultConstructor(recipeSerializerImpl)
                )
        );
    }
}
