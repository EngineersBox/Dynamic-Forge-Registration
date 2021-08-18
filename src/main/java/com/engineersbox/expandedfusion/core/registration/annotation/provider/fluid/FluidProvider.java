package com.engineersbox.expandedfusion.core.registration.annotation.provider.fluid;

import com.engineersbox.expandedfusion.core.registration.annotation.provider.ProvidesElement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ProvidesElement
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FluidProvider {
    String name();
    FluidBucketProperties[] bucket() default {};
    boolean gaseous() default false;
}