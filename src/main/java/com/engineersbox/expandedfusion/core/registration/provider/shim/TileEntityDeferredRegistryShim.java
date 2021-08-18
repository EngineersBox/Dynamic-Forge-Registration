package com.engineersbox.expandedfusion.core.registration.provider.shim;

import com.engineersbox.expandedfusion.core.elements.block.IBlockProvider;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class TileEntityDeferredRegistryShim extends RegistryShim<TileEntity> {

    @Inject
    public TileEntityDeferredRegistryShim(@Named("modId") final String modID) {
        this.modID = modID;
    }

    public <T extends TileEntity> TileEntityType<T> register(final String name, final Supplier<T> tileFactory, final IBlockProvider block) {
        return register(name, tileFactory, block.asBlock());
    }

    public <T extends TileEntity> TileEntityType<T> register(final String name, final Supplier<T> tileFactory, final Block... blocks) {
        final TileEntityType<T> type = TileEntityType.Builder.create(tileFactory, blocks).build(null);
        return register(name, type);
    }

    public <T extends TileEntity> TileEntityType<T> register(final String name, final TileEntityType<T> type) {
        if (type.getRegistryName() == null) {
            type.setRegistryName(this.getId(name));
        }
        ForgeRegistries.TILE_ENTITIES.register(type);
        return type;
    }

}
