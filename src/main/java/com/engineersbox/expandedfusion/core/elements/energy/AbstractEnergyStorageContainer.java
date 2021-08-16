package com.engineersbox.expandedfusion.core.elements.energy;

import com.engineersbox.expandedfusion.core.elements.tileentity.AbstractEnergyInventoryTileEntity;
import com.engineersbox.expandedfusion.core.util.math.MathUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;

public class AbstractEnergyStorageContainer<T extends AbstractEnergyInventoryTileEntity> extends Container {
    protected final T tileEntity;
    protected final IIntArray fields;

    protected AbstractEnergyStorageContainer(final ContainerType<?> type,
                                             final int id,
                                             final T tileEntityIn,
                                             final IIntArray fieldsIn) {
        super(type, id);
        this.tileEntity = tileEntityIn;
        this.fields = fieldsIn;

        trackIntArray(this.fields);
    }

    @Override
    public boolean canInteractWith(final PlayerEntity playerIn) {
        // TODO
        return true;
    }

    public T getTileEntity() {
        return tileEntity;
    }

    public IIntArray getFields() {
        return fields;
    }

    public int getEnergyStored() {
        final int upper = fields.get(1) & 0xFFFF;
        final int lower = fields.get(0) & 0xFFFF;
        return (upper << 16) + lower;
    }

    public int getMaxEnergyStored() {
        final int upper = fields.get(3) & 0xFFFF;
        final int lower = fields.get(2) & 0xFFFF;
        return (upper << 16) + lower;
    }

    public int getEnergyBarHeight() {
        final int max = getMaxEnergyStored();
        final int energyClamped = MathUtils.clamp(getEnergyStored(), 0, max);
        return max > 0 ? 50 * energyClamped / max : 0;
    }
}
