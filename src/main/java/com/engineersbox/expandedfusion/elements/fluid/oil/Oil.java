package com.engineersbox.expandedfusion.elements.fluid.oil;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.core.registration.annotation.data.tag.Tag;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProperties;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidFog;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidBucket;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LocaleEntry;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

@LangMetadata(locales = @LocaleEntry(key = LangKey.EN_US, mapping = Oil.NAME_MAPPING))
@FluidBucket(
        name = Oil.BUCKET_NAME,
        lang = @LangMetadata(
                locales =  @LocaleEntry(key = LangKey.EN_US, mapping = Oil.BUCKET_NAME_MAPPING)
        ),
        tabGroup = Oil.BUCKET_TAB_GROUP
)
@Tag(path = "fluids/oil")
@FluidFog(
        red = 0.02F,
        green = 0.02F,
        blue = 0.02F
)
@FluidProvider(
        name = Oil.PROVIDER_NAME,
        blockProperties = @BlockProperties(
                material = "WATER",
                doesNotBlockMovement = true,
                hardness = 100.0F,
                resistance = 100.0F,
                noDrops = true
        )
)
public class Oil extends ForgeFlowingFluid.Source {
    public static final String PROVIDER_NAME = "oil";
    public static final String NAME_MAPPING = "Oil";
    public static final String BUCKET_NAME = "oil_bucket";
    public static final String BUCKET_NAME_MAPPING = "Oil Bucket";
    public static final String BUCKET_TAB_GROUP = "expandedfusion_machines";
    public static final String BLOCK_NAME = PROVIDER_NAME;

    public Oil() {
        super(
            new ForgeFlowingFluid.Properties(
                () -> RegistryObjectContext.getSourceFluidRegistryObject(PROVIDER_NAME).asFluid(),
                () -> RegistryObjectContext.getFlowingFluidRegistryObject(FlowingOil.PROVIDER_NAME).asFluid(),
                FluidAttributes.builder(
                    ExpandedFusion.getId("block/" + PROVIDER_NAME + "_still"),
                    ExpandedFusion.getId("block/" + PROVIDER_NAME + "_flowing")
                )
            ).block(() -> (FlowingFluidBlock) RegistryObjectContext.getBlockRegistryObject(Oil.PROVIDER_NAME).asBlock())
            .bucket(() -> RegistryObjectContext.getItemRegistryObject(Oil.BUCKET_NAME).asItem())
        );
    }

}
