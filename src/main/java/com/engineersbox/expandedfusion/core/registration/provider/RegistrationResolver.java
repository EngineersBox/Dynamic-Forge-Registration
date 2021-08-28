package com.engineersbox.expandedfusion.core.registration.provider;

import com.engineersbox.expandedfusion.core.reflection.CheckedInstantiator;
import com.engineersbox.expandedfusion.core.registration.exception.provider.ResolverComponentInstantiationException;
import net.minecraft.block.Block;
import org.reflections.ReflectionUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

public abstract class RegistrationResolver {

    public abstract void registerAll();

    public <T> T instantiateWithDefaultConstructor(@Nonnull final Class<? extends T> impl) {
        try {
            return new CheckedInstantiator<T>()
                    .withImplementation(impl)
                    .newInstance();
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ResolverComponentInstantiationException(e);
        }
    }

}
