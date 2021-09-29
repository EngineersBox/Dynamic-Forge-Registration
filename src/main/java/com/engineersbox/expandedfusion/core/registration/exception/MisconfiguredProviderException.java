package com.engineersbox.expandedfusion.core.registration.exception;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MisconfiguredProviderException extends RuntimeException {

    public MisconfiguredProviderException(final Throwable e) {
        super(e);
    }

    public MisconfiguredProviderException(final Map<String, List<Class<? extends Annotation>>> requires) {
        super(formatError(requires));
    }

    private static String formatError(final Map<String, List<Class<? extends Annotation>>> requires) {
        final StringBuilder sb = new StringBuilder("Invalid providers:\n\n");
        final AtomicInteger index = new AtomicInteger(0);
        requires.forEach((String name, List<Class<? extends Annotation>> missing) -> sb.append(String.format(
            "[%d] Missing provider annotated resources for [%s]%nMissing providers: [%s]%n%n",
            index.incrementAndGet(),
            name,
            missing.stream().map(Class::getName).collect(Collectors.joining(", "))
        )));
        return sb.toString().trim();
    }
}
