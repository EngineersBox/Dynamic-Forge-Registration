package com.engineersbox.expandedfusion.core.registration.annotation.block;

import com.engineersbox.expandedfusion.core.registration.provider.elements.block.BlockImplType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface BlockProvider {
    String name();
    BlockImplType type();
    boolean noItem() default false;
    BaseBlockProperties[] properties() default {};
}
