package com.engineersbox.expandedfusion.core.data.annotation.recipe.crafting;

import com.engineersbox.expandedfusion.core.data.recipe.IngredientType;

public @interface PatternKey {
    char symbol();
    String ingredient();
    IngredientType type();
}
