package com.engineersbox.expandedfusion.register;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public final class ModRecipes {
    public static final class Types {
    }

    private ModRecipes() {}

    static void register() {}

    private static RegistryObject<IRecipeSerializer<?>> registerSerializer(final ResourceLocation name, final Supplier<IRecipeSerializer<?>> serializer) {
        return Registration.RECIPE_SERIALIZERS.register(name.getPath(), serializer);
    }

    private static <T extends IRecipe<?>> IRecipeType<T> registerType(final ResourceLocation name) {
        return Registry.register(Registry.RECIPE_TYPE, name, new IRecipeType<T>() {
            @Override
            public String toString() {
                return name.toString();
            }
        });
    }
}
