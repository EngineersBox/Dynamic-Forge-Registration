package com.engineersbox.expandedfusion.core.registration.registryObject;

import com.engineersbox.expandedfusion.core.elements.tileentity.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

public class TileEntityRegistryObject<T extends TileEntity> extends RegistryObjectWrapper<TileEntityType<T>> implements ITileEntityProvider<T> {

    public TileEntityRegistryObject(final RegistryObject<TileEntityType<T>> tileEntity) {
        super(tileEntity);
    }

    @Override
    public TileEntityType<T> asTileEntityType() {
        return this.registryObject.get();
    }

    @Override
    public TileEntity asTileEntity() {
        return this.asTileEntityType().create();
    }
}
