package com.engineersbox.expandedfusion.elements.fluid.oil;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.provider.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryInjectionContext;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

@LangMetadata(
        nameMapping = FlowingOil.NAME_MAPPING
)
@FluidProvider(
    name = FlowingOil.PROVIDER_NAME
)
public class FlowingOil extends ForgeFlowingFluid.Flowing {
    public static final String PROVIDER_NAME = "flowing_oil";
    public static final String NAME_MAPPING = "Flowing Oil";

    public FlowingOil() {
        super(
            new ForgeFlowingFluid.Properties(
                () -> RegistryInjectionContext.getSourceFluid(Oil.PROVIDER_NAME),
                () -> RegistryInjectionContext.getFlowingFluid(PROVIDER_NAME),
                FluidAttributes.builder(
                    ExpandedFusion.getId("block/" + Oil.BLOCK_NAME + "_still"),
                    ExpandedFusion.getId("block/" + Oil.BLOCK_NAME + "_flowing")
                )
            ).block(() -> (FlowingFluidBlock) RegistryInjectionContext.getBlockRegistryObject(Oil.PROVIDER_NAME).get())
            .bucket(() -> RegistryInjectionContext.getItemRegistryObject(Oil.BUCKET_NAME).get())
        );
    }

}
