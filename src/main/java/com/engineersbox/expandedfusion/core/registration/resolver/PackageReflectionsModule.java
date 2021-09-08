package com.engineersbox.expandedfusion.core.registration.resolver;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.apache.commons.lang3.ArrayUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.engineersbox.expandedfusion.ExpandedFusion.LOGGER;

public class PackageReflectionsModule extends AbstractModule {

    private Reflections reflections;
    private String packageName;

    public static List<Class<?>> getCallerTopLevelClasses(final List<String> toIgnore) {
        final String callerPackage = getCallerPackageName(toIgnore);
        if (callerPackage == null) {
            return Collections.emptyList();
        }
        final List<Class<?>> classes;
        try (final ScanResult scanResult = new ClassGraph()
                .acceptPackages(enumerateSearchableNestedPackages(callerPackage))
                .enableClassInfo()
                .scan()) {
            classes = new ArrayList<>(scanResult.getAllClasses().loadClasses());
        } catch (final Exception e) {
            LOGGER.warn("Could not find classes with provided packages", e);
            return Collections.emptyList();
        }
        return classes.stream()
                .sorted((final Class<?> a, final Class<?> b) -> {
                    final int aSeparationCount = a.getPackage().getName().split("\\.").length;
                    final int bSeparationCount = b.getPackage().getName().split("\\.").length;
                    return Integer.compare(aSeparationCount, bSeparationCount);
                }).collect(Collectors.toList());
    }

    public static String[] enumerateSearchableNestedPackages(final String packageName) {
        final String[] separatedPackage = packageName.split("\\.");
        if (separatedPackage.length < 3) {
            return new String[]{packageName};
        }
        return IntStream.range(1, separatedPackage.length)
                .mapToObj((final int idx) -> String.join(
                        ".",
                        ArrayUtils.subarray(separatedPackage, 0, idx + 1))
                ).toArray(String[]::new);
    }

    public static String getCallerPackageName(final List<String> toIgnore) {

        final Package packageObj = getCallerPackage(toIgnore);
        return packageObj != null ? packageObj.getName() : null;
    }

    private static boolean matchesAsSubPackage(final String packageName,
                                               final List<String> toMatchAgainst) {
        return toMatchAgainst.stream().anyMatch(packageName::contains);
    }

    public static Package getCallerPackage(final List<String> toIgnore) {
        final StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        int startIdx = 0;
        for (int i = 0; i < stElements.length; i++) {
            if (stElements[i].getClassName().equals(PackageReflectionsModule.class.getName())) {
                startIdx = i + 1;
            }
        }
        for (final StackTraceElement ste : ArrayUtils.subarray(stElements, startIdx, stElements.length)) {
            final boolean matches = matchesAsSubPackage(ste.getClassName(), toIgnore);
            if (!matches) {
                try {
                    return Class.forName(ste.getClassName()).getPackage();
                } catch (final ClassNotFoundException e) {
                    LOGGER.warn("Could not resolve class with fully qualified class name [{}], defaulting to null to avoid incorrect parent matching", ste.getClassName(), e);
                    return null;
                }
            }
        }
        return null;
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
                new SubTypesScanner(false),
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
    }

}
