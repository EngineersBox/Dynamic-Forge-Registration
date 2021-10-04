package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.builder.AnonymousBuilder;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.ElementProvider;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AttributedSupplier<T, E> implements AnonymousBuilder<T, E> {

    private Supplier<T> elementSupplier;
    private String elementTabGroup;
    private final TagBinding<E> tagBinding;
    private Map<LangKey, String> langMapping;
    private final ElementProvider elementProvider;
    private Consumer<Consumer<IFinishedRecipe>> recipeConsumer;

    public AttributedSupplier(final ElementProvider elementProvider) {
        this.tagBinding = new TagBinding<>();
        this.elementProvider = elementProvider;
    }

    @Override
    public AttributedSupplier<T,E> supplier(final Supplier<T> supplier) {
        this.elementSupplier = supplier;
        return this;
    }

    @Override
    public AttributedSupplier<T,E> tabGroup(final String tabGroup) {
        this.elementTabGroup = tabGroup;
        return this;
    }

    @Override
    public AttributedSupplier<T,E> tagResource(final ResourceLocation tagResource) {
        this.tagBinding.setTagResource(tagResource);
        return this;
    }

    @Override
    public AttributedSupplier<T,E> tag(final ITag.INamedTag<E> tag) {
        this.tagBinding.setTag(tag);
        return this;
    }

    @Override
    public AttributedSupplier<T,E> mirroredTagResource(final ResourceLocation mirroredTagResource) {
        this.tagBinding.setMirroredTagResource(mirroredTagResource);
        return this;
    }

    @Override
    public AttributedSupplier<T,E> mirroredTag(final ITag.INamedTag<Item> mirroredTag) {
        this.tagBinding.setMirroredTag(mirroredTag);
        return this;
    }

    @Override
    public AttributedSupplier<T,E> langMappings(final Map<LangKey, String> langMappings) {
        this.langMapping = langMappings;
        return this;
    }

    @Override
    public AttributedSupplier<T,E> recipe(final Consumer<Consumer<IFinishedRecipe>> recipeConsumer) {
        this.recipeConsumer = recipeConsumer;
        return this;
    }

    public Supplier<T> getSupplier() {
        return elementSupplier;
    }

    public String getTabGroup() {
        return elementTabGroup;
    }

    public TagBinding<E> getTagBinding() {
        return tagBinding;
    }

    public LangMetadata getLangMetadata() {
        return this.langMapping == null ? null : ElementAnnotationConstructor.createLangMetadata(this.langMapping);
    }

    public Map<LangKey, String> getLangMappings() {
        return this.langMapping;
    }

    public ElementProvider getElementProvider() {
        return this.elementProvider;
    }

    public Consumer<Consumer<IFinishedRecipe>> getRecipeConsumer() {
        return this.recipeConsumer;
    }
}
