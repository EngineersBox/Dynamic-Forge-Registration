package com.engineersbox.expandedfusion.core.registration.registryObject;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RegistryObjectWrapper<T extends IForgeRegistryEntry<? super T>> implements Supplier<T> {
    protected final RegistryObject<T> registryObject;

    public RegistryObjectWrapper(final RegistryObject<T> registryObject) {
        this.registryObject = registryObject;
    }

    public RegistryObject<T> getRegistryObject() {
        return registryObject;
    }

    @Override
    public T get() {
        return registryObject.get();
    }

    public ResourceLocation getId() {
        return registryObject.getId();
    }

    public Stream<T> stream() {
        return registryObject.stream();
    }

    public boolean isPresent() {
        return registryObject.isPresent();
    }

    public void ifPresent(final Consumer<? super T> consumer) {
        registryObject.ifPresent(consumer);
    }

    public RegistryObject<T> filter(final Predicate<? super T> predicate) {
        return registryObject.filter(predicate);
    }

    public <U> Optional<U> map(final Function<? super T, ? extends U> mapper) {
        return registryObject.map(mapper);
    }

    public <U> Optional<U> flatMap(final Function<? super T, Optional<U>> mapper) {
        return registryObject.flatMap(mapper);
    }

    public<U> Supplier<U> lazyMap(final Function<? super T, ? extends U> mapper) {
        return registryObject.lazyMap(mapper);
    }

    public T orElse(final T other) {
        return registryObject.orElse(other);
    }

    public T orElseGet(final Supplier<? extends T> other) {
        return registryObject.orElseGet(other);
    }

    public <X extends Throwable> T orElseThrow(final Supplier<? extends X> exceptionSupplier) throws X {
        return registryObject.orElseThrow(exceptionSupplier);
    }

    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj instanceof RegistryObjectWrapper) {
            return Objects.equals(((RegistryObjectWrapper<?>)obj).getId(), getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
