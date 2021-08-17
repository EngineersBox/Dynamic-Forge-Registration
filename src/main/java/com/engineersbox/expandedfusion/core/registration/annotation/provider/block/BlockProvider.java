package com.engineersbox.expandedfusion.core.registration.annotation.provider.block;

import com.engineersbox.expandedfusion.core.registration.annotation.provider.ProvidesElement;
import com.engineersbox.expandedfusion.core.registration.provider.elements.block.BlockImplType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ProvidesElement
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface BlockProvider {
    String name();
    BlockImplType type();
    boolean noItem() default false; // TODO: Handle this is provider registration
    BaseBlockProperties[] properties() default {};
}
