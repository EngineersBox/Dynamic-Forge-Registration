package com.engineersbox.expandedfusion.core.common.machine.container;

import com.engineersbox.expandedfusion.core.common.DataField;
import com.engineersbox.expandedfusion.core.common.machine.tileentity.AbstractMachineTileEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;

public abstract class AbstractMachineContainer<T extends AbstractMachineTileEntity<?>> extends AbstractMachineBaseContainer<T> {
    protected AbstractMachineContainer(final ContainerType<?> containerTypeIn,
                                       final int id,
                                       final T tileEntityIn,
                                       final IIntArray fieldsIn) {
        super(containerTypeIn, id, tileEntityIn, fieldsIn);
    }

    public int getProgress() {
        return fields.get(DataField.PROGRESS.index);
    }

    public int getProcessTime() {
        return fields.get(DataField.PROCESS_TIME.index);
    }
}