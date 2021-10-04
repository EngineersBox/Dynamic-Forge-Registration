package com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext;

import com.engineersbox.expandedfusion.core.event.broker.EventBroker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ClientEventHandler {
    int priority() default EventBroker.MAX_PRIORITY;
}
