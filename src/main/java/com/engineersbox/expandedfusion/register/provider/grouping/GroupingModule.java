package com.engineersbox.expandedfusion.register.provider.grouping;

import com.engineersbox.expandedfusion.register.provider.grouping.block.BlockImplClassGrouping;
import com.engineersbox.expandedfusion.register.provider.grouping.block.BlockImplGrouping;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class GroupingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<ImplClassGroupings<BlockImplGrouping>>(){})
            .to(new TypeLiteral<BlockImplClassGrouping>(){});
    }
}
