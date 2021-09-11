package com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.shapeless;

import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.ProvidesRecipeHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should only be used on classes with the following signature:
 *
 * <ul>
 *  <li>Modifiers: PUBLIC</li>
 *  <li>Name: ANY</li>
 *  <li>Extends: {@link net.minecraft.data.RecipeProvider}</li>
 *  <li>Implements: NONE</li>
 * </ul>
 *
 * Example:
 * <pre>{@code
 * @RecipeProvider
 * public class ModRecipeProvider extends net.minecraft.data.RecipeProvider {
 *      // ... Implementation here
 * }
 * }</pre>
 */
@ProvidesRecipeHandler
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RecipeProvider {
}
