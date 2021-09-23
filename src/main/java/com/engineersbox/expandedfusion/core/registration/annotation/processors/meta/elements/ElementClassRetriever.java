package com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.elements;

import com.engineersbox.expandedfusion.core.reflection.ProxyUtils;
import com.engineersbox.expandedfusion.core.registration.annotation.element.ProvidesElement;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.lang.ElementProvider;
import com.engineersbox.expandedfusion.core.registration.exception.annotation.processors.meta.elements.InvalidMetadataDeclaration;
import com.engineersbox.expandedfusion.core.registration.exception.annotation.processors.meta.lang.LangMetadataAnnotationRetrievalException;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ElementClassRetriever {

    private final Reflections reflections;

    @Inject
    public ElementClassRetriever(@Named("packageReflections") final Reflections reflections) {
        this.reflections = reflections;
    }

    public Set<MetadataProvider<? extends Annotation>> getProviders(final ElementProvider provider) {
        final Set<Class<?>> classes = this.reflections.getTypesAnnotatedWith(provider.providerClass);
        return classes.stream()
                .filter((final Class<?> clazz) -> isLangMetadataAnnotationPresent(clazz, provider))
                .map((final Class<?> clazz) -> new MetadataProvider<>(
                        getLangMetadata(clazz, provider),
                        getProviderName(clazz, provider),
                        getTypeName(provider)
                )).collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    private boolean isLangMetadataAnnotationPresent(final Class<?> clazz,
                                                    final ElementProvider provider) {
        if (provider.nestedLangField == null) {
            return clazz.isAnnotationPresent(LangMetadata.class);
        }
        final Set<Method> methods = ReflectionUtils.getMethods(provider.providerClass, ReflectionUtils.withName(provider.nestedLangField));
        return clazz.isAnnotationPresent(provider.providerClass) && !methods.isEmpty();
    }

    @SuppressWarnings("unchecked")
    private LangMetadata getLangMetadata(final Class<?> clazz,
                                         final ElementProvider provider) {
        if (provider.nestedLangField == null) {
            final LangMetadata langMetadata = clazz.getAnnotation(LangMetadata.class);
            if (langMetadata != null) {
                return langMetadata;
            }
            throw new LangMetadataAnnotationRetrievalException(String.format(
                    "No @LangMetadata annotation was present and no nested field specified to retrieve from in provider annotation on class %s",
                    clazz.getName()
            ));
        }
        final Annotation annotation = clazz.getAnnotation(provider.providerClass);
        final List<Method> methods = new ArrayList<>(ReflectionUtils.getMethods(provider.providerClass, ReflectionUtils.withName(provider.nestedLangField)));
        if (methods.size() != 1) {
            throw new LangMetadataAnnotationRetrievalException(String.format(
                    "No @LangMetadata annotation was present and multiple fields matched provided name %s in provider annotation on class %s",
                    provider.nestedLangField,
                    clazz.getName()
            ));
        }
        try {
            if (Proxy.isProxyClass(annotation.getClass())) {
                final LangMetadata[] langAnnotations = ProxyUtils.getProxiedAnnotationValue(annotation, provider.nestedLangField, LangMetadata[].class);
                if (langAnnotations.length > 0) {
                    return langAnnotations[0];
                }
            }
            final LangMetadata[] langAnnotations = (LangMetadata[]) methods.get(0).invoke(annotation);
            if (langAnnotations.length > 0) {
                return langAnnotations[0];
            }
            throw new LangMetadataAnnotationRetrievalException(String.format(
                    "Expected a @LangMetadata annotation for @%s on class %s but got none",
                    provider.providerClass.getName(),
                    clazz.getName()
            ));
        } catch (final InvocationTargetException | IllegalAccessException e) {
            throw new LangMetadataAnnotationRetrievalException(String.format(
                    "Could not retrieve @LangMetadata from field on provider annotation %s for class %s",
                    provider.providerClass.getName(),
                    clazz.getName()
            ), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Annotation> String getProviderName(final Class<?> clazz,
                                                          final ElementProvider provider) {
        final T providerAnnotation = (T) clazz.getAnnotation(provider.providerClass);
        if (providerAnnotation == null) {
            return null;
        }
        if (Proxy.isProxyClass(provider.providerClass)) {
            return ProxyUtils.getProxiedAnnotationValue(providerAnnotation, "name", String.class);
        }
        final List<Method> methods = new ArrayList<>(ReflectionUtils.getMethods(provider.providerClass, ReflectionUtils.withName("name")));
        if (methods.size() != 1) {
            throw new InvalidMetadataDeclaration(String.format(
                    "Invalid provider annotation %s, not annotated with @ProvidesElement: no provider name method name()",
                    providerAnnotation.getClass().getName()
            ));
        }
        try {
            return (String) methods.get(0).invoke(providerAnnotation);
        } catch (final InvocationTargetException | IllegalAccessException e) {
            throw new InvalidMetadataDeclaration(String.format(
                    "Invalid provider annotation %s, not annotated with @ProvidesElement: %s",
                    provider.providerClass.getName(),
                    e.getMessage()
            ));
        }
    }

    private  String getTypeName(final ElementProvider provider) {
        final ProvidesElement providerAnnotation = provider.providerClass.getAnnotation(ProvidesElement.class);
        if (providerAnnotation != null) {
            return providerAnnotation.value();
        }
        return null;
    }
}
