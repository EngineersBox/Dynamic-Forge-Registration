package com.engineersbox.expandedfusion.elements.block;

import com.engineersbox.expandedfusion.ExpandedFusion;
import com.engineersbox.expandedfusion.core.registration.annotation.anonymous.AnonymousElementRegistrant;
import com.engineersbox.expandedfusion.core.registration.annotation.anonymous.ElementRetriever;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.recipe.CriterionCondition;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

@AnonymousElementRegistrant
public enum Metals {
    ALUMINUM(new AnonymousElement.Builder()
            .block("aluminium_block")
                    .tabGroup(ExpandedFusion.MOD_ID)
                    .langMappings(ImmutableMap.of(LangKey.EN_US, "Aluminium Block"))
                    .properties(
                            AbstractBlock.Properties.create(Material.IRON)
                                    .hardnessAndResistance(4, 20)
                                    .sound(SoundType.METAL),
                            false
                    ).tagResource(new ResourceLocation("forge", "testblock/tag"))
                    .mirroredTagResource(new ResourceLocation("forge", "testitem/tag"))
                    .recipe((final Consumer<IFinishedRecipe> recipe) -> ShapedRecipeBuilder.shapedRecipe(RegistryObjectContext.getBlockRegistryObject("aluminium_block").asBlock())
                            .patternLine("III")
                            .patternLine("IBI")
                            .patternLine("III")
                            .key('I', Tags.Items.INGOTS_IRON)
                            .key('B', Tags.Items.STORAGE_BLOCKS_IRON)
                            .addCriterion("has_iron", CriterionCondition.hasItem(Tags.Items.INGOTS_IRON))
                            .build(recipe)
                    )
                    .construct()
            .build()
    ),
    LEAD(new AnonymousElement.Builder()
            .block("lead_block")
                    .tabGroup(ExpandedFusion.MOD_ID)
                    .langMappings(ImmutableMap.of(LangKey.EN_US, "Lead Block"))
                    .properties(
                            AbstractBlock.Properties.create(Material.IRON)
                                    .hardnessAndResistance(4, 20)
                                    .sound(SoundType.METAL),
                            true
                    ).construct()
            .build()
    ),
    ORCHALLIUM(new AnonymousElement.Builder()
            .block("orchallium_block")
                    .tabGroup(ExpandedFusion.MOD_ID)
                    .langMappings(ImmutableMap.of(LangKey.EN_US, "Orchallium Block"))
                    .properties(
                            AbstractBlock.Properties.create(Material.IRON)
                                    .hardnessAndResistance(4, 20)
                                    .sound(SoundType.METAL),
                            false
                    ).construct()
            .build()
    );

    @ElementRetriever
    public final AnonymousElement element;

    Metals(final AnonymousElement element) {
        this.element = element;
    }
}
