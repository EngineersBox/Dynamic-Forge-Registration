package com.engineersbox.expandedfusion.core.elements.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyStorageItem extends EnergyStorage {
    private final ItemStack stack;

    public EnergyStorageItem(final ItemStack stack,
                             final int capacity,
                             final int maxReceive,
                             final int maxExtract) {
        super(capacity, maxReceive, maxExtract);
        this.stack = stack;
    }

    @Override
    public int receiveEnergy(final int maxReceive, final boolean simulate) {
        if (!canReceive())
            return 0;

        final int energyStored = getEnergyStored();
        final int energyReceived = Math.min(capacity - energyStored, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            setEnergyStored(energyStored + energyReceived);
        return energyReceived;
    }

    @Override
    public int extractEnergy(final int maxExtract,final  boolean simulate) {
        if (!canExtract())
            return 0;

        final int energyStored = getEnergyStored();
        final int energyExtracted = Math.min(energyStored, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            setEnergyStored(energyStored - energyExtracted);
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return stack.getOrCreateTag().getInt("Energy");
    }

    private void setEnergyStored(final int amount) {
        stack.getOrCreateTag().putInt("Energy", amount);
    }
}
