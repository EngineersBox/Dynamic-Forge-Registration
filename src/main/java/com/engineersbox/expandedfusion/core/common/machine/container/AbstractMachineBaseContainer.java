package com.engineersbox.expandedfusion.core.common.machine.container;

import com.engineersbox.expandedfusion.core.common.energy.AbstractEnergyStorageContainer;
import com.engineersbox.expandedfusion.core.common.machine.tileentity.AbstractMachineBaseTileEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;

public abstract class AbstractMachineBaseContainer<T extends AbstractMachineBaseTileEntity> extends AbstractEnergyStorageContainer<T> {
    protected AbstractMachineBaseContainer(final ContainerType<?> type, final int id, final T tileEntityIn, final IIntArray fieldsIn) {
        super(type, id, tileEntityIn, fieldsIn);
    }
}