package com.engineersbox.expandedfusion.elements.block;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.core.registration.annotation.anonymous.AnonymousElementRegistrant;
import com.engineersbox.expandedfusion.core.registration.annotation.anonymous.ElementRetriever;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

@AnonymousElementRegistrant
public enum Metals {
    ALUMINUM(new AnonymousElement.Builder()
            .block(
                    "aluminium_block",
                    ImmutableMap.of(
                            LangKey.EN_US, "Aluminium Block"
                    ),
                    ExpandedFusion.MOD_ID,
                    AbstractBlock.Properties.create(Material.IRON)
                            .hardnessAndResistance(4, 20)
                            .sound(SoundType.METAL),
                    new ResourceLocation("forge", "testblock/tag"),
                    new ResourceLocation("forge", "testitem/tag")
            ).build()
    ),
    LEAD(new AnonymousElement.Builder()
            .block(
                    "lead_block",
                    ImmutableMap.of(
                            LangKey.EN_US, "Lead Block"
                    ),
                    ExpandedFusion.MOD_ID,
                    AbstractBlock.Properties.create(Material.IRON)
                            .hardnessAndResistance(4, 20)
                            .sound(SoundType.METAL)
            ).build()
    );

    @ElementRetriever
    public final AnonymousElement element;

    Metals(final AnonymousElement element) {
        this.element = element;
    }
}
