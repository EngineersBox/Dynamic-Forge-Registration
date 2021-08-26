package com.engineersbox.expandedfusion.core.registration.registryObject.data.recipe;

import com.engineersbox.expandedfusion.core.data.IRecipeProvider;
import com.engineersbox.expandedfusion.core.registration.registryObject.RegistryObjectWrapper;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;

public class RecipeSerializerRegistryObject<T extends IRecipe<?>> extends RegistryObjectWrapper<IRecipeSerializer<T>> implements IRecipeProvider {

    public RecipeSerializerRegistryObject(final RegistryObject<IRecipeSerializer<T>> registryObject) {
        super(registryObject);
    }

    @Override
    public IRecipeSerializer<T> asSerializer() {
        return this.registryObject.get();
    }
}
