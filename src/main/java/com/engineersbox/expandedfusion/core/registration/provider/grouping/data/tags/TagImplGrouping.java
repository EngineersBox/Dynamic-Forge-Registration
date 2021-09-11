package com.engineersbox.expandedfusion.core.registration.provider.grouping.data.tags;

import com.engineersbox.expandedfusion.core.registration.annotation.data.tag.Tag;
import com.engineersbox.expandedfusion.core.registration.exception.grouping.data.tag.DuplicateTagComponentBinding;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplGrouping;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public class TagImplGrouping implements ImplGrouping {

    private Class<? extends ForgeRegistryEntry<?>> registryEntry;

    public Class<? extends ForgeRegistryEntry<?>> getRegistryEntry() {
        return this.registryEntry;
    }

    public Tag[] getTagAnnotations() {
        if (this.registryEntry == null) {
            return new Tag[0];
        }
        return this.registryEntry.getAnnotationsByType(Tag.class);
    }

    public void setRegistryEntry(final Class<? extends ForgeRegistryEntry<?>> registryEntry) {
        if (this.registryEntry != null) {
            throw new DuplicateTagComponentBinding(this.registryEntry, registryEntry);
        }
        this.registryEntry = registryEntry;
    }

    @Override
    public List<Class<?>> getAllClasses() {
        return new ArrayList<>();
    }

    @Override
    public <E> E getCommonIdentifier() {
        return null;
    }

}
