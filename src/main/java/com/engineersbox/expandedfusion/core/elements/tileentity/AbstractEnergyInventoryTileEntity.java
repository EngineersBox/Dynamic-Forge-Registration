package com.engineersbox.expandedfusion.core.elements.tileentity;

import com.engineersbox.expandedfusion.core.sync.annotation.SyncVariable;
import com.engineersbox.expandedfusion.core.elements.DataField;
import com.engineersbox.expandedfusion.core.elements.capability.EnergyStorageCapability;
import com.engineersbox.expandedfusion.core.elements.energy.IEnergyHandler;
import com.engineersbox.expandedfusion.core.sync.SyncHandler;
import com.engineersbox.expandedfusion.core.util.EnergyUtils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractEnergyInventoryTileEntity extends LockableSidedInventoryTileEntity implements IEnergyHandler, ITickableTileEntity {
    protected final EnergyStorageCapability energy;
    private final int maxExtract;

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(final int index) {
            switch (DataField.fromInt(index)) {
                //Minecraft actually sends fields as shorts, so we need to split energy into 2 fields
                case ENERGY_STORED_LOWER:
                    // Energy lower bytes
                    return AbstractEnergyInventoryTileEntity.this.getEnergyStored() & 0xFFFF;
                case ENERGY_STORED_HIGHER:
                    // Energy upper bytes
                    return (AbstractEnergyInventoryTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case MAX_ENERGY_STORED_LOWER:
                    // Max energy lower bytes
                    return AbstractEnergyInventoryTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case MAX_ENERGY_STORED_HIGHER:
                    // Max energy upper bytes
                    return (AbstractEnergyInventoryTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                default:
                    return 0;
            }
        }

        @Override
        public void set(final int index, final int value) {
            getEnergyImpl().setEnergyDirectly(value);
        }

        @Override
        public int size() {
            return 4;
        }
    };

    protected AbstractEnergyInventoryTileEntity(final TileEntityType<?> typeIn,
                                                final int inventorySize,
                                                final int maxEnergy,
                                                final int maxReceive,
                                                final int maxExtract) {
        super(typeIn, inventorySize);
        this.energy = new EnergyStorageCapability(maxEnergy, maxReceive, maxExtract, this);
        this.maxExtract = maxExtract;
    }

    @Override
    public EnergyStorageCapability getEnergyImpl() {
        return energy;
    }

    public IIntArray getFields() {
        return fields;
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        if (maxExtract > 0) {
            EnergyUtils.trySendToNeighbors(world, pos, this, maxExtract);
        }
    }

    @Override
    public void read(final BlockState state, final CompoundNBT tags) {
        super.read(state, tags);
        SyncHandler.readSyncVars(this, tags);
        readEnergy(tags);
    }

    @Override
    public CompoundNBT write(final CompoundNBT tags) {
        super.write(tags);
        SyncHandler.writeSyncVars(this, tags, SyncVariable.Type.WRITE);
        writeEnergy(tags);
        return tags;
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        SyncHandler.readSyncVars(this, packet.getNbtCompound());
        readEnergy(packet.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        SyncHandler.writeSyncVars(this, tags, SyncVariable.Type.PACKET);
        writeEnergy(tags);
        return tags;
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (!this.removed && cap == CapabilityEnergy.ENERGY) {
            return getEnergy(side).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        energy.invalidate();
    }
}
