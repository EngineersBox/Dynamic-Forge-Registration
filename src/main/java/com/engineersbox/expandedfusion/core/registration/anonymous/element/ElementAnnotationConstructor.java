package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import com.engineersbox.expandedfusion.core.reflection.AnnotationFieldUpdater;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProperties;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.item.ItemProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LocaleEntry;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplType;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElementAnnotationConstructor<T> {

    private final AnnotationFieldUpdater<T> annotationFieldUpdater;

    public ElementAnnotationConstructor(final T instance) {
        this.annotationFieldUpdater = new AnnotationFieldUpdater<>(instance);
    }

    public ElementAnnotationConstructor<T> withLangMapping(final Map<LangKey, String> langMapping) {
        final LocaleEntry[] locales = createLocales(langMapping);
        this.annotationFieldUpdater.withNewValue(LangMetadata.class, new LangMetadata(){
            @Override
            public Class<? extends Annotation> annotationType() {
                return LangMetadata.class;
            }

            @Override
            public LocaleEntry[] locales() {
                return locales;
            }
        });
        return this;
    }

    private LocaleEntry[] createLocales(final Map<LangKey, String> langMapping) {
        final LocaleEntry[] locales = new LocaleEntry[langMapping.size()];
        final List<Map.Entry<LangKey, String>> indexableLangEntries = new ArrayList<>(langMapping.entrySet());
        for (int i = 0; i < langMapping.size(); i++) {
            final Map.Entry<LangKey, String> entryAtIdx = indexableLangEntries.get(i);
            final LocaleEntry localeEntry = new LocaleEntry(){
                @Override
                public Class<? extends Annotation> annotationType() {
                    return LocaleEntry.class;
                }

                @Override
                public LangKey key() {
                    return entryAtIdx.getKey();
                }

                @Override
                public String mapping() {
                    return entryAtIdx.getValue();
                }
            };
            locales[i] = localeEntry;
        }
        return locales;
    }

    public ElementAnnotationConstructor<T> withBlockProvider(final String providerName) {
        final BlockProvider blockProvider = new BlockProvider(){
            @Override
            public Class<? extends Annotation> annotationType() { return BlockProvider.class; }
            @Override
            public String name() { return providerName; }
            @Override
            public BlockImplType type() { return BlockImplType.STATIC; }
            @Override
            public boolean noItem() { return false; }
            @Override
            public BlockProperties[] properties() { return new BlockProperties[0]; }
            @Override
            public String tabGroup() { return null; }
        };
        this.annotationFieldUpdater.withNewValue(BlockProvider.class, blockProvider);
        return this;
    }

    public ElementAnnotationConstructor<T> withItemProvider(final String providerName) {
        final ItemProvider itemProvider = new ItemProvider(){
            @Override
            public Class<? extends Annotation> annotationType() { return ItemProvider.class; }
            @Override
            public String name() { return providerName; }
        };
        this.annotationFieldUpdater.withNewValue(ItemProvider.class, itemProvider);
        return this;
    }

    public ElementAnnotationConstructor<T> withFluidProvider(final String providerName) {
        final FluidProvider fluidProvider = new FluidProvider(){
            @Override
            public Class<? extends Annotation> annotationType() { return ItemProvider.class; }
            @Override
            public String name() { return providerName; }
            @Override
            public boolean gaseous() { return false; }
            @Override
            public BlockProperties[] blockProperties() { return new BlockProperties[0]; }
        };
        this.annotationFieldUpdater.withNewValue(FluidProvider.class, fluidProvider);
        return this;
    }

    public void applyAnnotations() {
        this.annotationFieldUpdater.performUpdates();
    }
}
