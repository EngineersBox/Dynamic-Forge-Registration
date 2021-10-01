package com.engineersbox.expandedfusion.core.registration.handler.data.meta.elements;

import com.engineersbox.expandedfusion.core.reflection.ProxyUtils;
import com.engineersbox.expandedfusion.core.registration.annotation.element.ProvidesElement;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.LangMetadataProcessor;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.ElementProvider;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.AttributedSupplier;
import com.engineersbox.expandedfusion.core.registration.exception.annotation.processors.meta.elements.InvalidMetadataDeclaration;
import com.engineersbox.expandedfusion.core.registration.exception.annotation.processors.meta.lang.LangMetadataAnnotationRetrievalException;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.anonymous.AnonymousElementImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.anonymous.AnonymousElementImplGrouping;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ElementClassRetriever {

    private static final Logger LOGGER = LogManager.getLogger(ElementClassRetriever.class);
    private final Reflections reflections;
    //    private final Map<ElementProvider, ImplClassGroupings<?>> classGroupings;
    private final AnonymousElementImplClassGrouping anonymousImplClassGroupings;

    @Inject
    public ElementClassRetriever(@Named("packageReflections") final Reflections reflections,
                                 final ImplClassGroupings<AnonymousElementImplGrouping> anonymousImplClassGroupings) {
        this.reflections = reflections;
        this.anonymousImplClassGroupings = (AnonymousElementImplClassGrouping) anonymousImplClassGroupings;
    }

    public Set<MetadataProvider<? extends Annotation>> getProviders(final ElementProvider provider) {
        if (provider == ElementProvider.ANONYMOUS) {
            return getAnonymousProviders();
        }
        final Set<Class<?>> classes = this.reflections.getTypesAnnotatedWith(provider.providerClass);
        // TODO: Refactor to use injected instances of ImplClassGroupings<?> for each of the ElementProvider enumerations
        return classes.stream()
                .filter((final Class<?> clazz) -> isLangMetadataAnnotationPresent(clazz, provider))
                .map((final Class<?> clazz) -> new MetadataProvider<>(
                        getLangMetadata(clazz, provider),
                        getProviderName(clazz, provider),
                        getTypeName(provider)
                )).collect(Collectors.toSet());
    }

    private Set<MetadataProvider<? extends Annotation>> getAnonymousProviders() {
        this.anonymousImplClassGroupings.collectAnnotatedResources();
        return this.anonymousImplClassGroupings.getClassGroupings()
                .values()
                .stream()
                .map(AnonymousElementImplGrouping::getRegistrantElements)
                .flatMap(List::stream)
                .flatMap((final AnonymousElement element) -> Stream.of(
                        constructMetadataProviders(element.blockSuppliers),
                        constructMetadataProviders(element.itemSuppliers),
                        constructMetadataProviders(element.sourceFluidSuppliers),
                        constructMetadataProviders(element.flowingFluidSuppliers)
                ).flatMap(Set::stream))
                .collect(Collectors.toSet());
    }

    private <T, E> Set<MetadataProvider<? extends Annotation>> constructMetadataProviders(final Map<String, AttributedSupplier<T, E>> suppliers) {
        return suppliers.entrySet()
                .stream()
                .flatMap((final Map.Entry<String, AttributedSupplier<T, E>> entry) -> {
                    final Class<?> supplierElementClass = entry.getValue().getSupplier().get().getClass();
                    final Optional<ElementProvider> anonymousAttachedProvider = ElementProvider.fromSupplierClass(supplierElementClass);
                    final LangMetadata langMetadata = entry.getValue().getLangMetadata();
                    if (!anonymousAttachedProvider.isPresent() && langMetadata == null) {
                        LOGGER.warn(
                                "No provider annotation present on supplier class {} for provider {}, skipping",
                                supplierElementClass.getName(),
                                entry.getKey()
                        );
                        return Stream.empty();
                    }
                    ElementProvider elementProviderType = entry.getValue().getElementProvider();
                    if (anonymousAttachedProvider.isPresent() && (langMetadata == null || elementProviderType == null)) {
                        elementProviderType = anonymousAttachedProvider.get();
                    }
                    if (elementProviderType == null) {
                        LOGGER.warn(
                                "No element provider type matches supplier class {} for provider {}, skipping",
                                supplierElementClass.getName(),
                                entry.getKey()
                        );
                        return Stream.empty();
                    }
                    return Stream.of(new MetadataProvider<>(
                            langMetadata != null ? langMetadata : getLangMetadata(supplierElementClass, ElementProvider.ANONYMOUS),
                            entry.getKey(),
                            getTypeName(elementProviderType)
                    ));
                })
                .collect(Collectors.toSet());
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
