package com.engineersbox.expandedfusion.core.registration.anonymous.element.builder;

import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.AttributedSupplier;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.ElementAnnotationConstructor;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.ElementDynamicClassGenerator;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.annotated.item.LangMappedItem;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.ElementProvider;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemBuilder extends AnonymousElementBuilder<Item, Item> {

    private final AnonymousElement.Builder baseBuilderInstance;
    private final AttributedSupplier<Item, Item> supplier;
    private Item.Properties propsConfig;
    private final String providerName;

    public ItemBuilder(final AnonymousElement.Builder baseBuilderInstance,
                       final String providerName) {
        this.baseBuilderInstance = baseBuilderInstance;
        this.providerName = providerName;
        this.supplier = new AttributedSupplier<>(ElementProvider.ITEM);
    }

    @Override
    public ItemBuilder supplier(final Supplier<Item> supplier) {
        this.supplier.supplier(supplier);
        return this;
    }

    public ItemBuilder properties(final Item.Properties props) {
        this.propsConfig = props;
        return this;
    }

    @Override
    public ItemBuilder tabGroup(final String tabGroup) {
        this.supplier.tabGroup(tabGroup);
        return this;
    }

    @Override
    public ItemBuilder tagResource(final ResourceLocation tagResource) {
        this.supplier.tagResource(tagResource);
        return this;
    }

    @Override
    public ItemBuilder tag(final ITag.INamedTag<Item> tag) {
        this.supplier.tag(tag);
        return this;
    }

    @Override
    public ItemBuilder mirroredTagResource(final ResourceLocation mirroredTagResource) {
        this.supplier.mirroredTagResource(mirroredTagResource);
        return this;
    }

    @Override
    public ItemBuilder mirroredTag(final ITag.INamedTag<Item> mirroredTag) {
        this.supplier.mirroredTag(mirroredTag);
        return this;
    }

    @Override
    public ItemBuilder langMappings(final Map<LangKey, String> langMappings) {
        this.supplier.langMappings(langMappings);
        return this;
    }

    @Override
    public ItemBuilder recipe(final Consumer<Consumer<IFinishedRecipe>> recipeConsumer) {
        this.supplier.recipe(recipeConsumer);
        return this;
    }

    @Override
    public AnonymousElement.Builder construct() {
        if (this.supplier.getSupplier() == null && this.propsConfig != null) {
            if (this.supplier.getLangMappings() == null) {
                this.supplier.supplier(() -> new LangMappedItem(propsConfig));
            } else {
                final LangMappedItem langMappedItem = createLangMappedItem(
                        this.providerName,
                        this.propsConfig
                );
                this.supplier.supplier(() -> langMappedItem);
                this.supplier.langMappings(null);
            }
        }
        this.baseBuilderInstance.addItemSupplier(this.providerName, this.supplier);
        return this.baseBuilderInstance;
    }

    private LangMappedItem createLangMappedItem(final String providerName,
                                                final Item.Properties props) {
        checkPackageNameInitialised();
        return new ElementDynamicClassGenerator<>(LangMappedItem.class)
                .retrieveDeclaredConstructor(Item.Properties.class)
                .withAnnotation(this.supplier.getLangMetadata())
                .withAnnotation(ElementAnnotationConstructor.createItemProvider(providerName))
                .createUnloadedClass(String.format(
                        DYNAMIC_CLASS_IDENTIFIER_FORMAT,
                        PACKAGE_NAME,
                        providerName,
                        DYNAMIC_ITEM_CLASS_SUFFIX
                ))
                .loadClass()
                .getInstance(props);
    }
}
