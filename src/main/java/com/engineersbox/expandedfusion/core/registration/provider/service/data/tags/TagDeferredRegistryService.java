package com.engineersbox.expandedfusion.core.registration.provider.service.data.tags;

import com.engineersbox.expandedfusion.core.registration.anonymous.element.TagBinding;
import com.engineersbox.expandedfusion.core.registration.contexts.provider.TagRegistryProvider;
import com.engineersbox.expandedfusion.core.registration.provider.service.RegistryService;
import com.google.inject.Inject;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TagDeferredRegistryService extends RegistryService<ITag.INamedTag<?>> {

    private final TagRegistryProvider tagRegistryProvider;

    @Inject
    public TagDeferredRegistryService(final TagRegistryProvider tagRegistryProvider) {
        this.tagRegistryProvider = tagRegistryProvider;
    }

    public void bindBlockTag(final String providerName,
                             final Pair<TagBinding<Block>, Boolean> tagPair) {
        final TagBinding<Block> tagBinding = tagPair.getKey();
        ITag.INamedTag<Block> blockTag = tagBinding.getTag();
        if (blockTag == null && tagBinding.getTagResource() != null) {
            blockTag = BlockTags.createOptional(tagBinding.getTagResource());
        }
        if (blockTag != null) {
            markTagForDeferredRegistration(providerName, blockTag, this.tagRegistryProvider.blockTagsToBeRegistered, tagPair.getValue());
            this.tagRegistryProvider.blockTags.put(blockTag.getName(), blockTag);
            ITag.INamedTag<Item> mirrorTag = tagBinding.getMirroredTag();
            if (mirrorTag == null && tagBinding.getMirroredTagResource() != null) {
                mirrorTag = ItemTags.createOptional(tagBinding.getMirroredTagResource());
            }
            if (mirrorTag != null) {
                this.tagRegistryProvider.blockTagsToBeRegisteredAsItemTags.put(blockTag, mirrorTag);
            }
        }
    }

    public void bindItemTag(final String providerName,
                            final Pair<TagBinding<Item>, Boolean> tagPair) {
        final TagBinding<Item> tagBinding = tagPair.getKey();
        ITag.INamedTag<Item> itemTag = tagBinding.getTag();
        if (itemTag == null && tagBinding.getTagResource() != null) {
            itemTag = ItemTags.createOptional(tagBinding.getTagResource());
        }
        if (itemTag != null) {
            this.tagRegistryProvider.itemTags.put(itemTag.getName(), itemTag);
            markTagForDeferredRegistration(providerName, itemTag, this.tagRegistryProvider.itemTagsToBeRegistered, tagPair.getValue());
        }
    }

    public void bindSourceFluidTag(final String providerName,
                                   final Pair<TagBinding<Fluid>, Boolean> tagPair) {
        final TagBinding<Fluid> tagBinding = tagPair.getKey();
        ITag.INamedTag<Fluid> fluidTag = tagBinding.getTag();
        if (fluidTag == null && tagBinding.getTagResource() != null) {
            fluidTag = FluidTags.createOptional(tagBinding.getTagResource());
        }
        if (fluidTag != null) {
            this.tagRegistryProvider.fluidTags.put(fluidTag.getName(), fluidTag);
            markTagForDeferredRegistration(providerName, fluidTag, this.tagRegistryProvider.sourceFluidTagsToBeRegistered, tagPair.getValue());
        }
    }

    public void bindFlowingFluidTag(final String providerName,
                                    final Pair<TagBinding<Fluid>, Boolean> tagPair) {
        final TagBinding<Fluid> tagBinding = tagPair.getKey();
        ITag.INamedTag<Fluid> fluidTag = tagBinding.getTag();
        if (fluidTag == null && tagBinding.getTagResource() != null) {
            fluidTag = FluidTags.createOptional(tagBinding.getTagResource());
        }
        if (fluidTag != null) {
            this.tagRegistryProvider.fluidTags.put(fluidTag.getName(), fluidTag);
            markTagForDeferredRegistration(providerName, fluidTag, this.tagRegistryProvider.flowingFluidTagsToBeRegistered, tagPair.getValue());
        }
    }

    private <T> void markTagForDeferredRegistration(final String providerName,
                                                    final ITag.INamedTag<T> tag,
                                                    final Map<ITag.INamedTag<T>, Pair<Set<String>, Boolean>> toBeRegistered,
                                                    final boolean replace) {
        final Pair<Set<String>, Boolean> tagPair = toBeRegistered.get(tag);
        final Set<String> tagsToBeRegistered = tagPair == null ? new HashSet<>() : tagPair.getKey();
        tagsToBeRegistered.add(providerName);
        toBeRegistered.put(tag, ImmutablePair.of(tagsToBeRegistered, (tagPair != null && tagPair.getValue()) || replace));
    }

}
