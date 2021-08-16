package com.engineersbox.expandedfusion.core.elements.capability;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumMap;

public class EnergyStorageCapability extends EnergyStorageCapabilityProvider {
    private final EnumMap<Direction, LazyOptional<Connection>> connections = new EnumMap<>(Direction.class);
    private final TileEntity tileEntity;

    public EnergyStorageCapability(final int capacity,
                                   final int maxReceive,
                                   final int maxExtract,
                                   final TileEntity tileEntity) {
        super(capacity, maxReceive, maxExtract);
        this.tileEntity = tileEntity;
        Arrays.stream(Direction.values()).forEach(d -> connections.put(d, LazyOptional.of(Connection::new)));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap,
                                             @Nullable final Direction side) {
        if (side == null) return super.getCapability(cap, null);
        return CapabilityEnergy.ENERGY.orEmpty(cap, connections.get(side).cast());
    }

    @Override
    public void invalidate() {
        super.invalidate();
        connections.values().forEach(LazyOptional::invalidate);
    }

    /**
     * Add energy, bypassing max receive limit. Useful for generators, which would normally not
     * receive energy from other blocks.
     *
     * @param amount The amount of energy
     */
    public void createEnergy(final int amount) {
        this.energy = Math.min(this.energy + amount, getMaxEnergyStored());
    }

    /**
     * Remove energy, bypassing max extract limit. Useful for machines which consume energy, which
     * would normally not send energy to other blocks.
     *
     * @param amount The amount of energy to remove
     */
    public void consumeEnergy(final int amount) {
        this.energy = Math.max(this.energy - amount, 0);
    }

    /**
     * Sets energy directly. Should only be used for syncing data from server to client.
     *
     * @param amount The new amount of stored energy
     */
    public void setEnergyDirectly(final int amount) {
        this.energy = amount;
    }

    public void setMaxEnergyDirectly(final int amount) {
        this.capacity = amount;
    }

    /**
     * Wrapper which will prevent energy from being sent back to the sender on the same tick
     */
    public class Connection implements IEnergyStorage {
        private long lastReceiveTick;

        @Override
        public int receiveEnergy(final int maxReceive, final boolean simulate) {
            final World world = EnergyStorageCapability.this.tileEntity.getWorld();
            if (world == null) return 0;

            final int received = EnergyStorageCapability.this.receiveEnergy(maxReceive, simulate);
            if (received > 0 && !simulate)
                this.lastReceiveTick = world.getGameTime();
            return received;
        }

        @Override
        public int extractEnergy(final int maxExtract, final boolean simulate) {
            final World world = EnergyStorageCapability.this.tileEntity.getWorld();
            if (world == null) return 0;

            final long time = world.getGameTime();
            if (time != this.lastReceiveTick) {
                return EnergyStorageCapability.this.extractEnergy(maxExtract, simulate);
            }
            return 0;
        }

        @Override
        public int getEnergyStored() {
            return EnergyStorageCapability.this.getEnergyStored();
        }

        @Override
        public int getMaxEnergyStored() {
            return EnergyStorageCapability.this.getMaxEnergyStored();
        }

        @Override
        public boolean canExtract() {
            return EnergyStorageCapability.this.canExtract();
        }

        @Override
        public boolean canReceive() {
            return EnergyStorageCapability.this.canReceive();
        }
    }
}
