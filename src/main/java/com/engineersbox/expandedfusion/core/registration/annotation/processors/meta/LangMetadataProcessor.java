package com.engineersbox.expandedfusion.core.registration.annotation.processors.meta;

import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.meta.LocaleEntry;
import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.elements.ElementClassRetriever;
import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.elements.MetadataProviderPair;
import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.lang.ElementProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.lang.LangFileResourceHandler;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidBucketProperties;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.lang.LangKey;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class LangMetadataProcessor {

    public static final String MOD_PACKAGE_SYSTEM_PROPERTY = "langmetadata.package_name";

    private static final Logger LOGGER = LogManager.getLogger(LangMetadataProcessor.class);
    private final String modId;
    private final String packageName;
    private final ElementClassRetriever elementClassRetriever;
    private final Map<LangKey, LangFileResourceHandler> resourceHandlers;
    private final Reflections reflections;

    @Inject
    public LangMetadataProcessor(@Named("packageName") final String packageName,
                                 @Named("packageReflections") final Reflections reflections,
                                 final ElementClassRetriever elementClassRetriever) {
        this.packageName = packageName;
        this.reflections = reflections;
        resourceHandlers = new HashMap<>();
        this.elementClassRetriever = elementClassRetriever;
        this.modId = this.getModId();
    }

    private String getModId() {
        final Set<Class<?>> modAnnotatedClasses = this.reflections.getTypesAnnotatedWith(Mod.class);
        if (modAnnotatedClasses.isEmpty()) {
            throw new RuntimeException(String.format(
                    "No classes annotated with @Mod found in package %s",
                    this.packageName
            )); // TODO: Implement an exception for this
        }
        final Class<?> modAnnotatedClass = modAnnotatedClasses.iterator().next();
        if (modAnnotatedClasses.size() > 1) {
            LOGGER.info("More than one @Mod annotated class found. Defaulting to first:");
        }
        final Mod modAnnotation = modAnnotatedClass.getAnnotation(Mod.class);
        if (modAnnotation == null) {
            throw new RuntimeException(String.format(
                    "Could not retrieve @Mod annotation from class %s",
                    modAnnotatedClass.getName()
            )); // TODO: Implement an exception for this
        }
        return modAnnotation.value();
    }

    public void createMappingsForElement(final ElementProvider elemProvider) {
        final Set<? extends MetadataProviderPair<? extends Annotation>> pairs = this.elementClassRetriever.getProviders(elemProvider.providerClass);
        pairs.forEach((final MetadataProviderPair<? extends Annotation> pair) -> {
            final String formattedProviderName = String.format(
                    "%s.%s.%s",
                    pair.getTypeName(),
                    this.modId,
                    pair.getProviderName()
            );
            addMappingForLocales(pair.getLocales(), formattedProviderName);
            if (FluidProvider.class.isAssignableFrom(pair.getAnnotation().getClass())) {
                createBucketLangEntry((FluidProvider) pair.getAnnotation());
            }
        });
    }

    public void createBucketLangEntry(final FluidProvider annotation) {
        final FluidBucketProperties[] bucketProperties = annotation.bucket();
        if (bucketProperties.length < 1) {
            LOGGER.debug(
                    "No bucket properties provided for {}, skipping bucket lang mapping",
                    annotation.name()
            );
            return;
        }
        final LangMetadata[] langMetadata = bucketProperties[0].lang();
        if (langMetadata.length < 1) {
            LOGGER.debug(
                    "No lang metadata provided for {}, skipping bucket lang mapping",
                    annotation.name()
            );
            return;
        }
        final String formattedProviderName = String.format(
                "item.%s.%s",
                this.modId,
                bucketProperties[0].name()
        );
        addMappingForLocales(langMetadata[0].locales(), formattedProviderName);
    }

    private void addMappingForLocales(final LocaleEntry[] locales,
                                      final String formattedProviderName) {
        Stream.of(locales).forEach((final LocaleEntry entry) -> {
            LangFileResourceHandler handler = this.resourceHandlers.get(entry.key());
            if (handler == null) {
                handler = new LangFileResourceHandler(entry.key());
            }
            handler.addLangEntryIfNotExists(
                    formattedProviderName,
                    entry.mapping()
            );
            this.resourceHandlers.put(entry.key(), handler);
        });
    }

    public void exportMappingsToFile() {
        this.resourceHandlers.values().forEach(LangFileResourceHandler::exportMappingsToFile);
    }
}
