package com.engineersbox.expandedfusion.core.registration.anonymous.element.builder;

import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.AttributedSupplier;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.fluid.LangMappedFlowingFluid;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.ElementProvider;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FlowingFluidBuilder extends BaseFluidBuilder<FlowingFluid, Fluid> {

    private final AnonymousElement.Builder baseBuilderInstance;
    private final AttributedSupplier<FlowingFluid, Fluid> supplier;
    private ForgeFlowingFluid.Properties propsConfig;
    private final String providerName;

    public FlowingFluidBuilder(final AnonymousElement.Builder baseBuilderInstance,
                               final String providerName) {
        this.baseBuilderInstance = baseBuilderInstance;
        this.providerName = providerName;
        this.supplier = new AttributedSupplier<>(ElementProvider.FLUID);
    }

    @Override
    public FlowingFluidBuilder supplier(final Supplier<FlowingFluid> supplier) {
        this.supplier.supplier(supplier);
        return this;
    }

    public FlowingFluidBuilder properties(final ForgeFlowingFluid.Properties props) {
        this.propsConfig = props;
        return this;
    }

    @Override
    public FlowingFluidBuilder tabGroup(final String tabGroup) {
        this.supplier.tabGroup(tabGroup);
        return this;
    }

    @Override
    public FlowingFluidBuilder tagResource(final ResourceLocation tagResource) {
        this.supplier.tagResource(tagResource);
        return this;
    }

    @Override
    public FlowingFluidBuilder tag(final ITag.INamedTag<Fluid> tag) {
        this.supplier.tag(tag);
        return this;
    }

    @Override
    public FlowingFluidBuilder mirroredTagResource(final ResourceLocation mirroredTagResource) {
        this.supplier.mirroredTagResource(mirroredTagResource);
        return this;
    }

    @Override
    public FlowingFluidBuilder mirroredTag(final ITag.INamedTag<Item> mirroredTag) {
        this.supplier.mirroredTag(mirroredTag);
        return this;
    }

    @Override
    public FlowingFluidBuilder langMappings(final Map<LangKey, String> langMappings) {
        this.supplier.langMappings(langMappings);
        return this;
    }

    @Override
    public FlowingFluidBuilder recipe(final Consumer<Consumer<IFinishedRecipe>> recipeConsumer) {
        this.supplier.recipe(recipeConsumer);
        return this;
    }

    @Override
    public AnonymousElement.Builder construct() {
        if (this.supplier.getSupplier() == null && this.propsConfig != null) {
            if (this.supplier.getLangMappings() == null) {
                this.supplier.supplier(() -> new LangMappedFlowingFluid(propsConfig));
            } else {
                final LangMappedFlowingFluid langMappedFlowingFluid = super.createLangMappedFluid(
                        LangMappedFlowingFluid.class,
                        this.providerName,
                        this.supplier.getLangMappings(),
                        this.propsConfig
                );
                this.supplier.supplier(() -> langMappedFlowingFluid);
                this.supplier.langMappings(null);
            }
        }
        this.baseBuilderInstance.addFlowingFluidSupplier(this.providerName, this.supplier);
        return this.baseBuilderInstance;
    }
}
