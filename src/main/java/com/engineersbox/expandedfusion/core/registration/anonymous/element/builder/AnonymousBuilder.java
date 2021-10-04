package com.engineersbox.expandedfusion.core.registration.anonymous.element.builder;

import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface AnonymousBuilder<T, E> {

    AnonymousBuilder<T, E> supplier(final Supplier<T> supplier);

    AnonymousBuilder<T, E> tabGroup(final String tabGroup);

    AnonymousBuilder<T, E> tagResource(final ResourceLocation tagResource);

    AnonymousBuilder<T, E> tag(final ITag.INamedTag<E> tag);

    AnonymousBuilder<T, E> mirroredTagResource(final ResourceLocation mirroredTagResource);

    AnonymousBuilder<T, E> mirroredTag(final ITag.INamedTag<Item> mirroredTag);

    AnonymousBuilder<T, E> langMappings(final Map<LangKey, String> langMappings);

    AnonymousBuilder<T, E> recipe(final Consumer<Consumer<IFinishedRecipe>> recipeConsumer);

}
