package com.engineersbox.expandedfusion.core.reflection;

import com.engineersbox.expandedfusion.core.reflection.exception.AnnotationFieldValueUpdateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationFieldUpdater<T> {

    private static final Logger LOGGER = LogManager.getLogger(AnnotationFieldUpdater.class);
    private static final String ANNOTATIONS_FIELD_KEY = "annotations";
    private static final String ANNOTATION_DATA_METHOD_KEY = "annotationData";

    private final T instance;
    private final Map<Class<? extends Annotation>, Annotation> annotationUpdates;

    public AnnotationFieldUpdater(final T instance) {
        this.instance = instance;
        this.annotationUpdates = new HashMap<>();
    }

    public AnnotationFieldUpdater<T> withNewValue(final Class<? extends Annotation> annotationClass,
                                                  final Annotation annotation) {
        this.annotationUpdates.put(annotationClass, annotation);
        return this;
    }

    public void performUpdates() {
        this.annotationUpdates.forEach(this::invokeUpdateOnAnnotation);
    }

    @SuppressWarnings("java:S3011")
    private void invokeUpdateOnAnnotation(final Class<? extends Annotation> annotationClass,
                                          final Annotation annotation) {
        final Method method = getAnnotationDataMethod();
        final Object annotationData;
        try {
            annotationData = method.invoke(this.instance.getClass());
        } catch (final InvocationTargetException | IllegalAccessException e) {
            throw new AnnotationFieldValueUpdateException(String.format(
                    "Could not invoke method %s on instance %s",
                    method.getName(),
                    this.instance.getClass().getName()
            ), e);
        }
        final Field annotations = getAnnotationsField(annotationData);
        annotations.setAccessible(true);
        final Map<Class<? extends Annotation>, Annotation> mappedAnnotationInstances = retrieveMappedAnnotationInstances(annotations, annotationData);
        if (mappedAnnotationInstances.containsKey(annotationClass)) {
            LOGGER.warn(
                    "Class {} already has bound annotation {}, overwriting previous binding",
                    this.instance.getClass().getName(),
                    annotationClass.getName()
            );
        }
        mappedAnnotationInstances.put(annotationClass, annotation);
    }

    @SuppressWarnings("java:S3011")
    private Method getAnnotationDataMethod() {
        final Method method;
        try {
            method = Class.class.getDeclaredMethod(ANNOTATION_DATA_METHOD_KEY, null);
        } catch (final NoSuchMethodException e) {
            throw new AnnotationFieldValueUpdateException("Could not find 'annotationData' method for Class", e);
        }
        method.setAccessible(true);
        return method;
    }

    @SuppressWarnings("java:S3011")
    private Field getAnnotationsField(final Object annotationData) {
        final Field annotations;
        try {
            annotations = annotationData.getClass().getDeclaredField(ANNOTATIONS_FIELD_KEY);
        } catch (final NoSuchFieldException e) {
            throw new AnnotationFieldValueUpdateException("No such field 'annotations' on AnnotationData class", e);
        }
        annotations.setAccessible(true);
        return annotations;
    }

    @SuppressWarnings("unchecked")
    private Map<Class<? extends Annotation>, Annotation> retrieveMappedAnnotationInstances(final Field annotations,
                                                                                           final Object annotationData) {
        final Map<Class<? extends Annotation>, Annotation> mappedAnnotationInstances;
        try {
            final Object uncastAnnotationData = annotations.get(annotationData);
            if (!(uncastAnnotationData instanceof Map)) {
                throw new AnnotationFieldValueUpdateException("Instance provides no 'annotationData' field of type Map<Class<? extends Annotation>, Annotation>. Class is a potential subclass with dangerous overrides.");
            }
            mappedAnnotationInstances = (Map<Class<? extends Annotation>, Annotation>) uncastAnnotationData;
        } catch (final IllegalAccessException e) {
            throw new AnnotationFieldValueUpdateException(String.format(
                    "Instance %s prevents access to 'annotationData' field",
                    this.instance.getClass().getName()
            ), e);
        }
        return mappedAnnotationInstances;
    }
}
