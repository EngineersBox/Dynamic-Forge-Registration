package com.engineersbox.expandedfusion.core.registration.annotation.meta;

import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface LocaleEntry {
    LangKey key() default LangKey.EN_US;
    String mapping();
}
