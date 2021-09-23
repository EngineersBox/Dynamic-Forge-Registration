package com.engineersbox.expandedfusion.core.reflection;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PackageInspector {

    private static final Logger LOGGER = LogManager.getLogger(PackageInspector.class);

    private PackageInspector() {
        throw new IllegalStateException("Utility Class");
    }

    public static List<Class<?>> getCallerTopLevelClasses(final String ...toIgnore) {
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

    public static String getCallerPackageName(final String ...toIgnore) {

        final Package packageObj = getCallerPackage(toIgnore);
        return packageObj != null ? packageObj.getName() : null;
    }

    private static boolean matchesAsSubPackage(final String packageName,
                                               final List<String> toMatchAgainst) {
        return toMatchAgainst.stream().anyMatch(packageName::contains);
    }

    public static Package getCallerPackage(final String ...toIgnore) {
        final StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        int startIdx = 0;
        for (int i = 0; i < stElements.length; i++) {
            if (stElements[i].getClassName().equals(PackageInspector.class.getName())) {
                startIdx = i + 1;
            }
        }
        for (final StackTraceElement ste : ArrayUtils.subarray(stElements, startIdx, stElements.length)) {
            final boolean matches = matchesAsSubPackage(ste.getClassName(), Arrays.asList(toIgnore));
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

}
