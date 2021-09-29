package com.engineersbox.expandedfusion.core.registration.data.recipe;

import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;

public enum IngredientType {
    TAG(ITag.class),
    ITEM(IItemProvider.class);

    final Class<?> clazz;

    IngredientType(final Class<?> clazz) {
        this.clazz = clazz;
    }
}
