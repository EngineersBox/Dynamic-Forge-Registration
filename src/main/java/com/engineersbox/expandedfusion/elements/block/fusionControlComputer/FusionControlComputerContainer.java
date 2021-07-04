package com.engineersbox.expandedfusion.elements.block.fusionControlComputer;

import com.engineersbox.expandedfusion.core.common.machine.AbstractMachineContainer;
import com.engineersbox.expandedfusion.core.common.machine.AbstractMachineTileEntity;
import com.engineersbox.expandedfusion.core.util.InventoryUtils;
import com.engineersbox.expandedfusion.core.util.SlotOutputOnly;
import com.engineersbox.expandedfusion.register.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

public class FusionControlComputerContainer extends AbstractMachineContainer<FusionControlComputerTileEntity> {
    public FusionControlComputerContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new FusionControlComputerTileEntity(), new IntArray(AbstractMachineTileEntity.FIELDS_COUNT));
    }

    public FusionControlComputerContainer(int id, PlayerInventory playerInventory, FusionControlComputerTileEntity tileEntity, IIntArray fieldsIn) {
        super(ModContainers.FUSION_CONTROL_COMPUTER, id, tileEntity, fieldsIn);

        this.addSlot(new Slot(this.tileEntity, 0, 56, 35));
        this.addSlot(new SlotOutputOnly(this.tileEntity, 1, 117, 35));

        InventoryUtils.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot == null || !slot.getHasStack()) {
            return itemstack;
        }
        ItemStack itemstack1 = slot.getStack();
        itemstack = itemstack1.copy();

        final int inventorySize = 2;
        final int playerInventoryEnd = inventorySize + 27;
        final int playerHotbarEnd = playerInventoryEnd + 9;

        if (index == 1) {
            if (!this.mergeItemStack(itemstack1, inventorySize, playerHotbarEnd, true)) {
                return ItemStack.EMPTY;
            }

            slot.onSlotChange(itemstack1, itemstack);
        } else if (index != 0) {
            if (this.isSmeltingIngredient(itemstack1) && !this.mergeItemStack(itemstack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            } else if (index < playerInventoryEnd && !this.mergeItemStack(itemstack1, playerInventoryEnd, playerHotbarEnd, false)) {
                    return ItemStack.EMPTY;
            }  else if (index < playerHotbarEnd && !this.mergeItemStack(itemstack1, inventorySize, playerInventoryEnd, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.mergeItemStack(itemstack1, inventorySize, playerHotbarEnd, false)) {
            return ItemStack.EMPTY;
        }

        if (itemstack1.isEmpty()) {
            slot.putStack(ItemStack.EMPTY);
        } else {
            slot.onSlotChanged();
        }

        if (itemstack1.getCount() == itemstack.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(playerIn, itemstack1);

        return itemstack;
    }

    private boolean isSmeltingIngredient(ItemStack stack) {
        // TODO
        return true;
    }
}