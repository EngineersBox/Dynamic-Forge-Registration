package com.engineersbox.expandedfusion.core.elements.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;

public interface IFluidProvider extends IItemProvider {

    Fluid asFluid();

    @Override
    default Item asItem() {
        return this.asFluid().getFilledBucket();
    }

}
