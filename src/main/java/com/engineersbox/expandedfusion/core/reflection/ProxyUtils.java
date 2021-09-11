package com.engineersbox.expandedfusion.core.reflection;

import com.engineersbox.expandedfusion.core.reflection.exception.ProxiedAnnotationResolutionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

public class ProxyUtils {

    private ProxyUtils() {
        throw new IllegalStateException("Utility Class");
    }

    @SuppressWarnings({"unchecked", "java:S3011"})
    public static <T> T getProxiedAnnotationValue(final Annotation annotation, final String attributeName, final Class<T> typeImpl) {
        final InvocationHandler handler = Proxy.getInvocationHandler(annotation);
        final Field memberValuesField;
        try {
            memberValuesField = handler.getClass().getDeclaredField("memberValues");
        } catch (final NoSuchFieldException e) {
            throw new ProxiedAnnotationResolutionException("Proxy invocation handler was not of type AnnotationInvocationHandler, and has no field 'memberValues'", e);
        }
        memberValuesField.setAccessible(true);
        final Map<String, Object> memberValues;
        try {
            final Object uncastMemberValues = memberValuesField.get(handler);
            if (!(uncastMemberValues instanceof Map)) {
                throw new ProxiedAnnotationResolutionException("Handler instance provides no 'memberValues' field of type Map<String, Object>. Class is a potential subclass with dangerous overrides.");
            }
            memberValues = (Map<String, Object>) uncastMemberValues;
        } catch (final IllegalAccessException e) {
            throw new ProxiedAnnotationResolutionException(String.format(
                    "AnnotationInvocationHandler instance %s prevents access to 'memberValues' field",
                    handler.getClass().getName()
            ), e);
        }
        final Object annotationMemberAttribute = memberValues.get(attributeName);
        if (!typeImpl.isAssignableFrom(annotationMemberAttribute.getClass())) {
            throw new ProxiedAnnotationResolutionException(String.format(
                    "Annotation attribute value differed from expected type [Expected: %s] [Actual: %s]",
                    typeImpl.getName(),
                    annotationMemberAttribute.getClass().getName()
            ));
        }
        return (T) annotationMemberAttribute;
    }

}
