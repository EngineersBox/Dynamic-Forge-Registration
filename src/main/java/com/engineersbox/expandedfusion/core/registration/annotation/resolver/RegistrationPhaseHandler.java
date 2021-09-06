package com.engineersbox.expandedfusion.core.registration.annotation.resolver;

import com.engineersbox.expandedfusion.core.registration.resolver.ResolverPhase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RegistrationPhaseHandler {
    ResolverPhase value();
}
