package com.engineersbox.expandedfusion.core.elements.machine.tileentity;

import com.engineersbox.expandedfusion.core.elements.DataField;
import com.engineersbox.expandedfusion.core.elements.MachineTier;
import com.engineersbox.expandedfusion.core.elements.tileentity.AbstractEnergyInventoryTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;

public abstract class AbstractMachineBaseTileEntity extends AbstractEnergyInventoryTileEntity {
    public static final int FIELDS_COUNT = 5;
    protected final MachineTier tier;

    protected final IIntArray fields = new IIntArray() {
        @Override
        public int get(final int index) {
            switch (DataField.fromInt(index)) {
                //Minecraft actually sends fields as shorts, so we need to split energy into 2 fields
                case ENERGY_STORED_LOWER:
                    // Energy lower bytes
                    return AbstractMachineBaseTileEntity.this.getEnergyStored() & 0xFFFF;
                case ENERGY_STORED_HIGHER:
                    // Energy upper bytes
                    return (AbstractMachineBaseTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case MAX_ENERGY_STORED_LOWER:
                    // Max energy lower bytes
                    return AbstractMachineBaseTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case MAX_ENERGY_STORED_HIGHER:
                    // Max energy upper bytes
                    return (AbstractMachineBaseTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                default:
                    return 0;
            }
        }

        @Override
        public void set(final int index, final int value) {
            switch (DataField.fromInt(index)) {
                //Minecraft actually sends fields as shorts, so we need to split energy into 2 fields
                case ENERGY_STORED_LOWER:
                    // Energy lower bytes
                    AbstractMachineBaseTileEntity.this.setEnergyStoredDirectly((value & 0xFFFF) + (AbstractMachineBaseTileEntity.this.getEnergyStored() & 0xFFFF0000));
                    return;
                case ENERGY_STORED_HIGHER:
                    // Energy upper bytes
                    AbstractMachineBaseTileEntity.this.setEnergyStoredDirectly((value << 16) + (AbstractMachineBaseTileEntity.this.getEnergyStored() & 0xFFFF));
                    return;
                case MAX_ENERGY_STORED_LOWER:
                    // Max energy lower bytes
                    AbstractMachineBaseTileEntity.this.setMaxEnergyStoredDirectly((value & 0xFFFF) + (AbstractMachineBaseTileEntity.this.getEnergyStored() & 0xFFFF0000));
                    return;
                case MAX_ENERGY_STORED_HIGHER:
                    // Max energy upper bytes
                    AbstractMachineBaseTileEntity.this.setMaxEnergyStoredDirectly((value << 16) + (AbstractMachineBaseTileEntity.this.getEnergyStored() & 0xFFFF));
                    return;
                default:
            }
        }

        @Override
        public int size() {
            return FIELDS_COUNT;
        }
    };

    protected AbstractMachineBaseTileEntity(final TileEntityType<?> typeIn,
                                            final int inventorySize,
                                            final int maxEnergy,
                                            final int maxReceive,
                                            final int maxExtract,
                                            final MachineTier tier) {
        super(typeIn, inventorySize, maxEnergy, maxReceive, maxExtract);
        this.tier = tier;
    }

    public MachineTier getMachineTier() {
        return tier;
    }

    @Override
    public IIntArray getFields() {
        return fields;
    }

    @Override
    public void read(final BlockState state, final CompoundNBT tags) {
        super.read(state, tags);
    }

    @Override
    public CompoundNBT write(final CompoundNBT tags) {
        super.write(tags);
        return tags;
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return super.getUpdateTag();
    }
}
