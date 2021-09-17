package com.engineersbox.expandedfusion.core.registration.provider.shim.element;

import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShim;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.FluidRegistryObject;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.fluid.Fluid;

import java.util.function.Supplier;

public class FluidDeferredRegistryShim extends RegistryShim<Fluid> {

    private final Registration registration;

    @Inject
    public FluidDeferredRegistryShim(@Named("modId") final String modID,
                                     final Registration registration) {
        this.modID = modID;
        this.registration = registration;
    }

    public <T extends Fluid> FluidRegistryObject<T> register(final String name, final Supplier<T> fluid) {
        return new FluidRegistryObject<>(this.registration.getFluidRegister().register(name, fluid));
    }

}
