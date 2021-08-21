package com.engineersbox.expandedfusion.core.elements.tileentity;

import com.engineersbox.expandedfusion.core.elements.block.IBlockProvider;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public interface ITileEntityProvider<T extends TileEntity> extends IBlockProvider {

    TileEntityType<T> asTileEntityType();

    TileEntity asTileEntity();

    @Override
    default Block asBlock() {
        return this.asTileEntity().getBlockState().getBlock();
    }

}
