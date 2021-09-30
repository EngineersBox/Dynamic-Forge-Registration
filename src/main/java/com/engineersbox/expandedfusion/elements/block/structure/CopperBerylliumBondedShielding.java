package com.engineersbox.expandedfusion.elements.block.structure;

import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LocaleEntry;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

@LangMetadata(locales = @LocaleEntry(key = LangKey.EN_US, mapping = CopperBerylliumBondedShielding.NAME_MAPPING))
@BlockProvider(
        name = CopperBerylliumBondedShielding.PROVIDER_NAME,
        type = BlockImplType.STATIC
)
public class CopperBerylliumBondedShielding extends Block {
    public static final String PROVIDER_NAME = "copper_beryllium_bonded_shielding";
    public static final String NAME_MAPPING = "Copper-Beryllium Bonded Shielding";

    public CopperBerylliumBondedShielding() {
        super(AbstractBlock.Properties.create(Material.IRON).setRequiresTool().hardnessAndResistance(6.0F, 20.0F));
    }
}
