package com.engineersbox.expandedfusion.core.registration.annotation.data.tag;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(Tags.class)
public @interface Tag {
    String namespace() default "forge";
    String path();
    String mirroredItemTagNamespace() default "forge";
    String mirroredItemTagPath() default "";
    boolean replace() default false;
    /*
     * If this field is specified, use it instead of the value found
     * in @BlockProvider, @ItemProvider or @FluidProvider annotations
     * on the same class
     */
    String provider() default "";
}
