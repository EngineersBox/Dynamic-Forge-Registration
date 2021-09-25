package com.engineersbox.expandedfusion.core.reflection;

import com.engineersbox.expandedfusion.core.reflection.exception.FieldAccessorException;
import net.minecraft.block.material.Material;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public class FieldAccessor {

    public static <T> T getStaticFieldValue(final String fieldName,
                                            final Class<T> classOfT) {
        return getFieldValue(fieldName, classOfT, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(final String fieldName,
                                      final Class<T> classOfT,
                                      final T instance) {
        final Field[] fields = classOfT.getDeclaredFields();
        final Optional<Field> fieldMatch = Arrays.stream(fields)
                .filter((final Field field) -> field.getType().isAssignableFrom(classOfT) && field.getName().equals(fieldName))
                .findFirst();
        if (fieldMatch.isPresent()) {
            try {
                return (T) fieldMatch.get().get(instance);
            } catch (final IllegalAccessException e) {
                throw new FieldAccessorException(e);
            }
        }
        throw new FieldAccessorException(String.format(
                "Could not find field %s in class %s",
                fieldName,
                classOfT.getName()
        ));
    }

}
