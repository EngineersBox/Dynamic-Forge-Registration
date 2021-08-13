package com.engineersbox.expandedfusion.register.annotation.fluid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FluidProvider {
    String name();
    boolean bucketItem() default true;
    boolean gaseous() default false;
}