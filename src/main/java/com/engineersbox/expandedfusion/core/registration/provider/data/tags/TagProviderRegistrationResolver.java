package com.engineersbox.expandedfusion.core.registration.provider.data.tags;

import com.engineersbox.expandedfusion.core.registration.annotation.data.tag.Tag;
import com.engineersbox.expandedfusion.core.registration.annotation.resolver.RegistrationPhaseHandler;
import com.engineersbox.expandedfusion.core.registration.anonymous.element.TagBinding;
import com.engineersbox.expandedfusion.core.registration.exception.provider.data.ProviderDataRegistrationException;
import com.engineersbox.expandedfusion.core.registration.provider.RegistrationResolver;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.ImplClassGroupings;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.tags.TagImplClassGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.grouping.data.tags.TagImplGrouping;
import com.engineersbox.expandedfusion.core.registration.provider.service.RegistryService;
import com.engineersbox.expandedfusion.core.registration.provider.service.data.tags.TagDeferredRegistryService;
import com.engineersbox.expandedfusion.core.registration.resolver.ResolverPhase;
import com.google.inject.Inject;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.annotation.Nonnull;

@RegistrationPhaseHandler(ResolverPhase.TAGS)
public class TagProviderRegistrationResolver implements RegistrationResolver {

    private final TagImplClassGrouping implClassGroupings;
    private final TagDeferredRegistryService tagDeferredRegistryService;

    @Inject
    public TagProviderRegistrationResolver(final ImplClassGroupings<TagImplGrouping> implClassGroupings,
                                           final RegistryService<ITag.INamedTag<?>> tagDeferredRegistryService) {
        this.implClassGroupings = (TagImplClassGrouping) implClassGroupings;
        this.tagDeferredRegistryService = (TagDeferredRegistryService) tagDeferredRegistryService;
        this.implClassGroupings.collectAnnotatedResources();
    }

    @Override
    public void registerAll() {
        this.implClassGroupings.getClassGroupings().forEach(this::registerTag);
    }

    private void registerTag(@Nonnull final String name,
                             @Nonnull final TagImplGrouping group) {
        final Class<? extends ForgeRegistryEntry<?>> registryEntry = group.getRegistryEntry();
        if (Block.class.isAssignableFrom(registryEntry)) {
            registerBlockTag(name, group);
        } else if (Item.class.isAssignableFrom(registryEntry)) {
            registerItemTag(name, group);
        } else if (ForgeFlowingFluid.Flowing.class.isAssignableFrom(registryEntry)) {
            registerFlowingFluidTag(name, group);
        } else if (ForgeFlowingFluid.Source.class.isAssignableFrom(registryEntry)) {
            registerSourceFluidTag(name, group);
        } else {
            throw new ProviderDataRegistrationException(String.format(
                    "Class with @Tag annotation was not of taggable registry entry type: %s",
                    registryEntry.getName()
            ));
        }
    }

    private void registerBlockTag(final String name,
                                  final TagImplGrouping group) {
        final Tag[] tagAnnotations = group.getTagAnnotations();
        for (final Tag tag : tagAnnotations) {
            final TagBinding<Block> tagBinding = new TagBinding<>();
            tagBinding.setTagResource(new ResourceLocation(
                    tag.namespace(),
                    tag.path()
            ));
            if (StringUtils.isNotEmpty(tag.mirroredItemTagPath())) {
                tagBinding.setMirroredTagResource(new ResourceLocation(
                        tag.mirroredItemTagNamespace(),
                        tag.mirroredItemTagPath()
                ));
            }
            this.tagDeferredRegistryService.bindBlockTag(
                    StringUtils.isEmpty(tag.provider()) ? name : tag.provider(),
                    ImmutablePair.of(tagBinding, tag.replace())
            );
        }
    }

    private void registerItemTag(final String name,
                                 final TagImplGrouping group) {
        final Tag[] tagAnnotations = group.getTagAnnotations();
        for (final Tag tag : tagAnnotations) {
            final TagBinding<Item> tagBinding = new TagBinding<>();
            tagBinding.setTagResource(new ResourceLocation(
                    tag.namespace(),
                    tag.path()
            ));
            this.tagDeferredRegistryService.bindItemTag(
                    StringUtils.isEmpty(tag.provider()) ? name : tag.provider(),
                    ImmutablePair.of(tagBinding, tag.replace())
            );
        }
    }

    private void registerSourceFluidTag(final String name,
                                        final TagImplGrouping group) {
        final Tag[] tagAnnotations = group.getTagAnnotations();
        for (final Tag tag : tagAnnotations) {
            final TagBinding<Fluid> tagBinding = new TagBinding<>();
            tagBinding.setTagResource(new ResourceLocation(
                    tag.namespace(),
                    tag.path()
            ));
            this.tagDeferredRegistryService.bindSourceFluidTag(
                    StringUtils.isEmpty(tag.provider()) ? name : tag.provider(),
                    ImmutablePair.of(tagBinding, tag.replace())
            );
        }
    }

    private void registerFlowingFluidTag(final String name,
                                         final TagImplGrouping group) {
        final Tag[] tagAnnotations = group.getTagAnnotations();
        for (final Tag tag : tagAnnotations) {
            final TagBinding<Fluid> tagBinding = new TagBinding<>();
            tagBinding.setTagResource(new ResourceLocation(
                    tag.namespace(),
                    tag.path()
            ));
            this.tagDeferredRegistryService.bindFlowingFluidTag(
                    StringUtils.isEmpty(tag.provider()) ? name : tag.provider(),
                    ImmutablePair.of(tagBinding, tag.replace())
            );
        }
    }

}
