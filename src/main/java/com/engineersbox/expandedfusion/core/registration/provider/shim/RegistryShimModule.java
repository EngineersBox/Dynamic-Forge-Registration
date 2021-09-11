package com.engineersbox.expandedfusion.core.registration.provider.shim;

import com.engineersbox.expandedfusion.core.registration.provider.shim.data.recipe.RecipeDeferredRegistryShim;
import com.engineersbox.expandedfusion.core.registration.provider.shim.data.tags.TagDeferredRegistryShim;
import com.engineersbox.expandedfusion.core.registration.provider.shim.element.*;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;

public final class RegistryShimModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<RegistryShim<Block>>(){})
                .to(new TypeLiteral<BlockDeferredRegistryShim>(){});
        bind(new TypeLiteral<RegistryShim<Item>>(){})
                .to(new TypeLiteral<ItemDeferredRegistryShim>(){});
        bind(new TypeLiteral<RegistryShim<Fluid>>(){})
                .to(new TypeLiteral<FluidDeferredRegistryShim>(){});
        bind(new TypeLiteral<RegistryShim<Container>>(){})
                .to(new TypeLiteral<ContainerDeferredRegistryShim>(){});
        bind(new TypeLiteral<RegistryShim<TileEntity>>(){})
                .to(new TypeLiteral<TileEntityDeferredRegistryShim>(){});
        bind(new TypeLiteral<RegistryShim<IRecipeSerializer<?>>>(){})
                .to(new TypeLiteral<RecipeDeferredRegistryShim>(){});
        bind(new TypeLiteral<RegistryShim<ITag.INamedTag<?>>>(){})
                .to(new TypeLiteral<TagDeferredRegistryShim>(){});
    }
}
