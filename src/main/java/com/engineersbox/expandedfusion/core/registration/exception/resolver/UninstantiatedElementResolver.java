package com.engineersbox.expandedfusion.core.registration.exception.resolver;

import com.engineersbox.expandedfusion.core.registration.resolver.ResolverType;

public class UninstantiatedElementResolver extends RuntimeException {

    public UninstantiatedElementResolver(final ResolverType resolverType) {
        super(String.format(
                "Attempted to register elements of resolver that has not been instantiated: %s",
                resolverType
        ));
    }

}
