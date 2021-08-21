package com.engineersbox.expandedfusion.core.registration.provider.shim;

import com.engineersbox.expandedfusion.core.registration.registryObject.FluidRegistryObject;
import com.engineersbox.expandedfusion.register.Registration;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.fluid.Fluid;

import java.util.function.Supplier;

public class FluidDeferredRegistryShim extends RegistryShim<Fluid> {

    @Inject
    public FluidDeferredRegistryShim(@Named("modId") final String modID) {
        this.modID = modID;
    }

    public <T extends Fluid> FluidRegistryObject<T> register(final String name, final Supplier<T> fluid) {
        return new FluidRegistryObject<>(Registration.FLUIDS.register(name, fluid));
    }

}
