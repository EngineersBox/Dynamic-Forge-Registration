package com.engineersbox.expandedfusion.core.registration.annotation.recipe.crafting;

import com.engineersbox.expandedfusion.core.registration.annotation.recipe.ProvidesRecipeHandler;

import java.lang.annotation.*;

@ProvidesRecipeHandler
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(CraftingRecipes.class)
public @interface CraftingRecipe {
    // If this field is specified, use it over the value found in
    // @BlockProvider or @ItemProvider annotations on the same class
    String provider() default "";
    PatternLine[] pattern();
    PatternKey[] keys();
    UnlockCriterion[] criteria() default {};
}
