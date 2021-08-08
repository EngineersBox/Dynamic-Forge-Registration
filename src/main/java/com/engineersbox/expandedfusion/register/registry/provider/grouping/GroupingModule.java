package com.engineersbox.expandedfusion.register.registry.provider.grouping;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class GroupingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<ImplClassGroupings<BlockImplGrouping>>(){})
            .to(new TypeLiteral<BlockImplClassGrouping>(){});
    }
}
