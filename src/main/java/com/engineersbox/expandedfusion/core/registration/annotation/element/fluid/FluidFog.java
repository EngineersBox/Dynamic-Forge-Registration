package com.engineersbox.expandedfusion.core.registration.annotation.element.fluid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FluidFog {
    float red() default 0F;
    float green() default 0F;
    float blue() default 0F;
}
