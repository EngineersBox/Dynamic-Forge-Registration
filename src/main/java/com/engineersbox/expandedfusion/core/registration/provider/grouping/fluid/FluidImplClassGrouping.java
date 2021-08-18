package com.engineersbox.expandedfusion.core.registration.provider.grouping.fluid;

import com.engineersbox.expandedfusion.core.registration.annotation.provider.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

public class FluidImplClassGrouping extends ImplClassGroupings<FluidImplGrouping> {

    private final Reflections reflections;

    @Inject
    public FluidImplClassGrouping(@Named("packageReflections") final Reflections reflections) {
        this.reflections = reflections;
    }

    @Override
    public void collectAnnotatedResources() {
        final Set<Class<? extends ForgeFlowingFluid.Flowing>> flowingFluidProviderAnnotatedClasses = super.filterClassesBySuperType(
                ForgeFlowingFluid.Flowing.class,
                this.reflections.getTypesAnnotatedWith(FluidProvider.class)
        );
        for (final Class<? extends FlowingFluid> c : flowingFluidProviderAnnotatedClasses) {
            final FluidProvider annotation = c.getAnnotation(FluidProvider.class);
            if (annotation == null) {
                continue;
            }
            addIfNotExists(annotation.name(), c);
        }
        final Set<Class<? extends ForgeFlowingFluid.Source>> sourceFluidProviderAnnotatedClasses = super.filterClassesBySuperType(
                ForgeFlowingFluid.Source.class,
                this.reflections.getTypesAnnotatedWith(FluidProvider.class)
        );
        for (final Class<? extends Fluid> c : sourceFluidProviderAnnotatedClasses) {
            final FluidProvider annotation = c.getAnnotation(FluidProvider.class);
            if (annotation == null) {
                continue;
            }
            addIfNotExists(annotation.name(), c);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addIfNotExists(String name, Class<?> toAdd) {
         FluidImplGrouping fluidImplGrouping = this.classGroupings.get(name);
        if (fluidImplGrouping == null) {
            fluidImplGrouping = new FluidImplGrouping();
        }
        if (ForgeFlowingFluid.Flowing.class.isAssignableFrom(toAdd)) {
            fluidImplGrouping.setFlowingFluid((Class<? extends FlowingFluid>) toAdd);
        } else if (ForgeFlowingFluid.Source.class.isAssignableFrom(toAdd)) {
            fluidImplGrouping.setSourceFluid((Class<? extends Fluid>) toAdd);
        }
        this.classGroupings.put(name, fluidImplGrouping);
    }
}
