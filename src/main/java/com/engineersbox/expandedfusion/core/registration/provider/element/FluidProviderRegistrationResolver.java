package com.engineersbox.expandedfusion.core.registration.provider.element;

import com.engineersbox.expandedfusion.core.functional.PredicateSplitterConsumer;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidBucketProperties;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.resolver.RegistrationPhaseHandler;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.exception.provider.element.ProviderElementRegistrationException;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.RegistryProvider;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.fluid.FluidImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.fluid.FluidImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.shim.*;
import com.engineersbox.expandedfusion.core.registration.provider.shim.element.BlockDeferredRegistryShim;
import com.engineersbox.expandedfusion.core.registration.provider.shim.element.FluidDeferredRegistryShim;
import com.engineersbox.expandedfusion.core.registration.provider.shim.element.ItemDeferredRegistryShim;
import com.engineersbox.expandedfusion.core.registration.resolver.ResolverPhase;
import com.google.inject.Inject;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Supplier;

@RegistrationPhaseHandler(ResolverPhase.FLUID)
public class FluidProviderRegistrationResolver extends RegistrationResolver {

    private final FluidImplClassGrouping implClassGroupings;
    private final FluidDeferredRegistryShim fluidDeferredRegistryShim;
    private final BlockDeferredRegistryShim blockDeferredRegistryShim;
    private final ItemDeferredRegistryShim itemDeferredRegistryShim;
    final RegistryProvider registryProvider;

    @Inject
    public FluidProviderRegistrationResolver(final RegistryProvider registryProvider,
                                             final ImplClassGroupings<FluidImplGrouping> implClassGroupings,
                                             final RegistryShim<Fluid> fluidDeferredRegistryShim,
                                             final RegistryShim<Block> blockDeferredRegistryShim,
                                             final RegistryShim<Item> itemDeferredRegistryShim) {
        this.registryProvider = registryProvider;
        this.fluidDeferredRegistryShim = (FluidDeferredRegistryShim) fluidDeferredRegistryShim;
        this.blockDeferredRegistryShim = (BlockDeferredRegistryShim) blockDeferredRegistryShim;
        this.itemDeferredRegistryShim = (ItemDeferredRegistryShim) itemDeferredRegistryShim;
        this.implClassGroupings = (FluidImplClassGrouping) implClassGroupings;
        this.implClassGroupings.collectAnnotatedResources();
    }

    @Override
    public void registerAll() {
        this.implClassGroupings.getClassGroupings()
                .entrySet()
                .forEach(new PredicateSplitterConsumer<>(
                        (final Map.Entry<String, FluidImplGrouping> entry) -> entry.getValue().getSourceFluid() != null,
                        (final Map.Entry<String, FluidImplGrouping> entry) -> this.registerSourceProviderAnnotatedFluid(entry.getKey(), entry.getValue()),
                        (final Map.Entry<String, FluidImplGrouping> entry) -> this.registerFlowingProviderAnnotatedFluid(entry.getKey(), entry.getValue())
                ));
    }

    private void registerSourceProviderAnnotatedFluid(@Nonnull final String name,
                                                      @Nonnull final FluidImplGrouping group) {
        final FluidProvider sourceFluidProvider = group.getSourceFluidProviderAnnotation();
        if (sourceFluidProvider == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "Item implementation %s has no plausible annotation",
                    name
            ));
        }
        if (!sourceFluidProvider.name().equals(name)) {
            throw new ProviderElementRegistrationException(String.format(
                    "Mismatched provider element name against annotation: %s != %s",
                    name,
                    sourceFluidProvider.name()
            ));
        }
        if (!sourceFluidProvider.gaseous()) {
            registerFluidBlock(sourceFluidProvider.name());
        }
        final Class<? extends Fluid> sourceFluidImpl = group.getSourceFluid();
        if (sourceFluidImpl == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "No item implementation could be found with associated annotation: %s",
                    name
            ));
        }
        if (sourceFluidProvider.bucket().length > 0) {
            registerBucketItem(sourceFluidProvider, group);
        }
        registerSourceFluid(name, sourceFluidImpl);
    }

    @SuppressWarnings("unchecked")
    private void registerBucketItem(final FluidProvider sourceFluidProvider,
                                    final FluidImplGrouping group) {
        final FluidBucketProperties[] fluidBucketPropertiesArray = sourceFluidProvider.bucket();
        final FluidBucketProperties bucketProperties = fluidBucketPropertiesArray[0];
        final Supplier<? extends Item> bucketSupplier = () -> {
            final Supplier<? extends Fluid> fluidSupplier = () -> RegistryObjectContext.getSourceFluidRegistryObject(sourceFluidProvider.name()).asFluid();
            if (bucketProperties.canPlace() && FlowingFluid.class.isAssignableFrom(group.getSourceFluid())) {
                return this.itemDeferredRegistryShim.createBucketItem((Supplier<FlowingFluid>) fluidSupplier, bucketProperties.tabGroup());
            }
            return this.itemDeferredRegistryShim.createNoPlaceBucketItem((Supplier<Fluid>) fluidSupplier, bucketProperties.tabGroup());
        };
        this.itemDeferredRegistryShim.register(
                bucketProperties.name(),
                bucketSupplier
        );
    }

    private void registerFlowingProviderAnnotatedFluid(@Nonnull final String name,
                                                       @Nonnull final FluidImplGrouping group) {
        final FluidProvider flowingFluidProvider = group.getFlowingFluidProviderAnnotation();
        if (flowingFluidProvider == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "Item implementation %s has no plausible annotation",
                    name
            ));
        }
        if (!flowingFluidProvider.name().equals(name)) {
            throw new ProviderElementRegistrationException(String.format(
                    "Mismatched provider element name against annotation: %s != %s",
                    name,
                    flowingFluidProvider.name()
            ));
        }
        final Class<? extends FlowingFluid> flowingFluidImpl = group.getFlowingFluid();
        if (flowingFluidImpl == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "No item implementation could be found with associated annotation: %s",
                    name
            ));
        }
        registerFlowingFluid(name, flowingFluidImpl);
    }

    private void registerFluidBlock(final String name) {
        this.registryProvider.blocks.put(
                name,
                this.blockDeferredRegistryShim.registerFluid(
                        name,
                        () -> (FlowingFluid) RegistryObjectContext.getSourceFluidRegistryObject(name).asFluid()
                )
        );
    }

    private void registerSourceFluid(final String name,
                                     final Class<? extends Fluid> sourceFluidImpl) {
        this.registryProvider.sourceFluids.put(
                name,
                this.fluidDeferredRegistryShim.register(
                        name,
                        () -> super.<Fluid>instantiateWithDefaultConstructor(sourceFluidImpl)
                )
        );
    }

    private void registerFlowingFluid(final String name,
                                      final Class<? extends FlowingFluid> fluidImpl) {
        this.registryProvider.flowingFluids.put(
                name,
                this.fluidDeferredRegistryShim.register(
                        name,
                        () -> super.<FlowingFluid>instantiateWithDefaultConstructor(fluidImpl)
                )
        );
    }
}
