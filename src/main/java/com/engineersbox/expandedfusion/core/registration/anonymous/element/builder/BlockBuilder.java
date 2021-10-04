package com.engineersbox.expandedfusion.core.registration.anonymous.element.builder;

import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.AttributedSupplier;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.ElementAnnotationConstructor;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.ElementDynamicClassGenerator;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.block.LangMappedBlock;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.block.NoDefaultLootTableBlock;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.ElementProvider;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockBuilder extends AnonymousElementBuilder<Block, Block> {

    private final AnonymousElement.Builder baseBuilderInstance;
    private final AttributedSupplier<Block, Block> supplier;
    private Triple<AbstractBlock.Properties, Class<? extends LangMappedBlock>, Function<AbstractBlock.Properties, ? extends LangMappedBlock>> propsConfig;
    private final String providerName;

    public BlockBuilder(final AnonymousElement.Builder baseBuilderInstance,
                        final String providerName) {
        this.baseBuilderInstance = baseBuilderInstance;
        this.providerName = providerName;
        this.supplier = new AttributedSupplier<>(ElementProvider.BLOCK);
    }

    @Override
    public BlockBuilder supplier(final Supplier<Block> supplier) {
        this.supplier.supplier(supplier);
        return this;
    }

    public BlockBuilder properties(final AbstractBlock.Properties props,
                                   final boolean noDefaultLootTable) {
        this.propsConfig = Triple.<AbstractBlock.Properties, Class<? extends LangMappedBlock>, Function<AbstractBlock.Properties, ? extends LangMappedBlock>>of(
                props,
                (noDefaultLootTable ? NoDefaultLootTableBlock.class : LangMappedBlock.class),
                (noDefaultLootTable ? NoDefaultLootTableBlock::new : LangMappedBlock::new)
        );
        return this;
    }

    @Override
    public BlockBuilder tabGroup(final String tabGroup) {
        this.supplier.tabGroup(tabGroup);
        return this;
    }

    @Override
    public BlockBuilder tagResource(final ResourceLocation tagResource) {
        this.supplier.tagResource(tagResource);
        return this;
    }

    @Override
    public BlockBuilder tag(final ITag.INamedTag<Block> tag) {
        this.supplier.tag(tag);
        return this;
    }

    @Override
    public BlockBuilder mirroredTagResource(final ResourceLocation mirroredTagResource) {
        this.supplier.mirroredTagResource(mirroredTagResource);
        return this;
    }

    @Override
    public BlockBuilder mirroredTag(final ITag.INamedTag<Item> mirroredTag) {
        this.supplier.mirroredTag(mirroredTag);
        return this;
    }

    @Override
    public BlockBuilder langMappings(final Map<LangKey, String> langMappings) {
        this.supplier.langMappings(langMappings);
        return this;
    }

    @Override
    public BlockBuilder recipe(final Consumer<Consumer<IFinishedRecipe>> recipeConsumer) {
        this.supplier.recipe(recipeConsumer);
        return this;
    }

    @Override
    public AnonymousElement.Builder construct() {
        if (this.supplier.getSupplier() == null && this.propsConfig != null) {
            if (this.supplier.getLangMappings() == null) {
                this.supplier.supplier(() -> this.propsConfig.getRight().apply(this.propsConfig.getLeft()));
            } else {
                final LangMappedBlock langMappedBlock = createLangMappedBlock(
                        this.propsConfig.getMiddle(),
                        this.providerName,
                        this.propsConfig.getLeft()
                );
                this.supplier.supplier(() -> langMappedBlock);
                this.supplier.langMappings(null);
            }
        }
        this.baseBuilderInstance.addBlockSupplier(this.providerName, this.supplier);
        return this.baseBuilderInstance;
    }

    private <T extends LangMappedBlock> T createLangMappedBlock(final Class<T> baseClass,
                                                                final String providerName,
                                                                final AbstractBlock.Properties props) {
        super.checkPackageNameInitialised();
        return new ElementDynamicClassGenerator<>(baseClass)
                .retrieveDeclaredConstructor(AbstractBlock.Properties.class)
                .withAnnotation(this.supplier.getLangMetadata())
                .withAnnotation(ElementAnnotationConstructor.createBlockProvider(providerName))
                .createUnloadedClass(String.format(
                        AnonymousElementBuilder.DYNAMIC_CLASS_IDENTIFIER_FORMAT,
                        AnonymousElementBuilder.PACKAGE_NAME,
                        providerName,
                        AnonymousElementBuilder.DYNAMIC_BLOCK_CLASS_SUFFIX
                ))
                .loadClass()
                .getInstance(props);
    }
}
