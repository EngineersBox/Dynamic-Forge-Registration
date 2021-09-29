package com.engineersbox.expandedfusion.core.reflection;

import com.engineersbox.expandedfusion.core.reflection.exception.FieldAccessorException;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FieldAccessor {

    public static <T> T getStaticFieldValue(final String fieldName,
                                            final Class<T> classOfT) {
        final Predicate<? super Field> fieldPredicate = createFieldPredicate(
                fieldName,
                classOfT,
                Modifier.PUBLIC,
                Modifier.STATIC
        );
        return getFromField(
                fieldName,
                classOfT,
                null,
                fieldPredicate
        );
    }

    public static <T> T getFieldValue(final String fieldName,
                                      final Class<T> classOfT,
                                      final T instance) {
        final Predicate<? super Field> fieldPredicate = createFieldPredicate(
                fieldName,
                classOfT,
                Modifier.PUBLIC
        );
        return getFromField(
                fieldName,
                classOfT,
                instance,
                fieldPredicate
        );
    }

    public static <T> Predicate<? super Field> createFieldPredicate(final String fieldName,
                                                                    final Class<T> classOfT,
                                                                    final int ...modifiers) {
        Predicate<? super Field> fieldPredicate = org.reflections.util.Utils.and(
                ReflectionUtils.withTypeAssignableTo(classOfT),
                ReflectionUtils.withName(fieldName)
        );
        for (final int modifier : modifiers) {
            fieldPredicate = org.reflections.util.Utils.and(fieldPredicate, ReflectionUtils.withModifier(modifier));
        }
        return fieldPredicate;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFromField(final String fieldName,
                                     final Class<T> classOfT,
                                     final T instance,
                                     Predicate<? super Field> predicate) {
        final List<Field> fields = new ArrayList<>(ReflectionUtils.getFields(
                classOfT,
                predicate
        ));
        if (fields.isEmpty()) {
            throw new FieldAccessorException(String.format(
                    "Could not find field %s in class %s",
                    fieldName,
                    classOfT.getName()
            ));
        }
        try {
            return (T) fields.get(0).get(instance);
        } catch (final IllegalAccessException e) {
            throw new FieldAccessorException(e);
        }
    }

}
