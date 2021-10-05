package com.engineersbox.expandedfusion.elements.block.structure;

import com.engineersbox.expandedfusion.core.registration.annotation.data.tag.Tag;
import com.engineersbox.expandedfusion.core.registration.annotation.data.tag.Tags;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LocaleEntry;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

@LangMetadata(locales = @LocaleEntry(key = LangKey.EN_US, mapping = NiobiumTitaniumCoil.NAME_MAPPING))
@BlockProvider(
        name = NiobiumTitaniumCoil.PROVIDER_NAME,
        type = BlockImplType.STATIC
)
@Tag(
        path = "blocks/" + NiobiumTitaniumCoil.PROVIDER_NAME,
        mirroredItemTagPath = "blocks/" + NiobiumTitaniumCoil.PROVIDER_NAME
)
public class NiobiumTitaniumCoil extends Block {
    public static final String PROVIDER_NAME = "niobium_titanium_coil";
    public static final String NAME_MAPPING = "Niobium-Titanium Coil";

    public NiobiumTitaniumCoil() {
        super(AbstractBlock.Properties.create(Material.IRON).setRequiresTool().hardnessAndResistance(6.0F, 20.0F));
    }

}
