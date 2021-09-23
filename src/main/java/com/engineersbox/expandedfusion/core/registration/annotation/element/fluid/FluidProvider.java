package com.engineersbox.expandedfusion.core.registration.annotation.element.fluid;

import com.engineersbox.expandedfusion.core.registration.annotation.element.ProvidesElement;

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
    // TODO: Allow users to specify K,V pairs of the form "<METHOD NAME>:<ARG VALUE>" to set fluid block properties
    //       These can be reflectively retrieved from the AbstractBlock class, get any method that returns
    //       AbstractBlock.Properties and isn't create(...) or from(...)
}