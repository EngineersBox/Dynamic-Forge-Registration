package com.engineersbox.expandedfusion.core.util;

import com.engineersbox.expandedfusion.core.elements.energy.IEnergyHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public final class EnergyUtils {
    private EnergyUtils() {throw new IllegalAccessError("Utility class");}

    public static void trySendToNeighbors(final IBlockReader world, final BlockPos pos, final IEnergyHandler energyHandler, final int maxSend) {
        for (final Direction side : Direction.values()) {
            if (energyHandler.getEnergyStored() == 0) {
                return;
            }
            trySendTo(world, pos, energyHandler, maxSend, side);
        }
    }

    public static void trySendTo(final IBlockReader world, final BlockPos pos, final IEnergyHandler energyHandler, final int maxSend, final Direction side) {
        final TileEntity tileEntity = world.getTileEntity(pos.offset(side));
        if (tileEntity != null) {
            final IEnergyStorage energy = energyHandler.getEnergy(side).orElse(new EnergyStorage(0));
            tileEntity.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).ifPresent(other -> trySendEnergy(maxSend, energy, other));
        }
    }

    private static void trySendEnergy(final int maxSend, final IEnergyStorage energy, final IEnergyStorage other) {
        if (other.canReceive()) {
            final int toSend = energy.extractEnergy(maxSend, true);
            final int sent = other.receiveEnergy(toSend, false);
            if (sent > 0) {
                energy.extractEnergy(sent, false);
            }
        }
    }

    /**
     * Gets the energy capability for the block at the given position. If it does not have an energy
     * capability, or the block is not a tile entity, this returns null.
     *
     * @param world The world
     * @param pos   The position to check
     * @return The energy capability, or null if not present
     */
    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static IEnergyStorage getEnergy(final IWorldReader world, final BlockPos pos) {
        if (!world.isAreaLoaded(pos, 1)) return null;
        final TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity != null ? tileEntity.getCapability(CapabilityEnergy.ENERGY).orElse(null) : null;
    }

    /**
     * Gets the energy capability of the object (item, etc), or null if it does not have one.
     *
     * @param provider The capability provider
     * @return The energy capability, or null if not present
     */
    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static IEnergyStorage getEnergy(final ICapabilityProvider provider) {
        return provider.getCapability(CapabilityEnergy.ENERGY).orElse(null);
    }

    /**
     * Gets the energy capability of the object (item, etc), or null if it does not have one. Tries
     * to get the capability for the given side first, then null side.
     *
     * @param provider The capability provider
     * @param side     The side being accessed
     * @return The energy capability, or null if not present
     */
    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static IEnergyStorage getEnergyFromSideOrNull(final ICapabilityProvider provider, final Direction side) {
        return provider.getCapability(CapabilityEnergy.ENERGY, side).orElse(provider.getCapability(CapabilityEnergy.ENERGY).orElse(null));
    }
}

