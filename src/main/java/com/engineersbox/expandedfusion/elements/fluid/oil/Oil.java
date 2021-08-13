package com.engineersbox.expandedfusion.elements.fluid.oil;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.register.annotation.fluid.FluidProvider;
import com.engineersbox.expandedfusion.register.contexts.RegistryInjectionContext;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

@FluidProvider(
        name = Oil.PROVIDER_NAME
)
public class Oil extends ForgeFlowingFluid.Source {

    public static final String PROVIDER_NAME = "oil";
    public static final String BLOCK_NAME = PROVIDER_NAME;

    public Oil() {
        super(
            new ForgeFlowingFluid.Properties(
                () -> RegistryInjectionContext.getFlowingFluid(PROVIDER_NAME),
                () -> RegistryInjectionContext.getFlowingFluid(FlowingOil.PROVIDER_NAME),
                FluidAttributes.builder(
                    ExpandedFusion.getId("block/" + PROVIDER_NAME + "_still"),
                    ExpandedFusion.getId("block/" + PROVIDER_NAME + "_flowing")
                )
            ).block(() -> (FlowingFluidBlock) RegistryInjectionContext.getBlockRegistryObject(Oil.PROVIDER_NAME).get())
            .bucket(() -> RegistryInjectionContext.getItemRegistryObject(Oil.PROVIDER_NAME).get())
        );
    }

}
