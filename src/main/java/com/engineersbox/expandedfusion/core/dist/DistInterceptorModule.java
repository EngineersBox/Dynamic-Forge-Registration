package com.engineersbox.expandedfusion.core.dist;

import com.engineersbox.expandedfusion.core.dist.annotation.DistBound;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public final class DistInterceptorModule extends AbstractModule {

    @Override
    protected void configure() {
        bindInterceptor(
                Matchers.any(),
                Matchers.annotatedWith(DistBound.class),
                new DistMethodInterceptor()
        );
    }

}
