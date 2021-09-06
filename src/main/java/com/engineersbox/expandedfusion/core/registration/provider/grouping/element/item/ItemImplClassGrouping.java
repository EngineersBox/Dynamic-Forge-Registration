package com.engineersbox.expandedfusion.core.registration.provider.grouping.element.item;

import com.engineersbox.expandedfusion.core.reflection.ReflectionClassFilter;
import com.engineersbox.expandedfusion.core.registration.annotation.element.item.ItemProvider;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.item.Item;
import org.reflections.Reflections;

import java.util.Set;

public class ItemImplClassGrouping extends ImplClassGroupings<ItemImplGrouping> {

    private final Reflections reflections;

    @Inject
    public ItemImplClassGrouping(@Named("packageReflections") final Reflections reflections) {
        this.reflections = reflections;
    }

    @Override
    public void collectAnnotatedResources() {
        final Set<Class<? extends Item>> blockProviderAnnotatedClasses = ReflectionClassFilter.filterClassesBySuperType(
                Item.class,
                this.reflections.getTypesAnnotatedWith(ItemProvider.class)
        );
        for (final Class<? extends Item> c : blockProviderAnnotatedClasses) {
            final ItemProvider annotation = c.getAnnotation(ItemProvider.class);
            if (annotation == null) {
                continue;
            }
            addIfNotExists(annotation.name(), c);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addIfNotExists(final String name, final Class<?> toAdd) {
        ItemImplGrouping itemImplGrouping = this.classGroupings.get(name);
        if (itemImplGrouping == null) {
            itemImplGrouping = new ItemImplGrouping();
        }
        if (Item.class.isAssignableFrom(toAdd)) {
            itemImplGrouping.setItem((Class<? extends Item>) toAdd);
        }
        this.classGroupings.put(name, itemImplGrouping);
    }
}
