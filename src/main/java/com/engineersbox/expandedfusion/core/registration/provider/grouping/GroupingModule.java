package com.engineersbox.expandedfusion.core.registration.provider.grouping;

import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.fluid.FluidImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.fluid.FluidImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.item.ItemImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.item.ItemImplGrouping;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class GroupingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<ImplClassGroupings<BlockImplGrouping>>(){})
            .to(new TypeLiteral<BlockImplClassGrouping>(){});
        bind(new TypeLiteral<ImplClassGroupings<ItemImplGrouping>>(){})
            .to(new TypeLiteral<ItemImplClassGrouping>(){});
        bind(new TypeLiteral<ImplClassGroupings<FluidImplGrouping>>(){})
            .to(new TypeLiteral<FluidImplClassGrouping>(){});
    }
}
