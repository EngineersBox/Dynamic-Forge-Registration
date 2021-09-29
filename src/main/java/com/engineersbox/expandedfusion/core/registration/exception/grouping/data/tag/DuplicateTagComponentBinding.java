package com.engineersbox.expandedfusion.core.registration.exception.grouping.data.tag;

import com.engineersbox.expandedfusion.core.registration.exception.grouping.DuplicateComponentBinding;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;

public class DuplicateTagComponentBinding extends DuplicateComponentBinding {

    private static final List<Class<?>> ASSIGNABLE_TO_CHECK = ImmutableList.of(ForgeRegistryEntry.class);

    public <T> DuplicateTagComponentBinding(final Class<? extends T> current, final Class<? extends T> duplicate) {
        super("tag", current, duplicate, ASSIGNABLE_TO_CHECK);
    }
}
