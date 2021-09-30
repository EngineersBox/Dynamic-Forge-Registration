package com.engineersbox.expandedfusion.core.registration.anonymous.transformer;

import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.ElementRetrieverInvoker;
import com.engineersbox.expandedfusion.core.registration.exception.anonymous.AnonymousElementRetrievalException;
import com.engineersbox.expandedfusion.elements.block.Metals;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BindingGroupTransformer {

    private final Class<?> registrant;

    public BindingGroupTransformer(final Class<?> registrant) {
        this.registrant = registrant;
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> List<AnonymousElement> getEnumRegistrantElements() {
        final Class<T> enumRegistrant = (Class<T>) this.registrant;
        final ElementRetrieverInvoker<T> retriever = new ElementRetrieverInvoker<>(enumRegistrant);
        final T[] enumConstants = enumRegistrant.getEnumConstants();
        if (enumConstants == null) {
            throw new AnonymousElementRetrievalException(String.format(
                    "No enum constants present in class %s",
                    this.registrant.getName()

            ));
        }
        return Stream.of(enumConstants)
                .map(retriever::invoke)
                .collect(Collectors.toList());
    }

    private <T extends Enum<T>> Set<T> getEnumConstants(final Class<T> enumRegistrant) {
        return Arrays.stream(enumRegistrant.getDeclaredFields())
                .filter(Field::isEnumConstant)
                .map((final Field field) -> {
                    try {
                        return field.get(null);
                    } catch (IllegalAccessException e) {
                        return Stream.empty();
                    }
                })
                .filter(Objects::nonNull)
                .map((final Object enumEntry) -> (T) enumEntry)
                .collect(Collectors.toSet());

    }

    @SuppressWarnings("unchecked")
    public List<AnonymousElement> getStaticFieldRegistrantElements() {
        final Set<Field> elementFields = ReflectionUtils.getFields(
                this.registrant,
                ReflectionUtils.withTypeAssignableTo(AnonymousElement.class),
                ReflectionUtils.withModifier(Modifier.PUBLIC),
                ReflectionUtils.withModifier(Modifier.STATIC)
        );
        try {
            final List<AnonymousElement> elements = new ArrayList<>();
            for (final Field field : elementFields) {
                elements.add((AnonymousElement) field.get(null));
            }
            return elements;
        } catch (final IllegalAccessException e) {
            throw new AnonymousElementRetrievalException(e);
        }
    }

}
