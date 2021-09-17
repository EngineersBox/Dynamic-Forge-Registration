package com.engineersbox.expandedfusion.core.registration.provider.anonymous;

import com.engineersbox.expandedfusion.core.registration.annotation.resolver.RegistrationPhaseHandler;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.AttributedSupplier;
import com.engineersbox.expandedfusion.core.registration.contexts.provider.ElementRegistryProvider;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.anonymous.AnonymousElementImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.anonymous.AnonymousElementImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.shim.RegistryShim;
import com.engineersbox.expandedfusion.core.registration.provider.shim.data.tags.TagDeferredRegistryShim;
import com.engineersbox.expandedfusion.core.registration.provider.shim.element.*;
import com.engineersbox.expandedfusion.core.registration.resolver.ResolverPhase;
import com.google.inject.Inject;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;

import javax.annotation.Nonnull;

@RegistrationPhaseHandler(ResolverPhase.ANONYMOUS_ELEMENT)
public class AnonymousElementProviderRegistrationResolver extends RegistrationResolver {

    private final AnonymousElementImplClassGrouping implClassGroupings;
    private final BlockDeferredRegistryShim blockDeferredRegistryShim;
    private final ItemDeferredRegistryShim itemDeferredRegistryShim;
    private final FluidDeferredRegistryShim fluidDeferredRegistryShim;
    private final TagDeferredRegistryShim tagDeferredRegistryShim;
    private final ElementRegistryProvider elementRegistryProvider;

    @Inject
    public AnonymousElementProviderRegistrationResolver(final ElementRegistryProvider elementRegistryProvider,
                                                        final ImplClassGroupings<AnonymousElementImplGrouping> implClassGroupings,
                                                        final RegistryShim<Block> blockDeferredRegistryShim,
                                                        final RegistryShim<Item> itemDeferredRegistryShim,
                                                        final RegistryShim<Fluid> fluidDeferredRegistryShim,
                                                        final RegistryShim<ITag.INamedTag<?>> tagDeferredRegistryShim) {
        this.elementRegistryProvider = elementRegistryProvider;
        this.implClassGroupings = (AnonymousElementImplClassGrouping) implClassGroupings;
        this.blockDeferredRegistryShim = (BlockDeferredRegistryShim) blockDeferredRegistryShim;
        this.itemDeferredRegistryShim = (ItemDeferredRegistryShim) itemDeferredRegistryShim;
        this.fluidDeferredRegistryShim = (FluidDeferredRegistryShim) fluidDeferredRegistryShim;
        this.tagDeferredRegistryShim = (TagDeferredRegistryShim) tagDeferredRegistryShim;
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
        element.blockSuppliers.forEach((final String providerName, final AttributedSupplier<Block, Block> attributedSupplier) -> {
            this.tagDeferredRegistryShim.bindBlockTag(providerName, attributedSupplier.getTagBinding());
            this.elementRegistryProvider.blocks.put(
                    providerName,
                    this.blockDeferredRegistryShim.register(providerName, attributedSupplier.getSupplier(), attributedSupplier.getTabGroup())
            );
        });
    }

    private void registerItems(final AnonymousElement element) {
        element.itemSuppliers.forEach((final String providerName, final AttributedSupplier<Item, Item> attributedSupplier) -> {
            this.tagDeferredRegistryShim.bindItemTag(providerName, attributedSupplier.getTagBinding());
            this.elementRegistryProvider.items.put(
                    providerName,
                    this.itemDeferredRegistryShim.register(providerName, attributedSupplier.getSupplier())
            );
        });
    }

    private void registerSourceFluids(final AnonymousElement element) {
        element.sourceFluidSuppliers.forEach((final String providerName, final AttributedSupplier<Fluid, Fluid> attributedSupplier) -> {
            this.tagDeferredRegistryShim.bindFlowingFluidTag(providerName, attributedSupplier.getTagBinding());
            this.elementRegistryProvider.sourceFluids.put(
                    providerName,
                    this.fluidDeferredRegistryShim.register(providerName, attributedSupplier.getSupplier())
            );
        });
    }

    private void registerFlowingFluids(final AnonymousElement element) {
        element.flowingFluidSuppliers.forEach((final String providerName, final AttributedSupplier<FlowingFluid, Fluid> attributedSupplier) -> {
            this.tagDeferredRegistryShim.bindSourceFluidTag(providerName, attributedSupplier.getTagBinding());
            this.elementRegistryProvider.flowingFluids.put(
                    providerName,
                    this.fluidDeferredRegistryShim.register(providerName, attributedSupplier.getSupplier())
            );
        });
    }
}
