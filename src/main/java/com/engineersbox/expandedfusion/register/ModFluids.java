package com.engineersbox.expandedfusion.register;

import com.engineersbox.expandedfusion.ExpandedFusion;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public final class ModFluids {

    private ModFluids() {}

    public static void registerFluids(final RegistryEvent.Register<Fluid> event) {
    }

    private static <T extends Fluid> T register(final String name, final T fluid) {
        final ResourceLocation id = ExpandedFusion.getId(name);
        fluid.setRegistryName(id);
        ForgeRegistries.FLUIDS.register(fluid);
        return fluid;
    }

    private static ForgeFlowingFluid.Properties properties(final String name, final Supplier<Fluid> still, final Supplier<Fluid> flowing) {
        final String tex = "block/" + name;
        return new ForgeFlowingFluid.Properties(still, flowing, FluidAttributes.builder(ExpandedFusion.getId(tex + "_still"), ExpandedFusion.getId(tex + "_flowing")));
    }

    private static ForgeFlowingFluid.Properties propertiesGas(final String name, final Supplier<Fluid> still) {
        final String tex = "block/" + name;
        //noinspection ReturnOfNull -- null-returning Supplier for flowing fluid
        return new ForgeFlowingFluid.Properties(still, () -> null, FluidAttributes.builder(ExpandedFusion.getId(tex), ExpandedFusion.getId(tex)).gaseous());
    }
}
