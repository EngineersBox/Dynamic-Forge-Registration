package com.engineersbox.expandedfusion.core.registration.annotation.provider.fluid;

import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;

public @interface FluidBucketProperties {
    String name();
    boolean canPlace() default true;
    LangMetadata[] lang() default {};
}
