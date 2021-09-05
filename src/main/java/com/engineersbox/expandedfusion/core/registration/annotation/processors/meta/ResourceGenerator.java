package com.engineersbox.expandedfusion.core.registration.annotation.processors.meta;

import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.lang.ElementProvider;
import com.engineersbox.expandedfusion.core.registration.exception.annotation.processors.meta.lang.MissingRequiredVMArgument;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceGenerator {

    public static final String ELEMENTS_TO_GENERATE_PROPERTY = "langmetadata.elements_to_gen";

    public static void main(final String[] args) {
        final Injector injector = Guice.createInjector(
                new LangMetadataModule(getPackageName())
        );
        final LangMetadataProcessor langMetadataProcessor = injector.getInstance(LangMetadataProcessor.class);
        getElementsToGenerate().forEach(langMetadataProcessor::createMappingsForElement);
        langMetadataProcessor.exportMappingsToFile();
    }

    private static String getPackageName() {
        final String packageName = System.getProperty(LangMetadataProcessor.MOD_PACKAGE_SYSTEM_PROPERTY);
        if (packageName == null) {
            throw new MissingRequiredVMArgument(String.format(
                    "Mod root package is required and should be provided via the \"-D%s=<PACKAGE>\" argument",
                    LangMetadataProcessor.MOD_PACKAGE_SYSTEM_PROPERTY
            ));
        }
        return packageName;
    }

    private static List<ElementProvider> getElementsToGenerate() {
        final String literalElements = System.getProperty(ELEMENTS_TO_GENERATE_PROPERTY);
        if (literalElements == null) {
            throw new MissingRequiredVMArgument(String.format(
                    "Missing parameter for elements to generate, provide it with \"-D%s=<GEN_ELEM>[,<GEN_ELEM>]\" where GEN_ELEM is one of: %s",
                    ELEMENTS_TO_GENERATE_PROPERTY,
                    Stream.of(ElementProvider.values())
                            .map(ElementProvider::toString)
                            .map(String::toUpperCase)
                            .collect(Collectors.joining(","))
            ));
        }
        final String[] splitElements = literalElements.split(",");
        return Stream.of(splitElements)
                .map(ElementProvider::valueOf)
                .collect(Collectors.toList());
    }
}
