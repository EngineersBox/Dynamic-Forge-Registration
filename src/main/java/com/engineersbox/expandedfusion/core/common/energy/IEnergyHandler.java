package com.engineersbox.expandedfusion.core.common.energy;

import com.engineersbox.expandedfusion.core.common.capability.EnergyStorageImpl;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public interface IEnergyHandler {
    EnergyStorageImpl getEnergyImpl();

    default LazyOptional<IEnergyStorage> getEnergy(@Nullable Direction side) {
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

    default void setEnergyStoredDirectly(int value) {
        getEnergy(null).ifPresent(e -> {
            if (e instanceof EnergyStorageImpl) {
                ((EnergyStorageImpl) e).setEnergyDirectly(value);
            }
        });
    }

    default void setMaxEnergyStoredDirectly(int value) {
        getEnergy(null).ifPresent(e -> {
            if (e instanceof EnergyStorageImpl) {
                ((EnergyStorageImpl) e).setMaxEnergyDirectly(value);
            }
        });
    }

    default void readEnergy(CompoundNBT tags) {
        setEnergyStoredDirectly(tags.getInt("Energy"));
    }

    default void writeEnergy(CompoundNBT tags) {
        tags.putInt("Energy", getEnergyStored());
    }
}

