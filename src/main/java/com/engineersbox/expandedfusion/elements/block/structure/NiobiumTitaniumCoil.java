package com.engineersbox.expandedfusion.elements.block.structure;

import com.engineersbox.expandedfusion.register.annotation.block.BlockProvider;
import com.engineersbox.expandedfusion.register.provider.block.BlockImplType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

@BlockProvider(
    name = NiobiumTitaniumCoil.PROVIDER_NAME,
    type = BlockImplType.BASE
)
public class NiobiumTitaniumCoil extends Block {

    public static final String PROVIDER_NAME = "niobium_titanium_coil";

    public NiobiumTitaniumCoil() {
        super(Block.Properties.create(Material.IRON));
    }

}
