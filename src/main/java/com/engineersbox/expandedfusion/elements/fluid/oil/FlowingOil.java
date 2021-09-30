package com.engineersbox.expandedfusion.elements.fluid.oil;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LocaleEntry;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

@LangMetadata(locales =  @LocaleEntry(key = LangKey.EN_US, mapping = FlowingOil.NAME_MAPPING))
@FluidProvider(name = FlowingOil.PROVIDER_NAME)
public class FlowingOil extends ForgeFlowingFluid.Flowing {
    public static final String PROVIDER_NAME = "flowing_oil";
    public static final String NAME_MAPPING = "Flowing Oil";

    public FlowingOil() {
        super(
            new ForgeFlowingFluid.Properties(
                () -> RegistryObjectContext.getSourceFluidRegistryObject(Oil.PROVIDER_NAME).asFluid(),
                () -> RegistryObjectContext.getFlowingFluidRegistryObject(PROVIDER_NAME).asFluid(),
                FluidAttributes.builder(
                    ExpandedFusion.getId("block/" + Oil.BLOCK_NAME + "_still"),
                    ExpandedFusion.getId("block/" + Oil.BLOCK_NAME + "_flowing")
                )
            ).block(() -> (FlowingFluidBlock) RegistryObjectContext.getBlockRegistryObject(Oil.PROVIDER_NAME).asBlock())
            .bucket(() -> RegistryObjectContext.getItemRegistryObject(Oil.BUCKET_NAME).asItem())
        );
    }

}
