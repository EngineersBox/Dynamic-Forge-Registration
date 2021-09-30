package com.engineersbox.expandedfusion.core.registration.handler.data.meta;

import com.engineersbox.expandedfusion.core.event.EventSubscriptionHandler;
import com.engineersbox.expandedfusion.core.event.annotation.InternalEventHandler;
import com.engineersbox.expandedfusion.core.event.annotation.Subscriber;
import com.engineersbox.expandedfusion.core.event.annotation.modloadingcontext.DataEventHandler;
import com.engineersbox.expandedfusion.core.registration.exception.annotation.processors.meta.lang.MissingRequiredVMArgument;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.ElementProvider;
import com.google.inject.Inject;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
@InternalEventHandler
@DataEventHandler
public class LangMappingClientEventHandler implements EventSubscriptionHandler {

    private static final String ELEMENTS_TO_GENERATE_PROPERTY = "langmetadata.elements_to_gen";
    private final LangMetadataProcessor langMetadataProcessor;

    @Inject
    public LangMappingClientEventHandler(final LangMetadataProcessor langMetadataProcessor) {
        this.langMetadataProcessor = langMetadataProcessor;
    }

    @Subscriber
    public void generateLangMappings(final GatherDataEvent gatherEvent) {
        getElementsToGenerate().forEach(this.langMetadataProcessor::createMappingsForElement);
        this.langMetadataProcessor.exportMappingsToFile();
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
