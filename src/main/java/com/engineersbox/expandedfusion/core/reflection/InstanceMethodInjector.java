package com.engineersbox.expandedfusion.core.reflection;

import com.engineersbox.expandedfusion.core.reflection.annotation.TargetedInjection;
import com.engineersbox.expandedfusion.core.reflection.exception.ClassMethodNotPresentException;
import com.engineersbox.expandedfusion.core.reflection.exception.MethodInvocationException;
import com.google.inject.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class InstanceMethodInjector<T> {

    private static final Logger LOGGER = LogManager.getLogger(InstanceMethodInjector.class.getName());

    private final Set<Method> methods;
    private final String methodName;
    private final T instance;

    @SuppressWarnings("unchecked")
    public InstanceMethodInjector(final T instance, final String methodName) {
        this.instance = instance;
        this.methodName = methodName;
        this.methods = ReflectionUtils.getMethods(
                instance.getClass(),
                ReflectionUtils.withName(methodName),
                ReflectionUtils.withAnnotation(Inject.class)
        );
        if (this.methods.isEmpty()) {
            throw new ClassMethodNotPresentException(instance.getClass(), methodName);
        }
    }

    @SuppressWarnings("unchecked")
    public <R> R invokeMethod(final Injector injector) {
        Set<Method> filteredMethods = this.methods.stream()
                .filter((final Method method) -> method.isAnnotationPresent(TargetedInjection.class))
                .collect(Collectors.toSet());
        if (filteredMethods.isEmpty()) {
            filteredMethods = this.methods;
        }
        Method resolvedMethod = null;
        List<Object> injectables = new ArrayList<>();
        for (final Method method : filteredMethods) {
            final Optional<List<Object>> resolvedInjectables = retrieveInjectablesFromInjector(method, injector);
            if (resolvedInjectables.isPresent()) {
                injectables.addAll(resolvedInjectables.get());
                resolvedMethod = method;
                break;
            }
        }
        if (resolvedMethod == null) {
            throw new ClassMethodNotPresentException(String.format(
                    "No variation of method %s on class %s could be found with resolvable parameters",
                    this.methodName,
                    this.instance.getClass()
            ));
        }
        try {
            return (R) resolvedMethod.invoke(this.instance, injectables.toArray());
        } catch (final InvocationTargetException | IllegalAccessException e) {
            throw new MethodInvocationException(String.format(
                    "Could not invoke injectable method %s",
                    resolvedMethod.getName()
            ), e);
        }
    }

    private Optional<List<Object>> retrieveInjectablesFromInjector(final Method method, final Injector injector) {
        final List<Object> injectables = new ArrayList<>();
        final Class<?>[] paramTypes = method.getParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < paramTypes.length; i++) {
            final Annotation[] annotations = paramAnnotations[i];
            Binding<?> binding = null;
            if (annotations.length < 1) {
                try {
                    binding = injector.getBinding(paramTypes[i]);
                } catch (final ConfigurationException ignored) {
                    LOGGER.trace(
                            "Could not find a binding for {} with pure class key, attempting to find it with annotations included",
                            paramTypes[i].getName()
                    );
                }
            } else {
                for (final Annotation annotation : annotations) {
                    final Key<?> key = Key.get(paramTypes[i], annotation);
                    try {
                        binding = injector.getBinding(key);
                    } catch (final ConfigurationException ignored) {
                        LOGGER.trace(
                                "No binding available for {}, skipping",
                                key
                        );
                    }
                }
            }
            if (binding == null) {
                continue;
            }
            injectables.add(binding.getProvider().get());
        }
        return injectables.size() == paramTypes.length ? Optional.of(injectables) : Optional.empty();
    }
}
