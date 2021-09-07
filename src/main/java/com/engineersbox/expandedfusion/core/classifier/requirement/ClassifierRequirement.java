package com.engineersbox.expandedfusion.core.classifier.requirement;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassifierRequirement<T, E extends Annotation> {

    private static final Logger LOGGER = LogManager.getLogger(ClassifierRequirement.class);

    public final RequirementCondition condition;
    public final Class<? extends T> inheritsFrom;
    public final Class<E> uniquelyMarkedBy;
    public final String commonIdentifier;

    public ClassifierRequirement(final RequirementCondition condition,
                                 final Class<? extends T> inheritsFrom,
                                 final Class<E> uniquelyMarkedBy,
                                 final String commonIdentifier) {
        this.condition = condition;
        this.inheritsFrom = inheritsFrom;
        this.uniquelyMarkedBy = uniquelyMarkedBy;
        this.commonIdentifier = commonIdentifier;
    }

    @SuppressWarnings("unchecked")
    public <F> Pair<Boolean, Pair<F, F>> testCommonIdentifier(final Class<?> clazz, final F identifierValue) {
        final E annotation = clazz == null ? null : clazz.getAnnotation(uniquelyMarkedBy);
        final F methodResultValue;
        try {
            final Method method = this.uniquelyMarkedBy.getDeclaredMethod(this.commonIdentifier);
            methodResultValue = (F) method.invoke(annotation);
        } catch (final IllegalAccessException | NoSuchMethodException | InvocationTargetException | NullPointerException e) {
            return ImmutablePair.of(false, ImmutablePair.of(null, identifierValue));
        }
        return ImmutablePair.of(
                annotation != null && methodResultValue.equals(identifierValue),
                ImmutablePair.of(methodResultValue, identifierValue)
        );
    }

    public Pair<Boolean, Pair<Class<?>, Class<?>>> testMarkedBy(final Class<?> clazz) {
        final E annotation = clazz == null ? null : clazz.getAnnotation(uniquelyMarkedBy);
        return ImmutablePair.of(
                annotation != null,
                ImmutablePair.of(uniquelyMarkedBy, annotation == null ? null : uniquelyMarkedBy)
        );
    }

    public Pair<Boolean, Pair<Class<?>, Class<?>>> testMatchesType(final Class<?> clazz) {
        return ImmutablePair.of(
                clazz != null && this.inheritsFrom.isAssignableFrom(clazz),
                ImmutablePair.of(this.inheritsFrom, clazz)
        );
    }

    public boolean isRequired() {
        return this.condition == RequirementCondition.REQUIRED;
    }
}
