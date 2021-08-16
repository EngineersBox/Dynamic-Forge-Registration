package com.engineersbox.expandedfusion.core.elements.energy;

import com.engineersbox.expandedfusion.core.elements.capability.EnergyStorageCapability;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public interface IEnergyHandler {
    EnergyStorageCapability getEnergyImpl();

    default LazyOptional<IEnergyStorage> getEnergy(@Nullable final Direction side) {
        return getEnergyImpl().getCapability(CapabilityEnergy.ENERGY, side);
    }

    default int getEnergyStored() {
        IEnergyStorage energy = getEnergy(null).orElse(new EnergyStorage(100_000));
        return energy.getEnergyStored();
    }

    default int getMaxEnergyStored() {
        IEnergyStorage energy = getEnergy(null).orElse(new EnergyStorage(100_000));
        return energy.getMaxEnergyStored();
    }

    default void setEnergyStoredDirectly(final int value) {
        getEnergy(null).ifPresent(e -> {
            if (e instanceof EnergyStorageCapability) {
                ((EnergyStorageCapability) e).setEnergyDirectly(value);
            }
        });
    }

    default void setMaxEnergyStoredDirectly(final int value) {
        getEnergy(null).ifPresent(e -> {
            if (e instanceof EnergyStorageCapability) {
                ((EnergyStorageCapability) e).setMaxEnergyDirectly(value);
            }
        });
    }

    default void readEnergy(final CompoundNBT tags) {
        setEnergyStoredDirectly(tags.getInt("Energy"));
    }

    default void writeEnergy(final CompoundNBT tags) {
        tags.putInt("Energy", getEnergyStored());
    }
}

