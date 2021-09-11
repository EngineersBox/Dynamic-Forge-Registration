package com.engineersbox.expandedfusion.crafting.recipe.fusion;

import com.engineersbox.expandedfusion.core.elements.machine.IMachineInventory;
import com.engineersbox.expandedfusion.core.registration.annotation.data.recipe.implementation.RecipeImplementation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@RecipeImplementation(name = FusionRecipe.PROVIDER_NAME)
public class FusionRecipe implements IRecipe<IMachineInventory> {

    public static final String PROVIDER_NAME = "fusion";

    @Override
    public boolean matches(IMachineInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IMachineInventory inv) {
        return null;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return null;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public IRecipeType<?> getType() {
        return null;
    }
}
