package com.engineersbox.expandedfusion.core.registration.provider;

import com.engineersbox.expandedfusion.core.reflection.CheckedInstantiator;
import com.engineersbox.expandedfusion.core.registration.exception.provider.ResolverComponentInstantiationException;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;

public interface RegistrationResolver {

    void registerAll();

    default <T> T instantiateWithDefaultConstructor(@Nonnull final Class<? extends T> impl) {
        try {
            return new CheckedInstantiator<T>()
                    .withImplementation(impl)
                    .newInstance();
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ResolverComponentInstantiationException(e);
        }
    }

}
