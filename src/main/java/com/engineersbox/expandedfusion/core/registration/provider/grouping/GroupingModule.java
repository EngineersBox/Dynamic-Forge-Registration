package com.engineersbox.expandedfusion.core.registration.provider.grouping;

import com.engineersbox.expandedfusion.core.registration.provider.grouping.anonymous.AnonymousElementImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.anonymous.AnonymousElementImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting.CraftingRecipeImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.crafting.CraftingRecipeImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.implementation.RecipeSerializerImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.recipe.implementation.RecipeSerializerImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.tags.TagImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.tags.TagImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.fluid.FluidImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.fluid.FluidImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.item.ItemImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.item.ItemImplGrouping;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public final class GroupingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<ImplClassGroupings<BlockImplGrouping>>(){})
                .to(new TypeLiteral<BlockImplClassGrouping>(){});
        bind(new TypeLiteral<ImplClassGroupings<ItemImplGrouping>>(){})
                .to(new TypeLiteral<ItemImplClassGrouping>(){});
        bind(new TypeLiteral<ImplClassGroupings<FluidImplGrouping>>(){})
                .to(new TypeLiteral<FluidImplClassGrouping>(){});
        bind(new TypeLiteral<ImplClassGroupings<RecipeSerializerImplGrouping>>(){})
                .to(new TypeLiteral<RecipeSerializerImplClassGrouping>(){});
        bind(new TypeLiteral<ImplClassGroupings<CraftingRecipeImplGrouping>>(){})
                .to(new TypeLiteral<CraftingRecipeImplClassGrouping>(){});
        bind(new TypeLiteral<ImplClassGroupings<AnonymousElementImplGrouping>>(){})
                .to(new TypeLiteral<AnonymousElementImplClassGrouping>(){});
        bind(new TypeLiteral<ImplClassGroupings<TagImplGrouping>>(){})
                .to(new TypeLiteral<TagImplClassGrouping>(){});
    }
}
