package com.engineersbox.expandedfusion.core.registration.annotation.processors.meta;

import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.elements.ElementClassRetriever;
import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.elements.MetadataProviderPair;
import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.lang.ElementProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.lang.LangFileResourceHandler;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class LangMetadataProcessor {

    /*
     * TODO: Implement a class to generate the en_us.json file
     * it will need to handle checking if one exists already
     * and amending it with write-back. Otherwise creating a
     * new one and filling it with relevant entries.
     *
     * When creating entries it should be of the form:
     * "<TYPE>.<MOD ID>.<PROVIDER NAME>": "<NAME MAPPING>"
     *
     * The mod ID can be retrieved with System.getProperty("lang.metadata.mod.ID")
     * where the Mod ID is provided as JVM args: -Dlang.metadata.mod.ID
     *
     * It could also be inferred from an @Mod annotation from the Mod.value() property
     */


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
        });
    }

    public void exportMappingsToFile() {
        this.langFileResourceHandler.exportMappingsToFile();
    }
}
