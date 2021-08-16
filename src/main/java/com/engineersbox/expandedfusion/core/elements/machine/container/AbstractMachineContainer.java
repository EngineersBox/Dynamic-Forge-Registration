package com.engineersbox.expandedfusion.core.elements.machine.container;

import com.engineersbox.expandedfusion.core.elements.DataField;
import com.engineersbox.expandedfusion.core.elements.machine.tileentity.AbstractMachineTileEntity;
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