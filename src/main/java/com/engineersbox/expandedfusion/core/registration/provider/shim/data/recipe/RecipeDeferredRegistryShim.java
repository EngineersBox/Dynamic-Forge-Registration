package com.engineersbox.expandedfusion.core.registration.provider.shim.data.recipe;

import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShim;
import com.engineersbox.expandedfusion.core.registration.registryObject.data.recipe.RecipeSerializerRegistryObject;
import com.engineersbox.expandedfusion.register.Registration;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class RecipeDeferredRegistryShim extends RegistryShim<IRecipeSerializer<?>> {

    private static final Logger LOGGER = LogManager.getLogger(RecipeDeferredRegistryShim.class);

    @Inject
    public RecipeDeferredRegistryShim(@Named("modId") final String modID) {
        this.modID = modID;
    }

    public <T extends IRecipe<?>> RecipeSerializerRegistryObject<T> registerSerializer(final String name,
                                                                                       final Supplier<IRecipeSerializer<T>> serializer) {
        return new RecipeSerializerRegistryObject<>(Registration.RECIPE_SERIALIZERS.register(new ResourceLocation(this.modID, name).getPath(), serializer));
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
