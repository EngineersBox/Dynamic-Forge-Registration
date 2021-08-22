package com.engineersbox.expandedfusion.core.data.annotation.recipe.crafting;

import com.engineersbox.expandedfusion.core.registration.annotation.provider.ProvidesElement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ProvidesElement
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CraftingRecipe {
    // If this field is specified, use it over the value found in
    // @BlockProvider or @ItemProvider annotations on the same class
    String provider() default "";
    PatternLine[] pattern();
    PatternKey[] keys();
    UnlockCriterion[] criteria() default {};
}
