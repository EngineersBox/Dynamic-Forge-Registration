package com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.shapeless;

import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.ProvidesRecipeHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should only be used on methods with the following signature:
 *
 * <ul>
 *  <li>Modifiers: PUBLIC</li>
 *  <li>Return Type: ANY (will ignore non-void)</li>
 *  <li>Name: ANY</li>
 *  <li>Parameters: 1</li>
 *  <li>Parameter Entry 1: {@code final Consumer<IFinishedRecipe> consumer}</li>
 * </ul>
 *
 * Example:
 * {@code public void methodName(final Consumer<IFinishedRecipe> consumer)}
 */
@ProvidesRecipeHandler
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RecipeBuilder {
}
