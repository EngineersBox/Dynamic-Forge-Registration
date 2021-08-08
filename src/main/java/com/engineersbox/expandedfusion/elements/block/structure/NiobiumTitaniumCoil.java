package com.engineersbox.expandedfusion.elements.block.structure;

import com.engineersbox.expandedfusion.register.registry.annotation.block.BaseBlockProperties;
import com.engineersbox.expandedfusion.register.registry.annotation.block.BlockProvider;
import com.engineersbox.expandedfusion.register.registry.provider.BlockImplType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

@BlockProvider(
    name = NiobiumTitaniumCoil.providerName,
    type = BlockImplType.BASE,
    properties = {
        @BaseBlockProperties(material = "IRON")
    }
)
public class NiobiumTitaniumCoil extends Block {

    public static final String providerName = "niobium_titanium_coil";

    public NiobiumTitaniumCoil() {
        super(Block.Properties.create(Material.IRON));
    }

}
