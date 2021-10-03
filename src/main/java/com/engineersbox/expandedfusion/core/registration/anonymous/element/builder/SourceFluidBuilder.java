package com.engineersbox.expandedfusion.core.registration.anonymous.element.builder;

import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.AttributedSupplier;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.fluid.LangMappedSourceFluid;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.ElementProvider;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SourceFluidBuilder extends BaseFluidBuilder<Fluid, Fluid> {

    private final AnonymousElement.Builder baseBuilderInstance;
    private final AttributedSupplier<Fluid, Fluid> supplier;
    private ForgeFlowingFluid.Properties propsConfig;
    private final String providerName;

    public SourceFluidBuilder(final AnonymousElement.Builder baseBuilderInstance,
                              final String providerName) {
        this.baseBuilderInstance = baseBuilderInstance;
        this.providerName = providerName;
        this.supplier = new AttributedSupplier<>(ElementProvider.FLUID);
    }

    @Override
    public SourceFluidBuilder supplier(final Supplier<Fluid> supplier) {
        this.supplier.supplier(supplier);
        return this;
    }

    public SourceFluidBuilder properties(final ForgeFlowingFluid.Properties props) {
        this.propsConfig = props;
        return this;
    }

    @Override
    public SourceFluidBuilder tabGroup(final String tabGroup) {
        this.supplier.tabGroup(tabGroup);
        return this;
    }

    @Override
    public SourceFluidBuilder tagResource(final ResourceLocation tagResource) {
        this.supplier.tagResource(tagResource);
        return this;
    }

    @Override
    public SourceFluidBuilder tag(final ITag.INamedTag<Fluid> tag) {
        this.supplier.tag(tag);
        return this;
    }

    @Override
    public SourceFluidBuilder mirroredTagResource(final ResourceLocation mirroredTagResource) {
        this.supplier.mirroredTagResource(mirroredTagResource);
        return this;
    }

    @Override
    public SourceFluidBuilder mirroredTag(final ITag.INamedTag<Item> mirroredTag) {
        this.supplier.mirroredTag(mirroredTag);
        return this;
    }

    @Override
    public SourceFluidBuilder langMappings(final Map<LangKey, String> langMappings) {
        this.supplier.langMappings(langMappings);
        return this;
    }

    @Override
    public SourceFluidBuilder recipe(final Consumer<Consumer<IFinishedRecipe>> recipeConsumer) {
        this.supplier.recipe(recipeConsumer);
        return this;
    }

    @Override
    public AnonymousElement.Builder construct() {
        if (this.supplier.getSupplier() == null && this.propsConfig != null) {
            if (this.supplier.getLangMappings() == null) {
                this.supplier.supplier(() -> new LangMappedSourceFluid(propsConfig));
            } else {
                final LangMappedSourceFluid langMappedSourceFluid = super.createLangMappedFluid(
                        LangMappedSourceFluid.class,
                        this.providerName,
                        this.supplier.getLangMappings(),
                        this.propsConfig
                );
                this.supplier.supplier(() -> langMappedSourceFluid);
                this.supplier.langMappings(null);
            }
        }
        this.baseBuilderInstance.addSourceFluidSupplier(this.providerName, this.supplier);
        return this.baseBuilderInstance;
    }
}
