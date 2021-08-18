package com.engineersbox.expandedfusion.core.registration.provider.shim;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class FluidDeferredRegistryShim extends RegistryShim<Fluid> {

    @Inject
    public FluidDeferredRegistryShim(@Named("modId") final String modID) {
        this.modID = modID;
    }

    public <T extends Fluid> T register(final String name, final T fluid) {
        final ResourceLocation id = this.getId(name);
        fluid.setRegistryName(id);
        ForgeRegistries.FLUIDS.register(fluid);
        return fluid;
    }

}
