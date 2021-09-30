package com.engineersbox.expandedfusion.core.registration.annotation.element.block;

import com.engineersbox.expandedfusion.core.registration.annotation.element.ProvidesElement;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ProvidesElement("block")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface BlockProvider {
    String name();
    BlockImplType type() default BlockImplType.STATIC;
    boolean noItem() default false;
    BlockProperties[] properties() default {};
    String tabGroup() default "";
}
