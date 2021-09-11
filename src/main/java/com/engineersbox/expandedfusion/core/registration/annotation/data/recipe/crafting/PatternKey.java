package com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.crafting;

import com.engineersbox.expandedfusion.core.registration.data.recipe.IngredientType;

public @interface PatternKey {
    char symbol();
    String[] ingredient();
    IngredientType type();
}
