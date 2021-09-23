package com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.elements;

import com.engineersbox.expandedfusion.core.reflection.ProxyUtils;
import com.engineersbox.expandedfusion.core.registration.annotation.element.ProvidesElement;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LocaleEntry;
import com.engineersbox.expandedfusion.core.registration.exception.annotation.processors.meta.elements.InvalidMetadataDeclaration;
import org.reflections.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

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
