package com.engineersbox.expandedfusion.register.annotation.block;

import com.engineersbox.expandedfusion.register.provider.elements.block.BlockImplType;

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
