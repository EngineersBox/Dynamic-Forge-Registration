package com.engineersbox.expandedfusion.register.provider.shim;

import com.google.inject.Inject;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class FluidDeferredRegistryShim extends RegistryShim<Fluid> {

    @Inject
    public FluidDeferredRegistryShim(final String modID) {
        this.modID = modID;
    }

    private <T extends Fluid> T register(final String name, final T fluid) {
        final ResourceLocation id = this.getId(name);
        fluid.setRegistryName(id);
        ForgeRegistries.FLUIDS.register(fluid);
        return fluid;
    }

    private ForgeFlowingFluid.Properties properties(final String name, final Supplier<Fluid> still, final Supplier<Fluid> flowing) {
        final String tex = "block/" + name;
        return new ForgeFlowingFluid.Properties(still, flowing, FluidAttributes.builder(this.getId(tex + "_still"), this.getId(tex + "_flowing")));
    }

    private ForgeFlowingFluid.Properties propertiesGas(final String name, final Supplier<Fluid> still) {
        final String tex = "block/" + name;
        //noinspection ReturnOfNull -- null-returning Supplier for flowing fluid
        return new ForgeFlowingFluid.Properties(still, () -> null, FluidAttributes.builder(this.getId(tex), this.getId(tex)).gaseous());
    }

}
