package com.engineersbox.expandedfusion.elements.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class EmptyCanisterItem extends CanisterItem {
    public EmptyCanisterItem(final Properties properties) {
        super(properties);
    }

    @Override
    public FluidStack getFluid(final ItemStack stack) {
        return FluidStack.EMPTY;
    }

    @Override
    public void fillItemGroup(final ItemGroup group, final NonNullList<ItemStack> items) {
        if (isInGroup(group)) {
            items.add(new ItemStack(this));
        }
    }
}
