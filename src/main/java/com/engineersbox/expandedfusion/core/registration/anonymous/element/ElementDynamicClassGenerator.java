package com.engineersbox.expandedfusion.core.registration.anonymous.element;

import com.engineersbox.expandedfusion.core.registration.exception.anonymous.RuntimeElementClassGenerationException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ElementDynamicClassGenerator<T> {

    private final Class<? extends T> baseClass;
    private Constructor<? extends T> declaredConstructor;
    private Class<?>[] parameterTypes;
    private final List<Annotation> annotationsToApply;
    private DynamicType.Unloaded<? extends T> unloadedClass;
    private Class<? extends T> loadedClass;

    public ElementDynamicClassGenerator(final Class<? extends T> baseClass) {
        this.baseClass = baseClass;
        this.annotationsToApply = new ArrayList<>();
    }

    public ElementDynamicClassGenerator<T> retrieveDeclaredConstructor(final Class<?> ...parameterTypes) {
        this.parameterTypes = parameterTypes;
        try {
            this.declaredConstructor = this.baseClass.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeElementClassGenerationException(String.format(
                    "Could not find declared constructor on base class %s for parameter types %s",
                    this.baseClass.getName(),
                    Arrays.stream(parameterTypes).map(Class::getName).collect(Collectors.joining(", "))
            ), e);
        }
        return this;
    }

    public ElementDynamicClassGenerator<T> withAnnotation(final Annotation annotation) {
        this.annotationsToApply.add(annotation);
        return this;
    }

    public ElementDynamicClassGenerator<T> createUnloadedClass(final String className) {
        this.unloadedClass = new ByteBuddy()
                .subclass(this.baseClass, ConstructorStrategy.Default.IMITATE_SUPER_CLASS)
                .name(className)
                .annotateType(this.annotationsToApply)
                .make();
        return this;
    }

    public ElementDynamicClassGenerator<T> loadClass() {
        /**
         * Class loader used here needs to be the same as as the base class context since
         * {@link ClassLoader#getSystemClassLoader()} will be a different loader than the
         * one used for parameters that will eventually be used to construct a class
         * instanced from the loaded class. In order to ensure they are they same
         * this will be loaded from the {@code this.baseClass.getClassLoader()} class loader
         */
        this.loadedClass = this.unloadedClass.load(this.baseClass.getClassLoader()).getLoaded();
        return this;
    }

    @SuppressWarnings("unchecked")
    public <E extends T> E getInstance(final Object ...parameters) {
        final Constructor<?> constructor;
        try {
            constructor = this.loadedClass.getConstructor(this.declaredConstructor.getParameterTypes());
        } catch (final NoSuchMethodException e) {
            throw new RuntimeElementClassGenerationException(String.format(
                    "Could not find declared constructor on loaded class %s for parameter types %s",
                    this.loadedClass.getName(),
                    Arrays.stream(this.parameterTypes).map(Class::getName).collect(Collectors.joining(", "))
            ), e);
        }
        final Object uncastInstance;
        try {
            uncastInstance = constructor.newInstance(parameters);
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeElementClassGenerationException(String.format(
                    "Could not invoke constructor %s on loaded class %s for parameter types %s",
                    constructor.getName(),
                    this.loadedClass.getName(),
                    Arrays.stream(this.parameterTypes).map(Class::getName).collect(Collectors.joining(", "))
            ), e);
        }
        return (E) uncastInstance;
    }
}
