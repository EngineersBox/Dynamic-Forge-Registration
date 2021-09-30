package com.engineersbox.expandedfusion.core.registration.handler.data.meta.elements;

import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LocaleEntry;

import java.lang.annotation.Annotation;

public class MetadataProvider<T extends Annotation> {

    private final LangMetadata langMetadata;
    private final String providerName;
    private final String typeName;

    public MetadataProvider(final LangMetadata langMetadata,
                            final String providerName,
                            final String typeName) {
        this.langMetadata = langMetadata;
        this.providerName = providerName;
        this.typeName = typeName;
    }

    public String getProviderName() {
        return this.providerName;
    }

    public LocaleEntry[] getLocales() {
        return this.langMetadata.locales();
    }

    public String getTypeName() {
        return this.typeName;
    }
}
