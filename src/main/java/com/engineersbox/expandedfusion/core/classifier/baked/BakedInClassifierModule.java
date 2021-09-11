package com.engineersbox.expandedfusion.core.classifier.baked;

import com.engineersbox.expandedfusion.core.classifier.requirement.ClassifierRequirement;
import com.engineersbox.expandedfusion.core.classifier.ImplementationClassifier;
import com.engineersbox.expandedfusion.core.classifier.MultiClassImplementationClassifier;
import com.engineersbox.expandedfusion.core.classifier.requirement.RequirementCondition;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.*;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplType;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplGrouping;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;

public final class BakedInClassifierModule extends AbstractModule {

    private static final MultiClassImplementationClassifier<BlockImplGrouping> blockClassifier = new MultiClassImplementationClassifier.Builder<BlockImplGrouping>()
            .withRequirements(
                    BlockImplType.STATIC.name(),
                    new ClassifierRequirement<>(RequirementCondition.REQUIRED, Block.class, BlockProvider.class, "name")
            ).withRequirements(
                    BlockImplType.INTERACTIVE_TILE_ENTITY.name(),
                    new ClassifierRequirement<>(RequirementCondition.REQUIRED, Block.class,BlockProvider.class, "name"),
                    new ClassifierRequirement<>(RequirementCondition.REQUIRED, TileEntity.class, TileEntityProvider.class, "name"),
                    new ClassifierRequirement<>(RequirementCondition.REQUIRED, Container.class, ContainerProvider.class, "name"),
                    new ClassifierRequirement<>(RequirementCondition.REQUIRED, ContainerScreen.class, ScreenProvider.class, "name"),
                    new ClassifierRequirement<>(RequirementCondition.OPTIONAL, TileEntityRenderer.class, RendererProvider.class, "name")
            ).withRequirements(
                    BlockImplType.RENDERED_TILE_ENTITY.name(),
                    new ClassifierRequirement<>(RequirementCondition.REQUIRED, Block.class, BlockProvider.class, "name"),
                    new ClassifierRequirement<>(RequirementCondition.REQUIRED, TileEntity.class, TileEntityProvider.class, "name"),
                    new ClassifierRequirement<>(RequirementCondition.REQUIRED, TileEntityRenderer.class, RendererProvider.class, "name")
            ).build();

    @Override
    protected void configure() {
        bind(new TypeLiteral<ImplementationClassifier<BlockImplGrouping>>(){})
                .toInstance(blockClassifier);
    }
}
