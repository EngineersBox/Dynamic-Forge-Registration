package com.engineersbox.expandedfusion.core.data;

import net.minecraft.item.crafting.IRecipeSerializer;

public interface IRecipeProvider {

    IRecipeSerializer<?> asSerializer();

}
