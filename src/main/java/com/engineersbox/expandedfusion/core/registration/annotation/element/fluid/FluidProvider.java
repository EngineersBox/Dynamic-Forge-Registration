package com.engineersbox.expandedfusion.core.registration.annotation.element.fluid;

import com.engineersbox.expandedfusion.core.registration.annotation.element.ProvidesElement;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProperties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ProvidesElement("fluid")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FluidProvider {
    String name();
    boolean gaseous() default false;
    BlockProperties[] blockProperties() default {};
}