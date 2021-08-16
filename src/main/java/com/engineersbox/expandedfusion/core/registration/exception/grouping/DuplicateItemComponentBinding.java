package com.engineersbox.expandedfusion.core.registration.exception.grouping;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;

import java.util.List;

public class DuplicateItemComponentBinding extends DuplicateComponentBinding {

    private static final List<Class<?>> ASSIGNABLE_TO_CHECK = ImmutableList.of(Item.class);

    public <T> DuplicateItemComponentBinding(final Class<? extends T> current, final Class<? extends T> duplicate) {
        super("item", current, duplicate, ASSIGNABLE_TO_CHECK);
    }
}
