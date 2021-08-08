package com.engineersbox.expandedfusion.register.registry.provider.grouping;

import com.engineersbox.expandedfusion.register.registry.exception.DuplicateBlockComponentBinding;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ImplClassGroupings<T extends ImplGrouping> {

    final Map<String, T> classGroupings = new HashMap<>();

    public abstract void collectAnnotatedResources();

    @SuppressWarnings("unchecked")
    <E> Set<Class<? extends E>> filterClassesBySuperType(final Class<E> superType, final Set<Class<?>> classes) {
        return classes.stream()
            .filter(superType::isAssignableFrom)
            .map(c -> (Class<? extends E>) c)
            .collect(Collectors.toSet());
    }

    abstract void addIfNotExists(final String name, final Class<?> toAdd);

    public Map<String, T> getClassGroupings() {
        return this.classGroupings;
    }
}
