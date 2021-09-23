package com.engineersbox.expandedfusion.core.registration.provider.grouping.element.fluid;

import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidBucket;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.exception.grouping.element.fluid.DuplicateFluidComponentBinding;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplGrouping;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;

import java.util.ArrayList;
import java.util.List;

public class FluidImplGrouping implements ImplGrouping {

    private Class<? extends Fluid> source;
    private Class<? extends FlowingFluid> flowing;

    public Class<? extends Fluid> getSourceFluid() {
        return this.source;
    }

    public FluidProvider getSourceFluidProviderAnnotation() {
        if (this.source == null) {
            return null;
        }
        return this.source.getAnnotation(FluidProvider.class);
    }

    public FluidBucket getSourceFluidBucketAnnotation() {
        if (this.source == null) {
            return null;
        }
        return this.source.getAnnotation(FluidBucket.class);
    }

    public void setSourceFluid(final Class<? extends Fluid> sourceFluid) throws DuplicateFluidComponentBinding {
        if (this.source != null) {
            throw new DuplicateFluidComponentBinding(this.source, sourceFluid);
        }
        this.source = sourceFluid;
    }

    public Class<? extends FlowingFluid> getFlowingFluid() {
        return this.flowing;
    }

    public FluidProvider getFlowingFluidProviderAnnotation() {
        if (this.flowing == null) {
            return null;
        }
        return this.flowing.getAnnotation(FluidProvider.class);
    }

    public FluidBucket getFlowingFluidBucketAnnotation() {
        if (this.flowing == null) {
            return null;
        }
        return this.flowing.getAnnotation(FluidBucket.class);
    }

    public void setFlowingFluid(final Class<? extends FlowingFluid> flowingFluid) throws DuplicateFluidComponentBinding {
        if (this.flowing != null) {
            throw new DuplicateFluidComponentBinding(this.source, flowingFluid);
        }
        this.flowing = flowingFluid;
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
