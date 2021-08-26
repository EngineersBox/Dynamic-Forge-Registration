package com.engineersbox.expandedfusion.core.registration.registryObject.element;

import com.engineersbox.expandedfusion.core.elements.block.IBlockProvider;
import com.engineersbox.expandedfusion.core.registration.registryObject.RegistryObjectWrapper;
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
