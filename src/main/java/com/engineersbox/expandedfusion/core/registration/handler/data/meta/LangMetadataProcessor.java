package com.engineersbox.expandedfusion.core.registration.handler.data.meta;

import com.engineersbox.expandedfusion.core.registration.annotation.meta.LocaleEntry;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.elements.ElementClassRetriever;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.elements.MetadataProvider;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.ElementProvider;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangFileResourceHandler;
import com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class LangMetadataProcessor {

    private final String modId;
    private final ElementClassRetriever elementClassRetriever;
    private final Map<LangKey, LangFileResourceHandler> resourceHandlers;

    @Inject
    public LangMetadataProcessor(@Named("modId") final String modId,
                                 final ElementClassRetriever elementClassRetriever) {
        resourceHandlers = new HashMap<>();
        this.elementClassRetriever = elementClassRetriever;
        this.modId = modId;
    }

    public void createMappingsForElement(final ElementProvider elemProvider) {
        final Set<? extends MetadataProvider<? extends Annotation>> pairs = this.elementClassRetriever.getProviders(elemProvider);
        pairs.forEach((final MetadataProvider<? extends Annotation> pair) -> {
            final String formattedProviderName = String.format(
                    "%s.%s.%s",
                    pair.getTypeName(),
                    this.modId,
                    pair.getProviderName()
            );
            addMappingForLocales(pair.getLocales(), formattedProviderName);
        });
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
