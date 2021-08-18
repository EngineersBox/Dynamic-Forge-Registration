package com.engineersbox.expandedfusion.core.registration.provider;

import com.engineersbox.expandedfusion.core.registration.exception.provider.ResolverComponentInstantiationException;
import org.reflections.ReflectionUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

public abstract class RegistrationResolver {

    public abstract void registerAll();

    @SuppressWarnings("unchecked")
    public <T> T instantiateWithDefaultConstructor(@Nonnull final Class<? extends T> impl) {
        final Set<Constructor> constructors = ReflectionUtils.getConstructors(
                impl,
                (c) -> c.getParameterCount() < 1
        );
        if (constructors.size() < 1) {
            throw new ResolverComponentInstantiationException(String.format(
                    "No accessible constructors could be found for %s",
                    impl.getName()
            ));
        }
        try {
            return (T) new ArrayList<>(constructors).get(0).newInstance();
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ResolverComponentInstantiationException(e);
        }
    }

}
