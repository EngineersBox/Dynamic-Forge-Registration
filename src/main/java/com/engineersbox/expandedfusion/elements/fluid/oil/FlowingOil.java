package com.engineersbox.expandedfusion.elements.fluid.oil;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.register.annotation.fluid.FluidProvider;
import com.engineersbox.expandedfusion.register.contexts.RegistryInjectionContext;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

@FluidProvider(
    name = FlowingOil.PROVIDER_NAME
)
public class FlowingOil extends ForgeFlowingFluid.Flowing {

    public static final String PROVIDER_NAME = "flowing_oil";

    public FlowingOil() {
        super(
            new ForgeFlowingFluid.Properties(
                () -> RegistryInjectionContext.getFlowingFluid(Oil.PROVIDER_NAME),
                () -> RegistryInjectionContext.getFlowingFluid(PROVIDER_NAME),
                FluidAttributes.builder(
                    ExpandedFusion.getId("block/" + Oil.BLOCK_NAME + "_still"),
                    ExpandedFusion.getId("block/" + Oil.BLOCK_NAME + "_flowing")
                )
            ).block(() -> (FlowingFluidBlock) RegistryInjectionContext.getBlockRegistryObject(Oil.PROVIDER_NAME).get())
            .bucket(() -> RegistryInjectionContext.getItemRegistryObject(Oil.PROVIDER_NAME).get())
        );
    }

}
