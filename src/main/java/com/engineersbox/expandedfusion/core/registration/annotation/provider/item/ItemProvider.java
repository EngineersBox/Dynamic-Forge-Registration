package com.engineersbox.expandedfusion.core.registration.annotation.provider.item;


import com.engineersbox.expandedfusion.core.registration.annotation.provider.ProvidesElement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ProvidesElement
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ItemProvider {
    String name();
}
