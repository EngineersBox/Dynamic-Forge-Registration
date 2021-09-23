package com.engineersbox.expandedfusion.core.registration.exception.grouping.element.block;

import com.engineersbox.expandedfusion.core.registration.exception.grouping.DuplicateComponentBinding;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class DuplicateBlockComponentBinding extends DuplicateComponentBinding {

    private static final List<Class<?>> ASSIGNABLE_TO_CHECK = ImmutableList.of(
            Block.class,
            TileEntity.class,
            Container.class
    );

    public <T> DuplicateBlockComponentBinding(final Class<? extends T> current,
                                              final Class<? extends T> duplicate) {
        super("block", current, duplicate, ASSIGNABLE_TO_CHECK);
    }
}
