package com.engineersbox.expandedfusion.core.elements.capability;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyStorageCapabilityProvider extends EnergyStorage implements ICapabilityProvider {
    private final LazyOptional<EnergyStorageCapabilityProvider> lazy;

    public EnergyStorageCapabilityProvider(final int capacity, final int maxReceive, final int maxExtract) {
        super(capacity, maxReceive, maxExtract, 0);
        this.lazy = LazyOptional.of(() -> this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        return CapabilityEnergy.ENERGY.orEmpty(cap, lazy.cast());
    }

    public void invalidate() {
        this.lazy.invalidate();
    }
}
