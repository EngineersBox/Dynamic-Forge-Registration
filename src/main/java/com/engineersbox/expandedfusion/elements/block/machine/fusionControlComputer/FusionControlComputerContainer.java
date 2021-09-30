package com.engineersbox.expandedfusion.elements.block.machine.fusionControlComputer;

import com.engineersbox.expandedfusion.core.elements.machine.container.AbstractMachineContainer;
import com.engineersbox.expandedfusion.core.elements.machine.tileentity.AbstractMachineTileEntity;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LocaleEntry;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import com.engineersbox.expandedfusion.core.util.InventoryUtils;
import com.engineersbox.expandedfusion.core.util.SlotOutputOnly;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.ContainerProvider;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

@LangMetadata(locales = @LocaleEntry(key = LangKey.EN_US, mapping = FusionControlComputer.NAME_MAPPING))
@ContainerProvider(name = FusionControlComputer.PROVIDER_NAME)
public class FusionControlComputerContainer extends AbstractMachineContainer<FusionControlComputerTileEntity> {

    @SuppressWarnings("unused")
    public FusionControlComputerContainer(final int id, final PlayerInventory playerInventory) {
        this(id, playerInventory, new FusionControlComputerTileEntity(), new IntArray(AbstractMachineTileEntity.FIELDS_COUNT));
    }

    public FusionControlComputerContainer(final int id,
                                          final PlayerInventory playerInventory,
                                          final FusionControlComputerTileEntity tileEntity,
                                          final IIntArray fieldsIn) {
        super(RegistryObjectContext.getContainerRegistryObject(FusionControlComputer.PROVIDER_NAME).asContainerType(), id, tileEntity, fieldsIn);

        this.addSlot(new Slot(this.tileEntity, 0, 56, 35));
        this.addSlot(new SlotOutputOnly(this.tileEntity, 1, 117, 35));

        InventoryUtils.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);
    }

    @Override
    public ItemStack transferStackInSlot(final PlayerEntity playerIn, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot == null || !slot.getHasStack()) {
            return itemstack;
        }
        final ItemStack itemstack1 = slot.getStack();
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
            if ((this.isSmeltingIngredient(itemstack1) && !this.mergeItemStack(itemstack1, 0, 1, false))
                || (index < playerInventoryEnd && !this.mergeItemStack(itemstack1, playerInventoryEnd, playerHotbarEnd, false))
                || (index < playerHotbarEnd && !this.mergeItemStack(itemstack1, inventorySize, playerInventoryEnd, false))) {
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

    private boolean isSmeltingIngredient(final ItemStack stack) {
        // TODO
        return true;
    }
}