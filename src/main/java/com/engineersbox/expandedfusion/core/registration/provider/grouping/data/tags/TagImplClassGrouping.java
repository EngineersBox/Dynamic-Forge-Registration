package com.engineersbox.expandedfusion.core.registration.provider.grouping.data.tags;

import com.engineersbox.expandedfusion.core.reflection.ReflectionClassFilter;
import com.engineersbox.expandedfusion.core.registration.annotation.data.tag.Tag;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.element.item.ItemProvider;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.reflections.Reflections;

import java.util.Set;

public class TagImplClassGrouping extends ImplClassGroupings<TagImplGrouping>  {

    private final Reflections reflections;

    @Inject
    public TagImplClassGrouping(@Named("packageReflections") final Reflections reflections) {
        this.reflections = reflections;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void collectAnnotatedResources() {
        final Set<Class<? extends ForgeRegistryEntry>> elementProviderAnnotatedClasses = ReflectionClassFilter.filterClassesBySuperType(
                ForgeRegistryEntry.class,
                this.reflections.getTypesAnnotatedWith(Tag.class)
        );
        for (final Class<? extends ForgeRegistryEntry> c : elementProviderAnnotatedClasses) {
            final ItemProvider itemProvider = c.getAnnotation(ItemProvider.class);
            final BlockProvider blockProvider = c.getAnnotation(BlockProvider.class);
            final FluidProvider fluidProvider = c.getAnnotation(FluidProvider.class);
            if ((itemProvider == null) == (blockProvider == null) == (fluidProvider == null)) {
                continue;
            }
            final Tag[] tagProviders = c.getAnnotationsByType(Tag.class);
            if (tagProviders.length == 0) {
                continue;
            }
            if (itemProvider != null) {
                addIfNotExists(itemProvider.name(), c);
            } else if (blockProvider != null) {
                addIfNotExists(blockProvider.name(), c);
            } else {
                addIfNotExists(fluidProvider.name(), c);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addIfNotExists(String name, Class<?> toAdd) {
        TagImplGrouping tagImplGrouping = this.classGroupings.get(name);
        if (tagImplGrouping == null) {
            tagImplGrouping = new TagImplGrouping();
        }
        if (ForgeRegistryEntry.class.isAssignableFrom(toAdd)) {
            tagImplGrouping.setRegistryEntry((Class<? extends ForgeRegistryEntry<?>>) toAdd);
        }
        this.classGroupings.put(name, tagImplGrouping);
    }
}
