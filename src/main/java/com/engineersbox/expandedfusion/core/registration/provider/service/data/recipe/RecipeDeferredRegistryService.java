package com.engineersbox.expandedfusion.core.registration.provider.service.data.recipe;

import com.engineersbox.expandedfusion.core.registration.provider.service.RegistryService;
import com.engineersbox.expandedfusion.core.registration.registryObject.data.recipe.RecipeSerializerRegistryObject;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class RecipeDeferredRegistryService extends RegistryService<IRecipeSerializer<?>> {

    private final Registration registration;

    @Inject
    public RecipeDeferredRegistryService(@Named("modId") final String modID,
                                         final Registration registration) {
        this.modID = modID;
        this.registration = registration;
    }

    public <T extends IRecipe<?>> RecipeSerializerRegistryObject<T> registerSerializer(final String name,
                                                                                       final Supplier<IRecipeSerializer<T>> serializer) {
        return new RecipeSerializerRegistryObject<>(this.registration.getRecipeSerializerRegister().register(new ResourceLocation(this.modID, name).getPath(), serializer));
    }

    public <T extends IRecipe<?>> IRecipeType<T> registerType(final String name) {
        return Registry.register(Registry.RECIPE_TYPE, name, new IRecipeType<T>() {
            @Override
            public String toString() {
                return new ResourceLocation(modID, name).toString();
            }
        });
    }

}
