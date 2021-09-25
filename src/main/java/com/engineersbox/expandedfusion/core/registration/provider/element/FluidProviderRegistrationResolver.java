package com.engineersbox.expandedfusion.core.registration.provider.element;

import com.engineersbox.expandedfusion.core.functional.PredicateSplitterConsumer;
import com.engineersbox.expandedfusion.core.registration.annotation.element.block.BlockProperties;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidBucket;
import com.engineersbox.expandedfusion.core.registration.annotation.element.fluid.FluidProvider;
import com.engineersbox.expandedfusion.core.registration.annotation.resolver.RegistrationPhaseHandler;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryObjectContext;
import com.engineersbox.expandedfusion.core.registration.contexts.provider.ElementRegistryProvider;
import com.engineersbox.expandedfusion.core.registration.exception.provider.element.ProviderElementRegistrationException;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.fluid.FluidImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.element.fluid.FluidImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.service.*;
import com.engineersbox.expandedfusion.core.registration.provider.service.element.BlockDeferredRegistryService;
import com.engineersbox.expandedfusion.core.registration.provider.service.element.FluidDeferredRegistryService;
import com.engineersbox.expandedfusion.core.registration.provider.service.element.ItemDeferredRegistryService;
import com.engineersbox.expandedfusion.core.registration.registryObject.element.BlockRegistryObject;
import com.engineersbox.expandedfusion.core.registration.resolver.ResolverPhase;
import com.google.inject.Inject;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Supplier;

@RegistrationPhaseHandler(ResolverPhase.FLUID)
public class FluidProviderRegistrationResolver implements RegistrationResolver {

    private static final Logger LOGGER = LogManager.getLogger(FluidProviderRegistrationResolver.class);

    private final FluidImplClassGrouping implClassGroupings;
    private final FluidDeferredRegistryService fluidDeferredRegistryService;
    private final BlockDeferredRegistryService blockDeferredRegistryService;
    private final ItemDeferredRegistryService itemDeferredRegistryService;
    final ElementRegistryProvider elementRegistryProvider;

    @Inject
    public FluidProviderRegistrationResolver(final ElementRegistryProvider elementRegistryProvider,
                                             final ImplClassGroupings<FluidImplGrouping> implClassGroupings,
                                             final RegistryService<Fluid> fluidDeferredRegistryService,
                                             final RegistryService<Block> blockDeferredRegistryService,
                                             final RegistryService<Item> itemDeferredRegistryService) {
        this.elementRegistryProvider = elementRegistryProvider;
        this.fluidDeferredRegistryService = (FluidDeferredRegistryService) fluidDeferredRegistryService;
        this.blockDeferredRegistryService = (BlockDeferredRegistryService) blockDeferredRegistryService;
        this.itemDeferredRegistryService = (ItemDeferredRegistryService) itemDeferredRegistryService;
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
            final BlockProperties[] blockProperties = sourceFluidProvider.blockProperties();
            if (blockProperties.length > 1) {
                LOGGER.warn(
                        "{} @BlockProperties annotations present on fluid {}, defaulting to first at index 0",
                        blockProperties.length,
                        sourceFluidProvider.name()
                );
            }
            registerFluidBlock(sourceFluidProvider.name(), blockProperties.length < 1 ? null : blockProperties[0]);
        }
        final Class<? extends Fluid> sourceFluidImpl = group.getSourceFluid();
        if (sourceFluidImpl == null) {
            throw new ProviderElementRegistrationException(String.format(
                    "No item implementation could be found with associated annotation: %s",
                    name
            ));
        }
        if (group.getSourceFluidBucketAnnotation() != null) {
            registerBucketItem(group);
        }
        registerSourceFluid(name, sourceFluidImpl);
    }

    @SuppressWarnings("unchecked")
    private void registerBucketItem(final FluidImplGrouping group) {
        final FluidBucket bucketProperties = group.getSourceFluidBucketAnnotation();
        final Supplier<? extends Item> bucketSupplier = () -> {
            final Supplier<? extends Fluid> fluidSupplier = () -> RegistryObjectContext.getSourceFluidRegistryObject(group.getSourceFluidProviderAnnotation().name()).asFluid();
            if (bucketProperties.canPlace() && FlowingFluid.class.isAssignableFrom(group.getSourceFluid())) {
                return this.itemDeferredRegistryService.createBucketItem((Supplier<FlowingFluid>) fluidSupplier, bucketProperties.tabGroup());
            }
            return this.itemDeferredRegistryService.createNoPlaceBucketItem((Supplier<Fluid>) fluidSupplier, bucketProperties.tabGroup());
        };
        this.itemDeferredRegistryService.register(
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

    private void registerFluidBlock(final String name,
                                    final BlockProperties blockProperties) {
        final Supplier<FlowingFluid> flowingFluidSupplier = () -> (FlowingFluid) RegistryObjectContext.getSourceFluidRegistryObject(name).asFluid();
        BlockRegistryObject<FlowingFluidBlock> fluidBlock;
        if (blockProperties == null) {
            fluidBlock = this.blockDeferredRegistryService.registerFluid(
                    name,
                    flowingFluidSupplier
            );
        } else {
            fluidBlock = this.blockDeferredRegistryService.registerFluid(
                    name,
                    flowingFluidSupplier,
                    this.blockDeferredRegistryService.assemblePropertiesFromAnnotation(blockProperties)
            );
        }
        this.elementRegistryProvider.blocks.put(
                name,
                fluidBlock
        );
    }

    private void registerSourceFluid(final String name,
                                     final Class<? extends Fluid> sourceFluidImpl) {
        this.elementRegistryProvider.sourceFluids.put(
                name,
                this.fluidDeferredRegistryService.register(
                        name,
                        () -> this.<Fluid>instantiateWithDefaultConstructor(sourceFluidImpl)
                )
        );
    }

    private void registerFlowingFluid(final String name,
                                      final Class<? extends FlowingFluid> fluidImpl) {
        this.elementRegistryProvider.flowingFluids.put(
                name,
                this.fluidDeferredRegistryService.register(
                        name,
                        () -> this.<FlowingFluid>instantiateWithDefaultConstructor(fluidImpl)
                )
        );
    }
}
