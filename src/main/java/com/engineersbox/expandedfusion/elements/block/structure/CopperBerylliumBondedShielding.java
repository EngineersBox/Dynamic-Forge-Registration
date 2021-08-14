package com.engineersbox.expandedfusion.elements.block.structure;

import com.engineersbox.expandedfusion.register.annotation.block.BlockProvider;
import com.engineersbox.expandedfusion.register.provider.block.BlockImplType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

@BlockProvider(
    name = CopperBerylliumBondedShielding.PROVIDER_NAME,
    type = BlockImplType.BASE
)
public class CopperBerylliumBondedShielding extends Block {
    public static final String PROVIDER_NAME = "copper_beryllium_bonded_shielding";

    public CopperBerylliumBondedShielding() {
        super(Block.Properties.create(Material.IRON));
    }
}
