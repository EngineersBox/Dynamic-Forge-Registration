package com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.elements;

import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class ElementClassRetriever {

    private final Reflections reflections;

    @Inject
    public ElementClassRetriever(@Named("packageReflections") final Reflections reflections) {
        this.reflections = reflections;
    }

    public <T extends Annotation> Set<MetadataProviderPair<T>> getProviders(final Class<T> providerClass) {
        return this.reflections.getTypesAnnotatedWith(providerClass)
                .stream()
                .filter((final Class<?> clazz) -> clazz.isAnnotationPresent(LangMetadata.class))
                .map((final Class<?> clazz) -> new MetadataProviderPair<>(
                        clazz.getAnnotation(LangMetadata.class),
                        clazz.getAnnotation(providerClass)
                )).collect(Collectors.toSet());
    }
}
