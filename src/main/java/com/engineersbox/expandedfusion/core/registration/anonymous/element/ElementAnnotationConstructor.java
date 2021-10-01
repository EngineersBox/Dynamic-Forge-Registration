package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProperties;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProviderImpl;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidProviderImpl;
import com.engineersbox.expandedfusion.core.registration.annotation.element.item.ItemProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.item.ItemProviderImpl;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LocaleEntry;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.block.BlockImplType;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElementAnnotationConstructor {

    private ElementAnnotationConstructor() {}

    public static LangMetadata createLangMetadata(final Map<LangKey, String> langMapping) {
        final LocaleEntry[] locales = createLocales(langMapping);
        return new LangMetadata(){
            @Override
            public Class<? extends Annotation> annotationType() {
                return LangMetadata.class;
            }

            @Override
            public LocaleEntry[] locales() {
                return locales;
            }
        };
    }

    private static LocaleEntry[] createLocales(final Map<LangKey, String> langMapping) {
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

    public static BlockProvider createBlockProvider(final String providerName) {
        return new BlockProviderImpl(
                providerName,
                BlockImplType.STATIC,
                false,
                new BlockProperties[0],
                ""
        );
    }

    public static ItemProvider createItemProvider(final String providerName) {
        return new ItemProviderImpl(providerName);
    }

    public static FluidProvider createFluidProvider(final String providerName) {
        return new FluidProviderImpl(
                providerName,
                false,
                new BlockProperties[0]
        );
    }
}
