package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import com.engineersbox.expandedfusion.core.registration.annotation.anonymous.ElementRetriever;
import com.engineersbox.expandedfusion.core.registration.exception.anonymous.AnonymousElementRetrievalException;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

public class ElementRetrieverInvoker<T extends Enum<T>> {

    private final Class<T> clazz;
    private final Optional<Field> field;
    private final Optional<Method> method;

    public ElementRetrieverInvoker(final Class<T> clazz) {
        this.clazz = clazz;
        this.field = resolveField(clazz);
        this.method = resolveMethod(clazz);
    }

    @SuppressWarnings("unchecked")
    private Optional<Field> resolveField(final Class<T> clazz) {
        final Set<Field> retrieverField = ReflectionUtils.getFields(
                clazz,
                ReflectionUtils.withAnnotation(ElementRetriever.class),
                ReflectionUtils.withTypeAssignableTo(AnonymousElement.class)
        );
        return retrieverField.stream().findFirst();
    }

    @SuppressWarnings("unchecked")
    private Optional<Method> resolveMethod(final Class<T> clazz) {
        final Set<Method> retrieverField = ReflectionUtils.getMethods(
                clazz,
                ReflectionUtils.withAnnotation(ElementRetriever.class),
                ReflectionUtils.withReturnTypeAssignableTo(AnonymousElement.class)
        );
        return retrieverField.stream().findFirst();
    }

    public AnonymousElement invoke(final Object obj) {
        try {
            if (this.field.isPresent()) {
                return (AnonymousElement) this.field.get().get(obj);
            } else if (this.method.isPresent()) {
                return (AnonymousElement) this.method.get().invoke(obj);
            }
            throw new AnonymousElementRetrievalException("No valid field or method could be resolved for: " + this.clazz.getName());
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new AnonymousElementRetrievalException(e);
        }
    }
}
