package com.engineersbox.expandedfusion.core.registration.resolver;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;


public class PackageReflectionsModule extends AbstractModule {

    private Logger logger;
    private Reflections reflections;
    private String packageName;

    public PackageReflectionsModule() {
        this.packageName = PackageReflectionsModule.getCallerPackageName();
    }

    public static String getCallerPackageName() {
        final StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (final StackTraceElement ste : stElements) {
            if (!ste.getClassName().equals(PackageReflectionsModule.class.getName())
                    && ste.getClassName().indexOf("java.lang.Thread") != 0) {
                return ste.getClass().getPackage().toString();
            }
        }
        return null;
    }

    public PackageReflectionsModule withLogger(final Logger logger) {
        this.logger = logger;
        return this;
    }

    public PackageReflectionsModule withPackageName(final String packageName) {
        this.packageName = packageName;
        return this;
    }

    public PackageReflectionsModule build() {
        this.reflections = new Reflections(new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forPackage(this.packageName))
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
        if (this.logger != null) {
            bind(Logger.class).toInstance(this.logger);
        }
        bind(Reflections.class)
            .annotatedWith(Names.named("packageReflections"))
            .toInstance(this.reflections);
    }

}
