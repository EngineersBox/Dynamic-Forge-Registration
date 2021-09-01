package com.engineersbox.expandedfusion.core.registration.annotation.element.fluid;

import com.engineersbox.expandedfusion.core.registration.annotation.element.ProvidesElement;

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
    String itemGroup() default "";  // TODO: Handle this
}