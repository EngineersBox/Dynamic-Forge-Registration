package com.engineersbox.expandedfusion.core.registration.provider.anonymous;

import com.engineersbox.expandedfusion.core.registration.annotation.resolver.RegistrationPhaseHandler;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.AttributedSupplier;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.contexts.RegistryProvider;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.anonymous.AnonymousElementImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.anonymous.AnonymousElementImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShim;
import com.engineersbox.expandedfusion.core.registration.provider.shim.element.*;
import com.engineersbox.expandedfusion.core.registration.resolver.ResolverPhase;
import com.google.inject.Inject;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RegistrationPhaseHandler(ResolverPhase.ANONYMOUS_ELEMENT)
public class AnonymousElementProviderRegistrationResolver extends RegistrationResolver {

    private final AnonymousElementImplClassGrouping implClassGroupings;
    private final BlockDeferredRegistryShim blockDeferredRegistryShim;
    private final ItemDeferredRegistryShim itemDeferredRegistryShim;
    private final FluidDeferredRegistryShim fluidDeferredRegistryShim;
    private final RegistryProvider registryProvider;

    @Inject
    public AnonymousElementProviderRegistrationResolver(final RegistryProvider registryProvider,
                                                        final ImplClassGroupings<AnonymousElementImplGrouping> implClassGroupings,
                                                        final RegistryShim<Block> blockDeferredRegistryShim,
                                                        final RegistryShim<Item> itemDeferredRegistryShim,
                                                        final RegistryShim<Fluid> fluidDeferredRegistryShim) {
        this.registryProvider = registryProvider;
        this.implClassGroupings = (AnonymousElementImplClassGrouping) implClassGroupings;
        this.blockDeferredRegistryShim = (BlockDeferredRegistryShim) blockDeferredRegistryShim;
        this.itemDeferredRegistryShim = (ItemDeferredRegistryShim) itemDeferredRegistryShim;
        this.fluidDeferredRegistryShim = (FluidDeferredRegistryShim) fluidDeferredRegistryShim;
        this.implClassGroupings.collectAnnotatedResources();
    }

    @Override
    public void registerAll() {
        this.implClassGroupings.getClassGroupings().forEach(this::registerAnonymousProvider);
    }

    private void registerAnonymousProvider(@Nonnull final String name,
                                           @Nonnull final AnonymousElementImplGrouping group) {
        for (final AnonymousElement element : group.getRegistrantElements()) {
            if (!element.blockSuppliers.isEmpty()) {
                registerBlocks(element);
            }
            if (!element.itemSuppliers.isEmpty()) {
                registerItems(element);
            }
            if (!element.sourceFluidSuppliers.isEmpty()) {
                registerSourceFluids(element);
            }
            if (!element.flowingFluidSuppliers.isEmpty()) {
                registerFlowingFluids(element);
            }
        }
    }

    private void registerBlocks(final AnonymousElement element) {
        element.blockSuppliers.forEach((final String providerName, final AttributedSupplier<Block> attributedSupplier) -> {
            markTagForDeferredRegistration(providerName, attributedSupplier, this.registryProvider.blockTagsToBeRegistered);
            if (attributedSupplier.registerBlockTagAsItemTag) {
                this.registryProvider.blockTagsToBeRegisteredAsItemTags.add(attributedSupplier.tagResource);
            }
            this.registryProvider.blocks.put(
                    providerName,
                    this.blockDeferredRegistryShim.register(providerName, attributedSupplier.supplier, attributedSupplier.tabGroup)
            );
        });
    }

    private void registerItems(final AnonymousElement element) {
        element.itemSuppliers.forEach((final String providerName, final AttributedSupplier<Item> attributedSupplier) -> {
            markTagForDeferredRegistration(providerName, attributedSupplier, this.registryProvider.itemTagsToBeRegistered);
            this.registryProvider.items.put(
                    providerName,
                    this.itemDeferredRegistryShim.register(providerName, attributedSupplier.supplier)
            );
        });
    }

    private void registerSourceFluids(final AnonymousElement element) {
        element.sourceFluidSuppliers.forEach((final String providerName, final AttributedSupplier<Fluid> attributedSupplier) -> {
            markTagForDeferredRegistration(providerName, attributedSupplier, this.registryProvider.sourceFluidTagsToBeRegistered);
            this.registryProvider.sourceFluids.put(
                    providerName,
                    this.fluidDeferredRegistryShim.register(providerName, attributedSupplier.supplier)
            );
        });
    }

    private void registerFlowingFluids(final AnonymousElement element) {
        element.flowingFluidSuppliers.forEach((final String providerName, final AttributedSupplier<FlowingFluid> attributedSupplier) -> {
            markTagForDeferredRegistration(providerName, attributedSupplier, this.registryProvider.flowingFluidTagsToBeRegistered);
            this.registryProvider.flowingFluids.put(
                    providerName,
                    this.fluidDeferredRegistryShim.register(providerName, attributedSupplier.supplier)
            );
        });
    }

    private <T> void markTagForDeferredRegistration(final String providerName,
                                                    final AttributedSupplier<T> attributedSupplier,
                                                    final Map<ResourceLocation, Set<String>> toBeRegistered) {
        if (attributedSupplier.tagResource == null) {
            return;
        }
        Set<String> tagsToBeRegistered = toBeRegistered.get(attributedSupplier.tagResource);
        if (tagsToBeRegistered == null) {
            tagsToBeRegistered = new HashSet<>();
        }
        tagsToBeRegistered.add(providerName);
        this.registryProvider.itemTagsToBeRegistered.put(attributedSupplier.tagResource, tagsToBeRegistered);
    }
}
