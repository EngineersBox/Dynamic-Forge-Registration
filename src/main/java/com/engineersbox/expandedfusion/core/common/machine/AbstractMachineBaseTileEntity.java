package com.engineersbox.expandedfusion.core.common.machine;

import com.engineersbox.expandedfusion.core.common.MachineTier;
import com.engineersbox.expandedfusion.core.common.tileentity.AbstractEnergyInventoryTileEntity;
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
        public int get(int index) {
            switch (index) {
                //Minecraft actually sends fields as shorts, so we need to split energy into 2 fields
                case 0:
                    // Energy lower bytes
                    return AbstractMachineBaseTileEntity.this.getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (AbstractMachineBaseTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2:
                    // Max energy lower bytes
                    return AbstractMachineBaseTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3:
                    // Max energy upper bytes
                    return (AbstractMachineBaseTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                //Minecraft actually sends fields as shorts, so we need to split energy into 2 fields
                case 0:
                    // Energy lower bytes
                    AbstractMachineBaseTileEntity.this.setEnergyStoredDirectly((value & 0xFFFF) + (AbstractMachineBaseTileEntity.this.getEnergyStored() & 0xFFFF0000));
                    return;
                case 1:
                    // Energy upper bytes
                    AbstractMachineBaseTileEntity.this.setEnergyStoredDirectly((value << 16) + (AbstractMachineBaseTileEntity.this.getEnergyStored() & 0xFFFF));
                    return;
                case 2:
                    // Max energy lower bytes
                    AbstractMachineBaseTileEntity.this.setMaxEnergyStoredDirectly((value & 0xFFFF) + (AbstractMachineBaseTileEntity.this.getEnergyStored() & 0xFFFF0000));
                    return;
                case 3:
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

    protected AbstractMachineBaseTileEntity(TileEntityType<?> typeIn, int inventorySize, int maxEnergy, int maxReceive, int maxExtract, MachineTier tier) {
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
    public void read(BlockState state, CompoundNBT tags) {
        super.read(state, tags);
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        super.write(tags);
        return tags;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return super.getUpdateTag();
    }
}
