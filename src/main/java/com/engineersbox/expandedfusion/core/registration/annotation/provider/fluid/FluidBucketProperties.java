package com.engineersbox.expandedfusion.core.registration.annotation.provider.fluid;

public @interface FluidBucketProperties {
    String bucketName();
    String nameMapping();
    boolean canPlaceBucket() default true;
}
