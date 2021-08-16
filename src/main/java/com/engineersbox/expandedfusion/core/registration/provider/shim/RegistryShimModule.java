package com.engineersbox.expandedfusion.core.registration.provider.shim;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

public class RegistryShimModule extends AbstractModule {

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
    }
}
