package com.engineersbox.expandedfusion.core.registration.annotation.processors.meta;

import com.engineersbox.expandedfusion.core.registration.annotation.meta.LangMetadata;
import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.elements.ElementClassRetriever;
import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.elements.MetadataProviderPair;
import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.lang.ElementProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.lang.LangFileResourceHandler;
import com.engineersbox.expandedfusion.core.registration.annotation.provider.fluid.FluidBucketProperties;
import com.engineersbox.expandedfusion.core.registration.annotation.provider.fluid.FluidProvider;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class LangMetadataProcessor {

    public static final String MOD_PACKAGE_SYSTEM_PROPERTY = "langmetadata.package_name";

    private static final Logger LOGGER = LogManager.getLogger(LangMetadataProcessor.class);
    private final String modId;
    private final String packageName;
    private final ElementClassRetriever elementClassRetriever;
    private final LangFileResourceHandler langFileResourceHandler;
    private final Reflections reflections;

    @Inject
    public LangMetadataProcessor(@Named("packageName") final String packageName,
                                 @Named("packageReflections") final Reflections reflections,
                                 final ElementClassRetriever elementClassRetriever,
                                 final LangFileResourceHandler langFileResourceHandler) {
        this.packageName = packageName;
        this.reflections = reflections;
        this.elementClassRetriever = elementClassRetriever;
        this.langFileResourceHandler = langFileResourceHandler;
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
            ));
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
            this.langFileResourceHandler.addLangEntryIfNotExists(
                    formattedProviderName,
                    pair.getNameMapping()
            );
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
        this.langFileResourceHandler.addLangEntryIfNotExists(
                formattedProviderName,
                langMetadata[0].nameMapping()
        );
    }

    public void exportMappingsToFile() {
        this.langFileResourceHandler.exportMappingsToFile();
    }
}
