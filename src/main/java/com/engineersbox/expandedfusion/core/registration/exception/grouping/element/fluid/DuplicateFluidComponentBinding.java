package com.engineersbox.expandedfusion.core.registration.exception.grouping.element.fluid;

import com.engineersbox.expandedfusion.core.registration.exception.grouping.DuplicateComponentBinding;
import com.google.common.collect.ImmutableList;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;

import java.util.List;

public class DuplicateFluidComponentBinding extends DuplicateComponentBinding {

    private static final List<Class<?>> ASSIGNABLE_TO_CHECK = ImmutableList.of(
            FlowingFluid.class,
            Fluid.class
    );

    public <T> DuplicateFluidComponentBinding(final Class<? extends T> current,
                                              final Class<? extends T> duplicate) {
        super("fluid", current, duplicate, ASSIGNABLE_TO_CHECK);
    }
}
