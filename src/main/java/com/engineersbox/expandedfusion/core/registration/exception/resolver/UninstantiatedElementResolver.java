package com.engineersbox.expandedfusion.core.registration.exception.resolver;

import com.engineersbox.expandedfusion.core.registration.resolver.ResolverPhase;

public class UninstantiatedElementResolver extends RuntimeException {

    public UninstantiatedElementResolver(final ResolverPhase resolverPhase) {
        super(String.format(
                "Attempted to register elements of resolver that has not been instantiated: %s",
                resolverPhase
        ));
    }

}
