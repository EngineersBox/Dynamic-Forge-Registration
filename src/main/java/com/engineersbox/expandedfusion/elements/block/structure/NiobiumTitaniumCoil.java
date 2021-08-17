package com.engineersbox.expandedfusion.elements.block.structure;

import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.provider.block.BlockProvider;
import com.engineersbox.expandedfusion.core.registration.provider.elements.block.BlockImplType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

@LangMetadata(
        nameMapping = NiobiumTitaniumCoil.NAME_MAPPING
)
@BlockProvider(
    name = NiobiumTitaniumCoil.PROVIDER_NAME,
    type = BlockImplType.BASE
)
public class NiobiumTitaniumCoil extends Block {
    public static final String PROVIDER_NAME = "niobium_titanium_coil";
    public static final String NAME_MAPPING = "Niobium-Titanium Coil";

    public NiobiumTitaniumCoil() {
        super(Block.Properties.create(Material.IRON));
    }

}
