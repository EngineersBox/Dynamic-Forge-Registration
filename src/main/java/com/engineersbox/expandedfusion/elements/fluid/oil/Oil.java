package com.engineersbox.expandedfusion.elements.fluid.oil;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.provider.fluid.FluidBucketProperties;
import com.engineersbox.expandedfusion.core.registration.annotation.provider.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryInjectionContext;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

@LangMetadata(
        nameMapping = Oil.NAME_MAPPING
)
@FluidProvider(
        name = Oil.PROVIDER_NAME,
        bucket = @FluidBucketProperties(
                name = Oil.BUCKET_NAME,
                lang = @LangMetadata(
                        nameMapping = Oil.BUCKET_NAME_MAPPING
                )
        )
)
public class Oil extends ForgeFlowingFluid.Source {
    public static final String PROVIDER_NAME = "oil";
    public static final String NAME_MAPPING = "Oil";
    public static final String BUCKET_NAME = "oil_bucket";
    public static final String BUCKET_NAME_MAPPING = "Oil Bucket";
    public static final String BLOCK_NAME = PROVIDER_NAME;

    public Oil() {
        super(
            new ForgeFlowingFluid.Properties(
                () -> RegistryInjectionContext.getSourceFluid(PROVIDER_NAME),
                () -> RegistryInjectionContext.getFlowingFluid(FlowingOil.PROVIDER_NAME),
                FluidAttributes.builder(
                    ExpandedFusion.getId("block/" + PROVIDER_NAME + "_still"),
                    ExpandedFusion.getId("block/" + PROVIDER_NAME + "_flowing")
                )
            ).block(() -> (FlowingFluidBlock) RegistryInjectionContext.getBlockRegistryObject(Oil.PROVIDER_NAME).get())
            .bucket(() -> RegistryInjectionContext.getItemRegistryObject(Oil.BUCKET_NAME).get())
        );
    }

}
