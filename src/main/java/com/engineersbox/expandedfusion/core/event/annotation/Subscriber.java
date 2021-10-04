package com.engineersbox.expandedfusion.core.event.annotation;

import com.engineersbox.expandedfusion.core.event.broker.EventBroker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Subscriber {
    int priority() default EventBroker.MAX_PRIORITY;
}
