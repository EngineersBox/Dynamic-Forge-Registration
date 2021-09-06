package com.engineersbox.expandedfusion.core.registration.exception.grouping.data.recipe;

import com.engineersbox.expandedfusion.core.registration.exception.grouping.DuplicateComponentBinding;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;

public class DuplicateRecipeSerializerBindingException extends DuplicateComponentBinding {

    private static final List<Class<?>> ASSIGNABLE_TO_CHECK = ImmutableList.of(
            ForgeRegistryEntry.class,
            IRecipe.class
    );

    public <T> DuplicateRecipeSerializerBindingException(final Class<? extends T> current, final Class<? extends T> duplicate) {
        super("recipe serializer", current, duplicate, ASSIGNABLE_TO_CHECK);
    }

}
