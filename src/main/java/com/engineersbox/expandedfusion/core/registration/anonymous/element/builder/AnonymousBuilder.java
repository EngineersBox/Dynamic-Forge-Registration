package com.engineersbox.expandedfusion.core.registration.anonymous.element.builder;

import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.engineersbox.expandedfusion.core.registration.exception.anonymous.UninitialisedRuntimeClassPackageNameException;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AnonymousBuilder<T, E> {

    public static final String UNINITIALISED_PACKAGE_NAME = "UNINITIALISED";

    @Inject
    @Named("packageName")
    public static String PACKAGE_NAME = UNINITIALISED_PACKAGE_NAME;
    public static final String DYNAMIC_BLOCK_CLASS_SUFFIX = "_DYN_BLOCK";
    public static final String DYNAMIC_ITEM_CLASS_SUFFIX = "_DYN_ITEM";
    public static final String DYNAMIC_SOURCE_FLUID_CLASS_SUFFIX = "_DYN_SOURCE_FLUID";
    public static final String DYNAMIC_FLOWING_FLUID_CLASS_SUFFIX = "_DYN_FLOWING_FLUID";
    public static final String DYNAMIC_CLASS_IDENTIFIER_FORMAT = "%s.%s%s";

    public abstract AnonymousBuilder<T, E> supplier(final Supplier<T> supplier);

    public abstract AnonymousBuilder<T, E> tabGroup(final String tabGroup);

    public abstract AnonymousBuilder<T, E> tagResource(final ResourceLocation tagResource);

    public abstract AnonymousBuilder<T, E> tag(final ITag.INamedTag<E> tag);

    public abstract AnonymousBuilder<T, E> mirroredTagResource(final ResourceLocation mirroredTagResource);

    public abstract AnonymousBuilder<T, E> mirroredTag(final ITag.INamedTag<Item> mirroredTag);

    public abstract AnonymousBuilder<T, E> langMappings(final Map<LangKey, String> langMappings);

    public abstract AnonymousBuilder<T, E> recipe(final Consumer<Consumer<IFinishedRecipe>> recipeConsumer);

    public void checkPackageNameInitialised() {
        if (PACKAGE_NAME.equals(UNINITIALISED_PACKAGE_NAME)) {
            throw new UninitialisedRuntimeClassPackageNameException("Package name for class generation was not instantiated via static injection. Did the context change?");
        }
    }

    public abstract AnonymousElement.Builder construct();

}
