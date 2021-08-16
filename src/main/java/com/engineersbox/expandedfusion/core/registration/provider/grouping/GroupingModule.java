package com.engineersbox.expandedfusion.core.registration.provider.grouping;

import com.engineersbox.expandedfusion.core.registration.provider.grouping.block.BlockImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.block.BlockImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.item.ItemImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.item.ItemImplGrouping;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class GroupingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<ImplClassGroupings<BlockImplGrouping>>(){})
            .to(new TypeLiteral<BlockImplClassGrouping>(){});
        bind(new TypeLiteral<ImplClassGroupings<ItemImplGrouping>>(){})
            .to(new TypeLiteral<ItemImplClassGrouping>(){});
    }
}
