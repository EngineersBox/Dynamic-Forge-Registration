package com.engineersbox.expandedfusion.register.handlers;

import com.engineersbox.expandedfusion.core.common.block.IBlockProvider;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;

public class BlockRegistryObject<T extends Block> extends RegistryObjectWrapper<T> implements IBlockProvider {
    public BlockRegistryObject(final RegistryObject<T> block) {
        super(block);
    }

    @Override
    public Block asBlock() {
        return registryObject.get();
    }
}
