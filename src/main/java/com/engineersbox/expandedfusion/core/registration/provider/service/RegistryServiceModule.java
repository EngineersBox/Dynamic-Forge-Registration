package com.engineersbox.expandedfusion.core.registration.provider.service;

import com.engineersbox.expandedfusion.core.registration.provider.service.data.recipe.RecipeDeferredRegistryService;
import com.engineersbox.expandedfusion.core.registration.provider.service.data.tags.TagDeferredRegistryService;
import com.engineersbox.expandedfusion.core.registration.provider.service.element.*;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;

public final class RegistryServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<RegistryService<Block>>(){})
                .to(new TypeLiteral<BlockDeferredRegistryService>(){});
        bind(new TypeLiteral<RegistryService<Item>>(){})
                .to(new TypeLiteral<ItemDeferredRegistryService>(){});
        bind(new TypeLiteral<RegistryService<Fluid>>(){})
                .to(new TypeLiteral<FluidDeferredRegistryService>(){});
        bind(new TypeLiteral<RegistryService<Container>>(){})
                .to(new TypeLiteral<ContainerDeferredRegistryService>(){});
        bind(new TypeLiteral<RegistryService<TileEntity>>(){})
                .to(new TypeLiteral<TileEntityDeferredRegistryService>(){});
        bind(new TypeLiteral<RegistryService<IRecipeSerializer<?>>>(){})
                .to(new TypeLiteral<RecipeDeferredRegistryService>(){});
        bind(new TypeLiteral<RegistryService<ITag.INamedTag<?>>>(){})
                .to(new TypeLiteral<TagDeferredRegistryService>(){});
    }
}
