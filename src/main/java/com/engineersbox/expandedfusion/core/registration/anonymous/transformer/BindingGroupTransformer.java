package com.engineersbox.expandedfusion.core.registration.anonymous.transformer;

import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.ElementRetrieverInvoker;
import com.engineersbox.expandedfusion.core.registration.exception.anonymous.AnonymousElementRetrievalException;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BindingGroupTransformer {

    private final Class<?> registrant;

    public BindingGroupTransformer(final Class<?> registrant) {
        this.registrant = registrant;
    }

    @SuppressWarnings("unchecked")
    public List<AnonymousElement> getEnumRegistrantElements() {
        final Class<Enum<?>> enumRegistrant = (Class<Enum<?>>) this.registrant;
        final ElementRetrieverInvoker retriever = new ElementRetrieverInvoker(enumRegistrant);
        return Stream.of(enumRegistrant.getEnumConstants())
                .map(retriever::invoke)
                .collect(Collectors.toList());
    }

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
