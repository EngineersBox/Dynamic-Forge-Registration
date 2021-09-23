package com.engineersbox.expandedfusion.core.registration.provider.anonymous;

import com.engineersbox.expandedfusion.core.registration.annotation.resolver.RegistrationPhaseHandler;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.AttributedSupplier;
import com.engineersbox.expandedfusion.core.registration.contexts.provider.ElementRegistryProvider;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.anonymous.AnonymousElementImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.anonymous.AnonymousElementImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.service.RegistryService;
import com.engineersbox.expandedfusion.core.registration.provider.service.data.tags.TagDeferredRegistryService;
import com.engineersbox.expandedfusion.core.registration.provider.service.element.*;
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
    private final BlockDeferredRegistryService blockDeferredRegistryService;
    private final ItemDeferredRegistryService itemDeferredRegistryService;
    private final FluidDeferredRegistryService fluidDeferredRegistryService;
    private final TagDeferredRegistryService tagDeferredRegistryService;
    private final ElementRegistryProvider elementRegistryProvider;

    @Inject
    public AnonymousElementProviderRegistrationResolver(final ElementRegistryProvider elementRegistryProvider,
                                                        final ImplClassGroupings<AnonymousElementImplGrouping> implClassGroupings,
                                                        final RegistryService<Block> blockDeferredRegistryService,
                                                        final RegistryService<Item> itemDeferredRegistryService,
                                                        final RegistryService<Fluid> fluidDeferredRegistryService,
                                                        final RegistryService<ITag.INamedTag<?>> tagDeferredRegistryService) {
        this.elementRegistryProvider = elementRegistryProvider;
        this.implClassGroupings = (AnonymousElementImplClassGrouping) implClassGroupings;
        this.blockDeferredRegistryService = (BlockDeferredRegistryService) blockDeferredRegistryService;
        this.itemDeferredRegistryService = (ItemDeferredRegistryService) itemDeferredRegistryService;
        this.fluidDeferredRegistryService = (FluidDeferredRegistryService) fluidDeferredRegistryService;
        this.tagDeferredRegistryService = (TagDeferredRegistryService) tagDeferredRegistryService;
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
            this.tagDeferredRegistryService.bindBlockTag(providerName, attributedSupplier.getTagBinding());
            this.elementRegistryProvider.blocks.put(
                    providerName,
                    this.blockDeferredRegistryService.register(providerName, attributedSupplier.getSupplier(), attributedSupplier.getTabGroup())
            );
        });
    }

    private void registerItems(final AnonymousElement element) {
        element.itemSuppliers.forEach((final String providerName, final AttributedSupplier<Item, Item> attributedSupplier) -> {
            this.tagDeferredRegistryService.bindItemTag(providerName, attributedSupplier.getTagBinding());
            this.elementRegistryProvider.items.put(
                    providerName,
                    this.itemDeferredRegistryService.register(providerName, attributedSupplier.getSupplier())
            );
        });
    }

    private void registerSourceFluids(final AnonymousElement element) {
        element.sourceFluidSuppliers.forEach((final String providerName, final AttributedSupplier<Fluid, Fluid> attributedSupplier) -> {
            this.tagDeferredRegistryService.bindFlowingFluidTag(providerName, attributedSupplier.getTagBinding());
            this.elementRegistryProvider.sourceFluids.put(
                    providerName,
                    this.fluidDeferredRegistryService.register(providerName, attributedSupplier.getSupplier())
            );
        });
    }

    private void registerFlowingFluids(final AnonymousElement element) {
        element.flowingFluidSuppliers.forEach((final String providerName, final AttributedSupplier<FlowingFluid, Fluid> attributedSupplier) -> {
            this.tagDeferredRegistryService.bindSourceFluidTag(providerName, attributedSupplier.getTagBinding());
            this.elementRegistryProvider.flowingFluids.put(
                    providerName,
                    this.fluidDeferredRegistryService.register(providerName, attributedSupplier.getSupplier())
            );
        });
    }
}
