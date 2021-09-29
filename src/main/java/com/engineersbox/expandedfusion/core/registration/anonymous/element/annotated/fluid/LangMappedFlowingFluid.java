package com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.fluid;

import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import net.minecraftforge.fluids.ForgeFlowingFluid;

@LangMetadata(locales={})
public class LangMappedFlowingFluid extends ForgeFlowingFluid.Flowing {
    public LangMappedFlowingFluid(Properties properties) {
        super(properties);
    }
}
