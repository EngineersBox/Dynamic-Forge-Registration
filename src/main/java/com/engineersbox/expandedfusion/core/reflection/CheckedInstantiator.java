package com.engineersbox.expandedfusion.core.reflection;

import com.engineersbox.expandedfusion.core.reflection.exception.ConstructorInstantiationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class CheckedInstantiator<T> {

    private static final Logger LOGGER = LogManager.getLogger(CheckedInstantiator.class);
    private Set<Constructor> constructors;

    private Class<? extends T> implementation;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
    private boolean ignoreVarArgs = false;
    private boolean preferVarArgs = false;

    public CheckedInstantiator() {
        this.parameterTypes = new Class<?>[]{};
        this.parameters = new Object[]{};
    }

    public CheckedInstantiator<T> withImplementation(final Class<? extends T> implementation) {
        this.implementation = implementation;
        return this;
    }

    public CheckedInstantiator<T> withParameterTypes(final Class<?> ...types) {
        this.parameterTypes = types;
        return this;
    }

    public CheckedInstantiator<T> withParameters(final Object ...args) {
        this.parameters = args;
        return this;
    }

    public CheckedInstantiator<T> ignoreVarArgs(final boolean ignoreVarArgs) {
        this.ignoreVarArgs = ignoreVarArgs;
        return this;
    }

    public CheckedInstantiator<T> preferVarArgsConstructor(final boolean preferVarArgs) {
        this.preferVarArgs = preferVarArgs;
        return this;
    }

    @SuppressWarnings("rawtypes")
    private boolean matchParamTypes(final Constructor c) {
        if (c.getDeclaringClass() != this.implementation) {
            return false;
        }
        final Class<?>[] constructorParamTypes = c.getParameterTypes();
        final boolean constructorHasExtraVarArgsParam = constructorParamTypes.length == this.parameterTypes.length + 1;
        if (constructorParamTypes.length < this.parameterTypes.length
            || constructorParamTypes.length >= this.parameterTypes.length + 2
            || (constructorHasExtraVarArgsParam && !this.ignoreVarArgs)) {
            return false;
        }
        for (int i = 0; i < this.parameterTypes.length; i++) {
            if (!this.parameterTypes[i].isAssignableFrom(constructorParamTypes[i])) {
                return false;
            }
        }
        if (constructorHasExtraVarArgsParam) {
            return constructorParamTypes[constructorParamTypes.length - 1].isArray();
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void filterAvailableConstructors() {
        this.constructors = ReflectionUtils.getConstructors(
                this.implementation,
                this::matchParamTypes
        );
        if (constructors.isEmpty()) {
            throw new ConstructorInstantiationException(String.format(
                    "No accessible constructors could be found for %s",
                    this.implementation
            ));
        }
    }

    @SuppressWarnings("unchecked, rawtypes")
    public T newInstance() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (this.implementation == null) {
            throw new NullPointerException("Cannot invoke constructor of null implementation class");
        }
        filterAvailableConstructors();
        final List<Constructor> constructorsList = new ArrayList<>(this.constructors);
        int desiredConstructorIndex = 0;
        if (!this.constructors.isEmpty()) {
            if (this.preferVarArgs) {
                for (int i = 0; i < constructorsList.size(); i++) {
                    final Class<?>[] constructorParamTypes = constructorsList.get(i).getParameterTypes();
                    if (constructorParamTypes.length == this.parameterTypes.length + 1 && constructorParamTypes[constructorParamTypes.length - 1].isArray()) {
                        desiredConstructorIndex = i;
                        break;
                    }
                }
                if (this.constructors.size() > 1) {
                    LOGGER.debug(
                            "Multiple ({}) constructors found for {} and preferVarArgs was set, using closest match at index {}",
                            constructorsList.size(),
                            this.implementation.getName(),
                            desiredConstructorIndex
                    );
                }
            } else if (this.constructors.size() > 1) {
                LOGGER.debug(
                        "Multiple ({}) constructors found for {}, defaulting to first available at index 0",
                        constructorsList.size(),
                        this.implementation.getName()
                );
            }
        }
        return (T) constructorsList.get(desiredConstructorIndex).newInstance(this.parameters);
    }

}
