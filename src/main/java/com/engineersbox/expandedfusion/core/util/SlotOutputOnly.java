package com.engineersbox.expandedfusion.core.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotOutputOnly extends Slot {
    public SlotOutputOnly(final IInventory inventoryIn, final int index, final int xPosition, final int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(final ItemStack stack) {
        return false;
    }
}
