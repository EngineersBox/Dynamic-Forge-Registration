package com.engineersbox.expandedfusion.register.registry.exception;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;

public class DuplicateBlockComponentBinding extends Exception {
    private DuplicateBlockComponentBinding(final String baseClassName, final Class<?> current, final Class<?> duplicate) {
        super(String.format(
                "Binding for block registration component [Base: %s] already exists. [Current: %s] [Duplicate: %s]",
                baseClassName,
                current.getName(),
                duplicate.getName()
        ));
    }

    public <T> DuplicateBlockComponentBinding(final Class<? extends T> current, final Class<? extends T> duplicate) {
        this(getBaseClassName(current), current, duplicate);
    }

    private static String getBaseClassName(final Class<?> current) {
        String baseClassName = "Unknown";
        if (Block.class.isAssignableFrom(current)) {
            baseClassName = Block.class.getName();
        } else if (TileEntity.class.isAssignableFrom(current)) {
            baseClassName = TileEntity.class.getName();
        } else if (Container.class.isAssignableFrom(current)) {
            baseClassName = Container.class.getName();
        }
        return baseClassName;
    }
}
