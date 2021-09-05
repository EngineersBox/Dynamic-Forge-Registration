package com.engineersbox.expandedfusion.core.registration.provider.grouping.anonymous;

import com.engineersbox.expandedfusion.core.registration.annotation.anonymous.ElementRetriever;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.ElementRetrieverInvoker;
import com.engineersbox.expandedfusion.core.registration.exception.anonymous.AnonymousElementRetrievalException;
import com.engineersbox.expandedfusion.core.registration.exception.grouping.anonymous.DuplicateAnonymousElementProviderBinding;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplGrouping;
import com.google.common.collect.ImmutableList;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnonymousElementImplGrouping implements ImplGrouping {

    private Class<?> registrant;

    public void setRegistrant(final Class<?> registrant) {
        if (this.registrant != null) {
            throw new DuplicateAnonymousElementProviderBinding(this.registrant, registrant);
        }
        this.registrant = registrant;
    }

    public List<AnonymousElement> getRegistrantElements() {
        if (this.registrant == null) {
            throw new RuntimeException("Registrant was not present, class is either synthetic or altered at runtime"); // TODO: Implement an exception for this
        }
        if (Enum.class.isAssignableFrom(this.registrant)) {

        }
        return Enum.class.isAssignableFrom(this.registrant) ? getEnumRegistrantElements() : getStaticFieldRegistrantElements();
    }

    @SuppressWarnings("unchecked")
    private List<AnonymousElement> getEnumRegistrantElements() {
        final Class<Enum<?>> enumRegistrant = (Class<Enum<?>>) this.registrant;
        final ElementRetrieverInvoker retriever = new ElementRetrieverInvoker(enumRegistrant);
        return Stream.of(enumRegistrant.getEnumConstants())
                .map(retriever::invoke)
                .collect(Collectors.toList());
    }

    private List<AnonymousElement> getStaticFieldRegistrantElements() {
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

    @Override
    public List<Class<?>> getAllClasses() {
        return ImmutableList.of(this.registrant);
    }

    @Override
    public <E> E getCommonIdentifier() {
        return null;
    }
}
