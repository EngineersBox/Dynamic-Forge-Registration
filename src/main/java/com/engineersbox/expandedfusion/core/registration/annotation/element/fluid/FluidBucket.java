package com.engineersbox.expandedfusion.core.registration.annotation.element.fluid;

import com.engineersbox.expandedfusion.core.registration.annotation.element.ProvidesElement;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ProvidesElement("item")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FluidBucket {
    String name();
    boolean canPlace() default true;
    LangMetadata[] lang() default {};
    String tabGroup() default "";
}
