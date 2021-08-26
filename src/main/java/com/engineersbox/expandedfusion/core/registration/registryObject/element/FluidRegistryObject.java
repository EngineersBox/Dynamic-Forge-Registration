package com.engineersbox.expandedfusion.core.registration.registryObject.element;

import com.engineersbox.expandedfusion.core.elements.fluid.IFluidProvider;
import com.engineersbox.expandedfusion.core.registration.registryObject.RegistryObjectWrapper;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fml.RegistryObject;

public class FluidRegistryObject<T extends Fluid> extends RegistryObjectWrapper<T> implements IFluidProvider {

    public FluidRegistryObject(final RegistryObject<T> fluid) {
        super(fluid);
    }

    @Override
    public T asFluid() {
        return registryObject.get();
    }

}
