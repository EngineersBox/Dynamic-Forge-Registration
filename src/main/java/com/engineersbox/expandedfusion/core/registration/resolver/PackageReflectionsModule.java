package com.engineersbox.expandedfusion.core.registration.resolver;

import com.engineersbox.expandedfusion.core.reflection.vfs.ModJarVfsUrlType;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.builder.AnonymousElementBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public final class PackageReflectionsModule extends AbstractModule {

    private Reflections reflections;
    private String packageName;

    public PackageReflectionsModule withPackageName(final String packageName) {
        this.packageName = packageName;
        return this;
    }

    public PackageReflectionsModule build() {
        this.reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(new ModJarVfsUrlType().filterOutFromURLs(ClasspathHelper.forPackage(this.packageName)))
                .setScanners(
                        new TypeElementsScanner(),
                        new SubTypesScanner(),
                        new TypeAnnotationsScanner()
                )
        );
        return this;
    }

    @Override
    protected void configure() {
        bind(Reflections.class)
                .annotatedWith(Names.named("packageReflections"))
                .toInstance(this.reflections);
        bind(String.class)
                .annotatedWith(Names.named("packageName"))
                .toInstance(this.packageName);
        requestStaticInjection(AnonymousElementBuilder.class);
    }

}
