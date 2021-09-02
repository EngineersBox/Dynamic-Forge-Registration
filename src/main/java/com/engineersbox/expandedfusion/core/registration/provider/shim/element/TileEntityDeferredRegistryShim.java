package com.engineersbox.expandedfusion.core.registration.provider.shim.element;

import com.engineersbox.expandedfusion.core.elements.block.IBlockProvider;
import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShim;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.TileEntityRegistryObject;
import com.engineersbox.expandedfusion.core.registration.contexts.Registration;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.function.Supplier;

public class TileEntityDeferredRegistryShim extends RegistryShim<TileEntity> {

    @Inject
    public TileEntityDeferredRegistryShim(@Named("modId") final String modID) {
        this.modID = modID;
    }

    public <T extends TileEntity> TileEntityRegistryObject<T> register(final String name, final Supplier<T> tileFactory, final IBlockProvider block) {
        return register(name, tileFactory, block.asBlock());
    }

    public <T extends TileEntity> TileEntityRegistryObject<T> register(final String name, final Supplier<T> tileFactory, final Block... blocks) {
        final TileEntityType<T> type = TileEntityType.Builder.create(tileFactory, blocks).build(null);
        return register(name, () -> type);
    }

    public <T extends TileEntity> TileEntityRegistryObject<T> register(final String name, final Supplier<TileEntityType<T>> type) {
        return new TileEntityRegistryObject<>(Registration.TILE_ENTITIES.register(name, type));
    }

}
