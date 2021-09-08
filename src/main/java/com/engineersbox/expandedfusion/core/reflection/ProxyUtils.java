package com.engineersbox.expandedfusion.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

public class ProxyUtils {

    private ProxyUtils() {
        throw new IllegalStateException("Utility Class");
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProxiedAnnotationValue(final Annotation annotation, final String attributeName, final Class<T> typeImpl) {
        final InvocationHandler handler = Proxy.getInvocationHandler(annotation);
        final Field memberValuesField;
        try {
            memberValuesField = handler.getClass().getDeclaredField("memberValues");
        } catch (final NoSuchFieldException e) {
            throw new RuntimeException("Proxy invocation hander was not of type AnnotationInvocationHandler, and has no field 'memberValues'", e); // TODO: Implement an exception for this
        }
        memberValuesField.setAccessible(true);
        final Map<String, Object> memberValues;
        try {
            final Object uncastMemberValues = memberValuesField.get(handler);
            if (!(uncastMemberValues instanceof Map)) {
                throw new RuntimeException("Handler instance provides no 'memberValues' field of type Map<String, Object>. Class is a potential subclass with dangerous overrides."); // TODO: Implement an exception for this
            }
            // TODO: Check instances of K, V generic variables on 'uncastMemberValues' instance
            memberValues = (Map<String, Object>) uncastMemberValues;
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(String.format(
                    "AnnotationInvocationHandler instance %s prevents access to 'memberValues' field",
                    handler.getClass().getName()
            ), e); // TODO: Implement an exception for this
        }
        final Object annotationMemberAttribute = memberValues.get(attributeName);
        if (!typeImpl.isAssignableFrom(annotationMemberAttribute.getClass())) {
            throw new RuntimeException(String.format(
                    "Annotation attribute value differed from expected type [Expected: %s] [Actual: %s]",
                    typeImpl.getName(),
                    annotationMemberAttribute.getClass().getName()
            ));
        }
        return (T) annotationMemberAttribute;
    }

}
