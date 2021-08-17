package com.engineersbox.expandedfusion.core.registration.annotation.processors.meta;

import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.elements.ElementClassRetriever;
import com.engineersbox.expandedfusion.core.registration.annotation.processors.meta.lang.LangFileResourceHandler;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class LangMetadataModule extends AbstractModule {

    private final String packageName;

    public LangMetadataModule(final String packageName) {
        this.packageName = packageName;
    }

    @Override
    protected void configure() {
        final Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(
                        new TypeElementsScanner(),
                        new SubTypesScanner(),
                        new TypeAnnotationsScanner()
                )
        );

        bind(ElementClassRetriever.class);
        bind(Reflections.class)
                .annotatedWith(Names.named("packageReflections"))
                .toInstance(reflections);
        bind(String.class)
                .annotatedWith(Names.named("packageName"))
                .toInstance(packageName);
    }
}
