package com.engineersbox.expandedfusion.core.registration.exception.grouping.element.fluid;

import net.minecraftforge.fluids.ForgeFlowingFluid;

public class FluidBucketBindingException extends RuntimeException {

    public FluidBucketBindingException(final Class<?> provider) {
        super(String.format(
                "@FluidBucket annotated provider %s does not inherit from %s",
                provider.getName(),
                ForgeFlowingFluid.Flowing.class.getName()
        ));
    }

}
